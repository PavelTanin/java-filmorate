package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.CustomValidator;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserValidationTest {

    CustomValidator customValidator = new CustomValidator();
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    UserService userService = new UserService(inMemoryUserStorage, customValidator);
    UserController userController = new UserController(userService);

    @Test
    void shouldReturnBadRequestResponsWhenLoginContainsSpace() {
        User user = new User();
        user.setName("Test Name");
        user.setLogin("testlog in");
        user.setEmail("test@test.ru");
        user.setBirthday(LocalDate.of(2005, 12, 5));
        Throwable exception = assertThrows(ValidationException.class, () -> userController.addUser(user));
        assertEquals("Некорректно указан логин", exception.getMessage());
    }

    @Test
    void shouldReturnBadRequestResponsWhenLoginIsNull() {
        User user = new User();
        user.setName("Test Name");
        user.setEmail("test@test.ru");
        user.setBirthday(LocalDate.of(2005, 12, 5));
        Throwable exception = assertThrows(ValidationException.class, () -> userController.addUser(user));
        assertEquals("Некорректно указан логин", exception.getMessage());
    }

    @Test
    void shouldReturnBadRequestResponsWhenEmailIsNull() {
        User user = new User();
        user.setName("Test Name");
        user.setLogin("testlogin");
        user.setBirthday(LocalDate.of(2005, 12, 5));
        Throwable exception = assertThrows(ValidationException.class, () -> userController.addUser(user));
        assertEquals("Некорректно указан email", exception.getMessage());
    }

    @Test
    void shouldReturnBadRequestResponsWhenEmailIsEmpty() {
        User user = new User();
        user.setName("Test Name");
        user.setLogin("testlogin");
        user.setEmail("");
        user.setBirthday(LocalDate.of(2005, 12, 5));
        Throwable exception = assertThrows(ValidationException.class, () -> userController.addUser(user));
        assertEquals("Некорректно указан email", exception.getMessage());
    }

    @Test
    void shouldReturnBadRequestResponsWhenBirthdateIsNull() {
        User user = new User();
        user.setName("Test Name");
        user.setLogin("testlogin");
        user.setEmail("test@test.ru");
        Throwable exception = assertThrows(ValidationException.class, () -> userController.addUser(user));
        assertEquals("Некорректно указана дата рождения", exception.getMessage());
    }

    @Test
    void shouldReturnBadRequestResponsWhenAllIsNull() {
        User user = new User();
        Throwable exception = assertThrows(ValidationException.class, () -> userController.addUser(user));
        assertEquals("Не указана информация о пользователе", exception.getMessage());
    }

}