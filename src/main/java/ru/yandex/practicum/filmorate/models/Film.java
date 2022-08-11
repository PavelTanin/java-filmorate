package ru.yandex.practicum.filmorate.models;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class Film {


    private Integer id;

    @NotNull(message = "Название фильма не указано")
    @NotBlank(message = "Название фильма не указано")
    private String name;

    @Size(min = 0, max = 200, message = "Слишком длинное описание фильма")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма указана некорректно")
    private Integer duration;

}
