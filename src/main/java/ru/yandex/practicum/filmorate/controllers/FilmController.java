package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class FilmController {

    private int id;
    private final HashMap<Integer, Film> filmList = new HashMap<>();

    @GetMapping("/films")
    public List<Film> findAll() {
        return new ArrayList<>(filmList.values());
    }

    @PostMapping("/films")
    @ResponseBody
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        if (releaseDateIsValid(film)) {
            film.setId(idGenerator());
            filmList.put(film.getId(), film);
            log.info("Добавлен фильм: {}", film);
            return new ResponseEntity<>(film, HttpStatus.valueOf(201));
        } else {
            log.debug("Ошибка при добавлении фильма - неверно заполнено одно(или несколько) из полей");
            return new ResponseEntity<>(HttpStatus.valueOf(500));
        }
    }

    @PutMapping("/films")
    @ResponseBody
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        if (releaseDateIsValid(film) && film.getId() > 0) {
            if (filmList.containsKey(film.getId())) {
                filmUpdater(film);
                log.info("Информация о фильме " + film.getName() + " обновлена: {}", film);
            } else {
                addFilm(film);
            }
            return new ResponseEntity<>(film, HttpStatus.valueOf(200));
        } else {
            log.debug("Ошибка при обновлении фильма - неверно заполнено одно(или несколько) из полей");
            return new ResponseEntity<>(HttpStatus.valueOf(500));
        }
    }

    protected boolean releaseDateIsValid(Film film) {
        try {
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ValidationException("Дата релиза раньше 28 декабря 1985 года");
            } else {
                return true;
            }
        } catch (ValidationException e) {
            System.out.printf(e.getMessage());
        }
        return false;
    }

    private void filmUpdater(Film film) {
        var updatedUser = filmList.get(film.getId());
        updatedUser.setName(film.getName());
        updatedUser.setDescription(film.getDescription());
        updatedUser.setReleaseDate(film.getReleaseDate());
        updatedUser.setDuration(film.getDuration());
    }

    private int idGenerator() {
        id++;
        return id;
    }
}
