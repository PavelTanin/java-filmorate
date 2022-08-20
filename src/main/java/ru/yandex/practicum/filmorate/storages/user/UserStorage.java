package ru.yandex.practicum.filmorate.storages.user;


import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

public interface UserStorage {

    List<User> findAll();

    User findById(Integer id);

    User addUser(User user);

    User updateUser(User user);

    boolean contains(Integer id);

    Integer idGenerator();

}
