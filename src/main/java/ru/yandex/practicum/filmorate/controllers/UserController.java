package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storages.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storages.user.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserStorage userStorage;

    @Autowired
    public UserController(UserService userService, InMemoryUserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll() {
        return userStorage.findAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public User findById(@PathVariable Integer id) {
        return userStorage.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public User addUser(@Valid @RequestBody User user) {
        return userStorage.addUser(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @PutMapping("{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public String addFriend(@PathVariable(value = "id") Integer id,
                            @PathVariable(value = "friendId") Integer friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteFriend(@PathVariable(value = "id") Integer id,
                               @PathVariable(value = "friendId") Integer friendId) {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getFriendList(@PathVariable(value = "id") Integer id) {
        return userService.getFriendList(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> commonFriendsList(@PathVariable(value = "id") Integer id,
                                        @PathVariable(value = "otherId") Integer otherId) {
        return userService.commonFriendsList(id, otherId);
    }


}
