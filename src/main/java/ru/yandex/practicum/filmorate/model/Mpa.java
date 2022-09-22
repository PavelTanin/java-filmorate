package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Mpa {

    public Mpa(int id) {
        this.id = id;
    }

    private int id;

    private String name;

}