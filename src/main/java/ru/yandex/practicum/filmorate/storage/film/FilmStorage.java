package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    List<Film> findAll();

    Optional<Film> findById(Integer id);

    Optional<Film> addFilm(Film film);

    Optional<Film> updateFilm(Film film);

    //Integer idGenerator();

    boolean contains(Integer id);
}
