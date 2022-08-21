package ru.yandex.practicum.filmorate.storage.user;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> findAll();

    User findById(Integer id);

    User addUser(User user);

    User updateUser(User user);

    boolean contains(Integer id);

    Integer idGenerator();

}
