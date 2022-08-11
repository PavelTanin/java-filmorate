package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.models.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomValidationTest {

    FilmController filmController = new FilmController();

    @Test
    void shouldThrowExceptionWhenReleaseDateIsWrong() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Discription")
                .duration(100)
                .releaseDate(LocalDate.of(1895,12,27))
                .build();
        ResponseEntity<Film> response = filmController.addFilm(film);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldRunCorrectWhenReleaseDateIsRight() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Discription")
                .duration(100)
                .releaseDate(LocalDate.of(1895,12,29))
                .build();
        ResponseEntity<Film> response = filmController.addFilm(film);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void shouldRunCorrectWhenReleaseDateIs12281895() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Discription")
                .duration(100)
                .releaseDate(LocalDate.of(1895,12,28))
                .build();
        ResponseEntity<Film> response = filmController.addFilm(film);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

}