package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.CustomValidator;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.storages.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storages.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmValidationTest {

    CustomValidator customValidator = new CustomValidator();
    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage(customValidator);

    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage(customValidator);
    FilmService filmService = new FilmService(inMemoryFilmStorage, inMemoryUserStorage);
    FilmController filmController = new FilmController(filmService, inMemoryFilmStorage);

    @Test
    void shouldThrowBadResponseWhenReleaseDateIsWrong() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Discription");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1895,12,27));
        Throwable exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Некорректно введена дата релиза", exception.getMessage());
    }

    @Test
    void shouldThrowBadResponseWhenReleaseDateIsNull() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Discription");
        film.setDuration(100);
        Throwable exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Некорректно введена дата релиза", exception.getMessage());
    }

    @Test
    void shouldThrowBadResponseWhenNameIsNull() {
        Film film = new Film();
        film.setDescription("Test Discription");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1895,12,27));
        Throwable exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Некорректно введена дата релиза", exception.getMessage());
    }

    @Test
    void shouldThrowBadResponseWhenDurationIsNull() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Discription");
        film.setReleaseDate(LocalDate.of(1895,12,27));
        Throwable exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Некорректно введена дата релиза", exception.getMessage());
    }

    @Test
    void shouldThrowBadResponseWhenDescriptionIsNull() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1895,12,27));
        Throwable exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Некорректно введена дата релиза", exception.getMessage());
    }
    @Test
    void shouldThrowBadResponseAllNull() {
        Film film = new Film();
        Throwable exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Некорректно введена дата релиза", exception.getMessage());
    }

    @Test
    void shouldRunCorrectWhenReleaseDateIsRight() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Discription");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1895,12,29));
        filmController.addFilm(film);
        assertEquals(film, inMemoryFilmStorage.findById(1));
    }

    @Test
    void shouldRunCorrectWhenReleaseDateIs12281895() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Discription");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1895,12,28));
        filmController.addFilm(film);
        assertEquals(film, inMemoryFilmStorage.findById(1));
    }

}