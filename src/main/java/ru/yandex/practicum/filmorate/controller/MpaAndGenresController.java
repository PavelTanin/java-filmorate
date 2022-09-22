package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpaandgenres.MpaAndGenreDBStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@ResponseBody
public class MpaAndGenresController {

    private final MpaAndGenreDBStorage mpaAndGenreDBStorage;

    @GetMapping("/mpa")
    @ResponseStatus(HttpStatus.OK)
    public List<Mpa> findAllMpa() {
        return mpaAndGenreDBStorage.findAllMpa();
    }

    @GetMapping("/mpa/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Mpa> findMpaById(@PathVariable(value = "id") Integer id) {
        return mpaAndGenreDBStorage.findMpaById(id);
    }

    @GetMapping("/genres")
    @ResponseStatus(HttpStatus.OK)
    public List<Genre> findAllGenres() {
        return mpaAndGenreDBStorage.findAllGenres();
    }

    @GetMapping("/genres/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Genre> findGenreById(@PathVariable(value = "id") Integer id) {
        return mpaAndGenreDBStorage.findGenreById(id);
    }


}
