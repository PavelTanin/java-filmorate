package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmMapper;
import ru.yandex.practicum.filmorate.dao.GenreMapper;
import ru.yandex.practicum.filmorate.dao.MpaMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenresSorter;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage {

    private int id;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> findAll() {
        List<Film> result = jdbcTemplate.query("SELECT * FROM FILMS", new FilmMapper());
        for (Film film : result) {
            film.setMpa(jdbcTemplate.queryForObject("SELECT m.* FROM FILMS AS f INNER JOIN MPA_RATING AS m ON m.MPA_ID = f.MPA WHERE ID = ?",
                    new MpaMapper(), film.getId()));
            film.getGenres().addAll(jdbcTemplate.query("SELECT g.* FROM FILM_GENRE AS fg INNER JOIN GENRES AS g ON g.GENRE_ID = fg.GENRE_ID WHERE FILM_ID = ?",
                    new GenreMapper(), film.getId()));
        }
        log.info("Получен список фильмов");
        return result;
    }

    @Override
    public Optional<Film> findById(Integer id) {
        Film result = jdbcTemplate.queryForObject("SELECT * FROM FILMS WHERE ID = ?", new FilmMapper(), id);
        result.setMpa(jdbcTemplate.queryForObject("SELECT m.* FROM FILMS AS f INNER JOIN MPA_RATING AS m on m.MPA_ID = f.MPA WHERE ID = ?",
                new MpaMapper(), result.getId()));
        result.getGenres().addAll(jdbcTemplate.query("SELECT g.* FROM FILM_GENRE AS fg INNER JOIN GENRES AS g ON g.GENRE_ID = fg.GENRE_ID WHERE FILM_ID = ? ORDER BY g.GENRE_ID ASC",
                new GenreMapper(), result.getId()));
        log.info("Получен фильм");
        return Optional.of(result);
    }

    @Override
    public Optional<Film> addFilm(Film film) {
        jdbcTemplate.update("INSERT INTO FILMS (NAME, DESCRIPTION, DURATION, RELEASEDATE, RATE, MPA) VALUES (?, ?, ?, ?, ?, ?)", film.getName(),
                film.getDescription(), film.getDuration(), film.getReleaseDate(), film.getRate(), film.getMpa().getId());
        film.setId(getCreatedId());
        if (!film.getGenres().isEmpty()) {
            updateGenres(film);
        }
        log.info("Добавлен фильм {} - {}", film.getId(), film.getName());
        return findById(film.getId());
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        jdbcTemplate.update("UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, DURATION = ?, RELEASEDATE = ?," +
                        "RATE = ?, MPA = ? WHERE ID = ?", film.getName(), film.getDescription(), film.getDuration(),
                film.getReleaseDate(), film.getRate(), film.getMpa().getId(), film.getId());
        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE FILM_ID = ?", film.getId());
        if (!film.getGenres().isEmpty()) {
            updateGenres(film);
        }
        log.info("Обновлена информация о пользователе {}", film.getId());
        return findById(film.getId());
    }

    private void updateGenres(Film film) {
        List<Genre> genres = new ArrayList<>(film.getGenres());
        Collections.sort(genres, new GenresSorter());
        for (Genre genre : genres) {
            jdbcTemplate.update("INSERT INTO FILM_GENRE VALUES (?, ?)", film.getId(), genre.getId());
        }
    }


    private Integer getCreatedId() {
        return jdbcTemplate.queryForObject("SELECT ID FROM FILMS ORDER BY ID DESC LIMIT 1", Integer.class);
    }

/*    @Override
    public Integer idGenerator() {
        id++;
        return id;
    }*/

    @Override
    public boolean contains(Integer id) {
        return jdbcTemplate.queryForList("SELECT ID FROM FILMS", Integer.class).contains(id);
    }
}
