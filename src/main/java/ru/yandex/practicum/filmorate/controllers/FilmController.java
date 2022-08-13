package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@ResponseBody
@RequestMapping("/films")
public class FilmController {

    private int id;
    private final HashMap<Integer, Film> filmList = new HashMap<>();

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(filmList.values());
    }

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        if (releaseDateIsValid(film)) {
            film.setId(idGenerator());
            filmList.put(film.getId(), film);
            log.info("Добавлен фильм: {}", film);
            return new ResponseEntity<>(film, HttpStatus.CREATED);
        } else {
            log.debug("Ошибка при добавлении фильма - неверно заполнено одно(или несколько) из полей");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        if (releaseDateIsValid(film) && film.getId() > 0) {
            if (filmList.containsKey(film.getId())) {
                filmUpdater(film);
                log.info("Информация о фильме {} обновлена: {}", film.getName(),film);
            } else {
                addFilm(film);
            }
            return new ResponseEntity<>(film, HttpStatus.OK);
        } else {
            log.debug("Ошибка при обновлении фильма - неверно заполнено одно(или несколько) из полей");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    protected boolean releaseDateIsValid(Film film) {
        try {
            if (film.getReleaseDate() == null ||
                    film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ValidationException("Некорректно введена дата релиза");
            } else if (film.getName() == null) {
                throw new ValidationException("Некорректно введено название фильма");
            } else if (film.getDescription() == null) {
                throw new ValidationException("Некорректно введено описание фильма");
            } else if (film.getDuration() == null) {
                throw new ValidationException("Некорректно введена продолжительность фильма");
            }else {
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
