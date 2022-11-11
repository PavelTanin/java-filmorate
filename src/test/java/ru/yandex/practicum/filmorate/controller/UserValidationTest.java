package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserValidationTest {

    private final UserController userController;

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