package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        user.setId(idGenerator());
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userList.put(user.getId(), user);
        log.info("Добавлен пользователь: {}", user);
        return new ResponseEntity<>(user, HttpStatus.valueOf(201));
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
            return new ResponseEntity<>(user, HttpStatus.valueOf(200));
        } else {
            log.info("Ошибка при обновлении пользователя: {}", user);
            return new ResponseEntity<>(HttpStatus.valueOf(404));
        }
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
