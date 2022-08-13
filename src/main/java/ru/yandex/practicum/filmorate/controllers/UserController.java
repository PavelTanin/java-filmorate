package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@ResponseBody
@RequestMapping("/users")
public class UserController {

    private int id;

    private final HashMap<Integer, User> userList = new HashMap<>();

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(userList.values());

    }

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        if (loginAndEmailValid(user)) {
            user.setId(idGenerator());
            if (user.getName().isEmpty() || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            userList.put(user.getId(), user);
            log.info("Добавлен пользователь: {}", user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        if (user.getId() > 0) {
            if (userList.containsKey(user.getId())) {
                userUpdater(user);
                log.info("Пользователь {} обновлен: {}", user.getName(), user);
            } else {
                addUser(user);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            log.info("Ошибка при обновлении пользователя: {}", user);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private boolean loginAndEmailValid(User user) {
        try {
            if (user.getLogin() == null || user.getLogin().contains(" ")) {
                throw new ValidationException("Некорректно указан логин");
            } else if (user.getEmail() == null || user.getEmail().isBlank()) {
                throw new ValidationException("Некорректно азан email");
            } else if (user.getBirthday() == null) {
                throw new ValidationException("Некорректно указана дата рождения");
            } else {
                return true;
            }
        } catch (ValidationException e) {
            System.out.printf(e.getMessage());
        }
        return false;
    }

    private void userUpdater(User user) {
        var updatedUser = userList.get(user.getId());
        updatedUser.setName(user.getName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setLogin(user.getLogin());
        updatedUser.setBirthday(user.getBirthday());
    }

    private int idGenerator() {
        id++;
        return id;
    }
}
