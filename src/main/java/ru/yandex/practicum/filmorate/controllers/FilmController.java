package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NegativeCountParam;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.storages.film.FilmStorage;
import ru.yandex.practicum.filmorate.storages.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@ResponseBody
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmService filmService, InMemoryFilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film findById(@PathVariable(value = "id") Integer id) {
        return filmStorage.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @PutMapping("{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public String addLike(@PathVariable(value = "id") Integer id,
                          @PathVariable(value = "userId") Integer userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteLike(@PathVariable(value = "id") Integer id,
                             @PathVariable(value = "userId") Integer userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getPopulatFilms(@RequestParam(defaultValue = "10") Integer count) {
        if (count <= 0) {
            throw new NegativeCountParam("Длинна списка не может быть отрицательной или ровняться нулю");
        }

        return filmService.getPopularFilms(count);
    }


}
