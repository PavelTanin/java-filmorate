package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.CustomValidator;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmValidationTest {

    CustomValidator customValidator = new CustomValidator();
    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    JdbcTemplate jdbcTemplate;
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    FilmService filmService = new FilmService(inMemoryFilmStorage, inMemoryUserStorage,  jdbcTemplate, customValidator);
    FilmController filmController = new FilmController(filmService);

    @Test
    void shouldThrowBadResponseWhenReleaseDateIsWrong() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Discription");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1895,12,27));
        film.setMpa(new Mpa(1));
        Throwable exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Некорректно введена дата релиза", exception.getMessage());
    }

    @Test
    void shouldThrowBadResponseWhenReleaseDateIsNull() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Discription");
        film.setDuration(100);
        film.setMpa(new Mpa(1));
        Throwable exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Некорректно введена дата релиза", exception.getMessage());
    }

    @Test
    void shouldThrowBadResponseWhenNameIsNull() {
        Film film = new Film();
        film.setDescription("Test Discription");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1895,12,27));
        film.setMpa(new Mpa(1));
        Throwable exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Некорректно введена дата релиза", exception.getMessage());
    }

    @Test
    void shouldThrowBadResponseWhenDurationIsNull() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Discription");
        film.setReleaseDate(LocalDate.of(1895,12,27));
        film.setMpa(new Mpa(1));
        Throwable exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Некорректно введена дата релиза", exception.getMessage());
    }

    @Test
    void shouldThrowBadResponseWhenDescriptionIsNull() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1895,12,27));
        film.setMpa(new Mpa(1));
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
        film.setMpa(new Mpa(1));
        film.setReleaseDate(LocalDate.of(1895,12,29));
        filmController.addFilm(film);
        assertEquals(Optional.of(film), inMemoryFilmStorage.findById(1));
    }

    @Test
    void shouldRunCorrectWhenReleaseDateIs12281895() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Discription");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1895,12,28));
        film.setMpa(new Mpa(1));
        filmController.addFilm(film);
        assertEquals(Optional.of(film), inMemoryFilmStorage.findById(1));
    }

}