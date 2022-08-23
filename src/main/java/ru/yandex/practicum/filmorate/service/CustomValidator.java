package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;

@Component
public class CustomValidator {

    public boolean isValid(User user) {
        if (user.getLogin() == null && user.getEmail() == null && user.getBirthday() == null) {
            throw new ValidationException("Не указана информация о пользователе");
        } else if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            throw new ValidationException("Некорректно указан логин");
        } else if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Некорректно указан email");
        } else if (user.getBirthday() == null) {
            throw new ValidationException("Некорректно указана дата рождения");
        } else {
            return true;
        }
    }

    public boolean isValid(Film film) {
        if (film.getReleaseDate() == null ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Некорректно введена дата релиза");
        } else if (film.getName() == null) {
            throw new ValidationException("Некорректно введено название фильма");
        } else if (film.getDescription() == null) {
            throw new ValidationException("Некорректно введено описание фильма");
        } else if (film.getDuration() == null) {
            throw new ValidationException("Некорректно введена продолжительность фильма");
        } else {
            return true;
        }
    }

}
