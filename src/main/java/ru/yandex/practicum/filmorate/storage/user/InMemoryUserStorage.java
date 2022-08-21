package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private int id;

    private final Map<Integer, User> userList = new HashMap();


    @Override
    public List<User> findAll() {
        log.info("Получен список пользователей");
        return new ArrayList<>(userList.values());
    }

    @Override
    public User findById(Integer id) {
        log.info("Найден пользователь {}", id);
        return userList.get(id);
    }

    @Override
    public User addUser(User user) {
        user.setId(idGenerator());
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userList.put(user.getId(), user);
        log.info("Добавлен пользователь {} - {}", user.getId(), user.getLogin());
        return user;
    }

    @Override
    public User updateUser(User user) {
        var updatedUser = userList.get(user.getId());
        updatedUser.setName(user.getName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setLogin(user.getLogin());
        updatedUser.setBirthday(user.getBirthday());
        log.info("Обновлена информация о пользователе {}", id);
        return userList.get(user.getId());
    }

    @Override
    public boolean contains(Integer id) {
        return userList.containsKey(id);
    }

    @Override
    public Integer idGenerator() {
        id++;
        return id;
    }
}
