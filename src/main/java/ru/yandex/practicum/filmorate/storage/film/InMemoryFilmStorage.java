package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private int id;
    private final Map<Integer, Film> filmList = new HashMap<>();

    @Override
    public List<Film> findAll() {
        log.info("Получен список фильмов");
        return new ArrayList<>(filmList.values());
    }

    @Override
    public Optional<Film> findById(Integer id) {
        log.info("Найден фильм {}", id);
        return Optional.ofNullable(filmList.get(id));
    }

    @Override
    public Optional<Film> addFilm(Film film) {
        film.setId(idGenerator());
        filmList.put(film.getId(), film);
        log.info("Добавлен фильм {} - {}", id, film.getName());
        return Optional.of(film);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        var updatedUser = filmList.get(film.getId());
        updatedUser.setName(film.getName());
        updatedUser.setDescription(film.getDescription());
        updatedUser.setReleaseDate(film.getReleaseDate());
        updatedUser.setDuration(film.getDuration());
        return Optional.of(film);
    }

    //@Override
    public Integer idGenerator() {
        id++;
        return id;
    }

    @Override
    public boolean contains(Integer id) {
        return filmList.containsKey(id);
    }
}
