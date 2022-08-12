package ru.yandex.practicum.filmorate.models;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class Film {


    private Integer id;

    @NotNull(message = "Некорректно введено название фильма")
    private String name;

    @Size(min = 0, max = 200, message = "Некорректно введено описание фильма")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Некорректно введена продолжительность фильма")
    private Integer duration;

}
