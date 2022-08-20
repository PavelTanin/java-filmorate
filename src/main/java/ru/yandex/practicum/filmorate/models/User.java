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
public class User {

    private Integer id;

    @Email(message = "Некорректно указан email")
    private String email;

    private String login;

    private String name;

    @Past(message = "Некорректно указана дата рождения")
    private LocalDate birthday;

    @JsonIgnore
    private Set<Integer> friendList = new HashSet<>();

}
