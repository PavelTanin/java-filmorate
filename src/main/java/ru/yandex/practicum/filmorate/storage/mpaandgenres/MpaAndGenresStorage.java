package ru.yandex.practicum.filmorate.storage.mpaandgenres;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaAndGenresStorage {

    Mpa findMpaById(Integer id);

    List<Mpa> findAllMpa();

    Genre findGenreById(Integer id);

    List<Genre> findAllGenres();

}
