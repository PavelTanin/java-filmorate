package ru.yandex.practicum.filmorate.storage.mpaandgenres;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaAndGenresStorage {

    Optional<Mpa> findMpaById(Integer id);

    List<Mpa> findAllMpa();

    Optional<Genre> findGenreById(Integer id);

    List<Genre> findAllGenres();

    boolean mpaContains(Integer id);

    boolean genreContains(Integer id);

}
