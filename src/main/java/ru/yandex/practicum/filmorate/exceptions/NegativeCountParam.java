package ru.yandex.practicum.filmorate.exceptions;

public class NegativeCountParam extends RuntimeException {

    public NegativeCountParam(String message) {
        super(message);
    }
}
