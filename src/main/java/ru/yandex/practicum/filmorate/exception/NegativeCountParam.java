package ru.yandex.practicum.filmorate.exception;

public class NegativeCountParam extends RuntimeException {

    public NegativeCountParam(String message) {
        super(message);
    }
}
