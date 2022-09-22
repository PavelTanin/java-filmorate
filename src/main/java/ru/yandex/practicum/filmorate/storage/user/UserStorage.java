package ru.yandex.practicum.filmorate.storage.user;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<User> findAll();

    Optional<User> findById(Integer id);

    Optional<User> addUser(User user);

    Optional<User> updateUser(User user);

    boolean contains(Integer id);

    //Integer idGenerator();
}
