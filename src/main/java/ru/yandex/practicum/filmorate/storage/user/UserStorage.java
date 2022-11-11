package ru.yandex.practicum.filmorate.storage.user;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(Integer id);

    List<User> findAll();

    User findById(Integer id);

    boolean contains(Integer id);

}
