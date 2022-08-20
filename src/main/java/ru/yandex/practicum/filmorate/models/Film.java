package ru.yandex.practicum.filmorate.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private LocalDate releaseDate;

    @Positive(message = "Некорректно введена продолжительность фильма")
    private Integer duration;

    @JsonIgnore
    private Set<Integer> likes = new HashSet<>();

    private int rate;

}
