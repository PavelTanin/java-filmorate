
package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FilmValidationTest {

    private final FilmController filmController;


    @Test
    void shouldThrowBadResponseWhenReleaseDateIsWrong() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Discription");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1895,12,27));
        film.setMpa(new Mpa(1, "TestMPA"));
        Throwable exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Некорректно введена дата релиза", exception.getMessage());
    }

    @Test
    void shouldThrowBadResponseWhenReleaseDateIsNull() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Discription");
        film.setDuration(100);
        film.setMpa(new Mpa(1, "TestMPA"));
        Throwable exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Некорректно введена дата релиза", exception.getMessage());
    }

    @Test
    void shouldThrowBadResponseWhenNameIsNull() {
        Film film = new Film();
        film.setDescription("Test Discription");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1895,12,27));
        film.setMpa(new Mpa(1, "TestMPA"));
        Throwable exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Некорректно введена дата релиза", exception.getMessage());
    }

    @Test
    void shouldThrowBadResponseWhenDurationIsNull() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Discription");
        film.setReleaseDate(LocalDate.of(1895,12,27));
        film.setMpa(new Mpa(1, "TestMPA"));
        Throwable exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Некорректно введена дата релиза", exception.getMessage());
    }

    @Test
    void shouldThrowBadResponseWhenDescriptionIsNull() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1895,12,27));
        film.setMpa(new Mpa(1, "TestMPA"));
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
        film.setRate(4);
        film.setMpa(new Mpa(1, "TestMPA"));
        assertEquals(film.getName(), filmController.addFilm(film).getName());
    }

    @Test
    void shouldRunCorrectWhenReleaseDateIs12281895() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Discription");
        film.setRate(4);
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1985,12,28));
        film.setMpa(new Mpa(1, null));
        assertEquals(film.getName(), filmController.addFilm(film).getName());
    }

}
