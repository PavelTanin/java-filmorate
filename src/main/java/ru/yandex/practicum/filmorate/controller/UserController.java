package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@ResponseBody
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> findById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
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
    public List<Optional<User>> getFriendList(@PathVariable(value = "id") Integer id) {
        return userService.getFriendList(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Optional<User>> commonFriendsList(@PathVariable(value = "id") Integer id,
                                                  @PathVariable(value = "otherId") Integer otherId) {
        return userService.commonFriendsList(id, otherId);
    }


}
