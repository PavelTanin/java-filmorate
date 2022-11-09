package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenresSorter;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilm(Film film) {
        String query = "INSERT INTO FILMS (NAME, DESCRIPTION, DURATION, RELEASEDATE, RATE, MPA) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setInt(3, film.getDuration());
            ps.setDate(4, Date.valueOf(film.getReleaseDate()));
            ps.setInt(5, film.getRate());
            ps.setInt(6, film.getMpa().getId());
            return ps;
        }, kh);
        film.setId((Integer) kh.getKey());
        if (!film.getGenres().isEmpty()) {
            log.info("Добавлен фильм {} - {}", film.getId(), film.getName());
            return updateGenres(film);
        }
        log.info("Добавлен фильм {} - {}", film.getId(), film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update("UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, DURATION = ?, RELEASEDATE = ?," +
                        "RATE = ?, MPA = ? WHERE ID = ?", film.getName(), film.getDescription(), film.getDuration(),
                film.getReleaseDate(), film.getRate(), film.getMpa().getId(), film.getId());
        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE FILM_ID = ?", film.getId());
        if (!film.getGenres().isEmpty()) {
            log.info("Обновлена информация о фильме {}", film.getId());
            return updateGenres(film);
        }
        log.info("Обновлена информация о фильме {}", film.getId());
        return film;
    }

    @Override
    public void deleteFilm(Integer id) {
        jdbcTemplate.update("DELETE FROM FILMS WHERE ID = ?", id);
        log.info("Фильм {} удален", id);
    }

    @Override
    public List<Film> findAll() {
        List<Film> result = jdbcTemplate.query("SELECT f.*, m.MPA as mpa_name," +
                "string_agg(g.GENRE_ID, ',') as film_genre_id," +
                "string_agg(g.GENRE, ',') as film_genre " +
                "FROM GENRES AS g " +
                "RIGHT OUTER JOIN FILM_GENRE AS fg ON g.GENRE_ID = fg.GENRE_ID " +
                "RIGHT OUTER JOIN FILMS AS f ON f.ID=fg.FILM_ID " +
                "INNER JOIN MPA_RATING AS m ON f.MPA = m.MPA_ID " +
                "GROUP BY f.ID", new FilmMapper());
        log.info("Получен список фильмов");
        return result;
    }

    @Override
    public Film findById(Integer id) {
        Film result = jdbcTemplate.queryForObject("SELECT f.*, m.MPA as mpa_name," +
                "string_agg(g.GENRE_ID, ',') as film_genre_id," +
                "string_agg(g.GENRE, ',') as film_genre " +
                "FROM GENRES AS g " +
                "INNER JOIN FILM_GENRE AS fg ON g.GENRE_ID = fg.GENRE_ID " +
                "RIGHT OUTER JOIN FILMS AS f ON f.ID=fg.FILM_ID " +
                "INNER JOIN MPA_RATING AS m ON f.MPA = m.MPA_ID WHERE f.ID = ? " +
                "GROUP BY f.ID", new FilmMapper(), id);
        log.info("Получен фильм");
        return result;
    }

    private Film updateGenres(Film film) {
        film.setGenres(film.getGenres().stream()
                .sorted(new GenresSorter())
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update("INSERT INTO FILM_GENRE VALUES (?, ?)", film.getId(), genre.getId());
        }
        return film;
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return jdbcTemplate.query("SELECT f.*, m.MPA as mpa_name," +
        "string_agg(g.GENRE_ID, ',') as film_genre_id," +
                "string_agg(g.GENRE, ',') as film_genre " +
                "FROM GENRES AS g " +
                "INNER JOIN FILM_GENRE AS fg ON g.GENRE_ID = fg.GENRE_ID " +
                "RIGHT OUTER JOIN FILMS AS f ON f.ID=fg.FILM_ID " +
                "INNER JOIN MPA_RATING AS m ON f.MPA = m.MPA_ID " +
                "GROUP BY f.ID ORDER BY f.RATE DESC LIMIT ?", new FilmMapper(), count);
    }

    @Override
    public boolean contains(Integer id) {
        return jdbcTemplate.queryForObject("SELECT COUNT(ID) FROM FILMS WHERE ID = ?", Integer.class, id) > 0;
    }
}
