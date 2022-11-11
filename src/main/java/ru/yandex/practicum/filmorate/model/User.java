package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class User {

    private Integer id;

    private String name;

    private String login;

    @Email(message = "Некорректно указан email")
    private String email;

    @Past(message = "Некорректно указана дата рождения")
    private LocalDate birthday;

}
