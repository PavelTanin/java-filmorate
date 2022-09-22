package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Film {

    private Integer id;

    @NotEmpty(message = "Некорректно введено название фильма")
    private String name;

    @Size(max = 200, message = "Некорректно введено описание фильма")
    private String description;

    @Positive(message = "Некорректно введена продолжительность фильма")
    private Integer duration;

    private LocalDate releaseDate;

    private Integer rate;

    private Mpa mpa;

    private Set<Genre> genres = new HashSet<>();

}
