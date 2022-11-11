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

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class MpaAndGenreDBStorage implements MpaAndGenresStorage{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa findMpaById(Integer id) {
        if (id <= 0) {
            throw new ObjectNotFoundException("Некорректный номер рейтинга MPA");
        }
        Mpa mpa = jdbcTemplate.queryForObject("SELECT * FROM MPA_RATING WHERE MPA_ID = ?", new MpaMapper(), id);
        if (mpa == null) {
            throw new ObjectNotFoundException("Такого рейтинга нет");
        } else {
            return mpa;
        }
    }

    @Override
    public List<Mpa> findAllMpa() {
        return jdbcTemplate.query("SELECT * FROM MPA_RATING", new MpaMapper());
    }

    @Override
    public Genre findGenreById(Integer id) {
        if (id <= 0) {
            throw new ObjectNotFoundException("Некорректный номер жанра");
        }
        Genre genre = jdbcTemplate.queryForObject("SELECT * FROM GENRES WHERE GENRE_ID = ?", new GenreMapper(), id);
        if (genre == null) {
            throw new ObjectNotFoundException("Такого жанра нет");
        }
        return genre;
    }

    @Override
    public List<Genre> findAllGenres() {
        return jdbcTemplate.query("SELECT * FROM GENRES", new GenreMapper());
    }
}
