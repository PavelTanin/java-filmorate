package ru.yandex.practicum.filmorate.storages.film;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> findAll();

    Film findById(Integer id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Integer idGenerator();

}
