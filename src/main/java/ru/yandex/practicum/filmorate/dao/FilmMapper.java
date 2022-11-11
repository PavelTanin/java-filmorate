package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FilmMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("ID"));
        film.setName(rs.getString("NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setDuration(rs.getInt("DURATION"));
        film.setReleaseDate(rs.getDate("RELEASEDATE").toLocalDate());
        film.setRate(rs.getInt("RATE"));
        if (rs.getObject("MPA") != null) {
            film.setMpa(new Mpa(rs.getInt("MPA"), rs.getString("MPA_NAME")));
        }
        if (rs.getObject("film_genre") != null) {
            List<String> genres_id = List.of(rs.getString("film_genre_id").split(","));
            List<String> genres = List.of(rs.getString("film_genre").split(","));
            for (int i = 0; i <= genres.size() - 1; i++) {
                film.getGenres().add(new Genre(Integer.parseInt(genres_id.get(i)), genres.get(i)));
            }
        }
        return film;
    }
}
