package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Integer id);

    List<Film> findAll();

    Film findById(Integer id);

    void addLike(Integer id, Integer userId);

    void deleteLike(Integer id, Integer userId);

    List<Film> getPopularFilms(Integer count);

    boolean containsLike(Integer id, Integer userId);

    boolean contains(Integer id);
}
