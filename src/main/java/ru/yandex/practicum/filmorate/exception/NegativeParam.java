package ru.yandex.practicum.filmorate.exception;

public class NegativeParam extends RuntimeException {

    public NegativeParam(String message) {
        super(message);
    }
}
