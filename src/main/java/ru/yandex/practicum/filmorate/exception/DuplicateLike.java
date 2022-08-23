package ru.yandex.practicum.filmorate.exception;

public class DuplicateLike extends RuntimeException {

    public DuplicateLike(String message) {
        super(message);
    }
}
