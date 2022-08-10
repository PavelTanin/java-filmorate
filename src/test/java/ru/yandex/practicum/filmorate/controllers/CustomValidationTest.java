package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

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
        assertEquals("<500 INTERNAL_SERVER_ERROR Internal Server Error,[]>", String.valueOf(filmController.addFilm(film)));
    }

    @Test
    void shouldRunCorrectWhenReleaseDateIsRight() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Discription")
                .duration(100)
                .releaseDate(LocalDate.of(1895,12,29))
                .build();
        assertEquals("<201 CREATED Created,Film(id=1, name=Test Film, description=Test Discription, " +
                "releaseDate=1895-12-29, duration=100),[]>", String.valueOf(filmController.addFilm(film)));
    }

    @Test
    void shouldRunCorrectWhenReleaseDateIs12281895() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Discription")
                .duration(100)
                .releaseDate(LocalDate.of(1895,12,28))
                .build();
        assertEquals("<201 CREATED Created,Film(id=1, name=Test Film, description=Test Discription, " +
                "releaseDate=1895-12-28, duration=100),[]>", String.valueOf(filmController.addFilm(film)));
    }

}