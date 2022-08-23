package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> findAll();

    Film findById(Integer id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Integer idGenerator();

    boolean contains(Integer id);
}
