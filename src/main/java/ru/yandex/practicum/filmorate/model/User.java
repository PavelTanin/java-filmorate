package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {

    private Integer id;

    @Email(message = "Некорректно указан email")
    private String email;

    @NotNull(message = "Некорректно указан логин")
    @NotBlank(message = "Некорректно указан логин")
    private String login;

    private String name;

    @Past
    private LocalDate birthday;
}
