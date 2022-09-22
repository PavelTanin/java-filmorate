package ru.yandex.practicum.filmorate.storage.mpaandgenres;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreMapper;
import ru.yandex.practicum.filmorate.dao.MpaMapper;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class MpaAndGenreDBStorage implements MpaAndGenresStorage{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Mpa> findMpaById(Integer id) {
        if (!mpaContains(id)) {
            throw new ObjectNotFoundException("Такого рейтинга нет");
        }
        return Optional.of(jdbcTemplate.queryForObject("SELECT * FROM MPA_RATING WHERE MPA_ID = ?", new MpaMapper(), id));
    }

    @Override
    public List<Mpa> findAllMpa() {
        return jdbcTemplate.query("SELECT * FROM MPA_RATING", new MpaMapper());
    }

    @Override
    public Optional<Genre> findGenreById(Integer id) {
        if (!mpaContains(id)) {
            throw new ObjectNotFoundException("Такого жанра нет");
        }
        return Optional.of(jdbcTemplate.queryForObject("SELECT * FROM GENRES WHERE GENRE_ID = ?", new GenreMapper(), id));
    }

    @Override
    public List<Genre> findAllGenres() {
        return jdbcTemplate.query("SELECT * FROM GENRES", new GenreMapper());
    }

    @Override
    public boolean mpaContains(Integer id) {
        return jdbcTemplate.queryForList("SELECT MPA_ID FROM MPA_RATING", Integer.class).contains(id);
    }

    @Override
    public boolean genreContains(Integer id) {
        return jdbcTemplate.queryForList("SELECT GENRE_ID FROM GENRES", Integer.class).contains(id);
    }
}
