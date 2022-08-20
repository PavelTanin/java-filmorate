package ru.yandex.practicum.filmorate.storages.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.CustomValidator;
import ru.yandex.practicum.filmorate.models.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private int id;

    private final CustomValidator customValidator;
    private final Map<Integer, User> userList = new HashMap();

    public InMemoryUserStorage(CustomValidator customValidator) {
        this.customValidator = customValidator;
    }

    @Override
    public List<User> findAll() {
        log.info("Получен список пользователей");
        return new ArrayList<>(userList.values());
    }

    @Override
    public User findById(Integer id) {
        if (!userList.containsKey(id)) {
            log.info("Запрашиваемый пользователь не зарегистрирован");
            throw new ObjectNotFoundException("Данный пользователь не зарегестрирован");
        }

        log.info("Найден пользователь {}", id);
        return userList.get(id);
    }

    @Override
    public User addUser(User user) {
        if (!customValidator.isValid(user)) {
            log.info("Попытка добавить пользователя с некорректной информацией");
            throw new ValidationException("Некорректно заполнено одно из полей");
        }

        user.setId(idGenerator());
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        userList.put(user.getId(), user);
        log.info("Добавлен пользователь {} - {}", id, user.getLogin());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!customValidator.isValid(user)) {
            log.info("Попытка обновить пользователя с некорректной информацией");
            throw new ValidationException("Некорректно заполнено одно из полей");
        }

        if (!userList.containsKey(user.getId())) {
            log.info("Попытка обновить незарегестрированного пользователя");
            throw new ObjectNotFoundException("Попытка обновить незарегестрированного пользователя");
        }

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
