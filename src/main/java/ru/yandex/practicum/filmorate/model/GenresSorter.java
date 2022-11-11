package ru.yandex.practicum.filmorate.model;

import java.util.Comparator;

public class GenresSorter implements Comparator<Genre> {

    @Override
    public int compare(Genre o1, Genre o2) {
        return o1.getId() - o2.getId();
    }
}
