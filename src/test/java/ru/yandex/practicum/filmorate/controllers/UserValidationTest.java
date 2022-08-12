package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserValidationTest {

    UserController userController = new UserController();

    @Test
    void shouldReturnBadRequestResponsWhenLoginContainsSpace() {
        User user = User.builder()
                .name("Test Name")
                .login("testlog in")
                .email("test@test.ru")
                .birthday(LocalDate.of(2005, 12, 5))
                .build();
        ResponseEntity response = userController.addUser(user);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestResponsWhenLoginIsNull() {
        User user = User.builder()
                .name("Test Name")
                .email("test@test.ru")
                .birthday(LocalDate.of(2005, 12, 5))
                .build();
        ResponseEntity response = userController.addUser(user);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestResponsWhenEmailIsNull() {
        User user = User.builder()
                .name("Test Name")
                .login("testlogin")
                .birthday(LocalDate.of(2005, 12, 5))
                .build();
        ResponseEntity response = userController.addUser(user);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestResponsWhenEmailIsEmpty() {
        User user = User.builder()
                .name("Test Name")
                .login("testlogin")
                .email("")
                .birthday(LocalDate.of(2005, 12, 5))
                .build();
        ResponseEntity response = userController.addUser(user);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestResponsWhenBirthdateIsNull() {
        User user = User.builder()
                .name("Test Name")
                .login("testlogin")
                .email("test@test.ru")
                .build();
        ResponseEntity response = userController.addUser(user);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestResponsWhenAllIsNull() {
        User user = User.builder().build();
        ResponseEntity response = userController.addUser(user);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}