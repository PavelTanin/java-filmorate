package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.CustomValidator;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserStorage userStorage;
    private final CustomValidator customValidator;

    private final JdbcTemplate jdbcTemplate;

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public Optional<User> findById(Integer id) {
        if (!userStorage.contains(id)) {
            log.info("Запрашиваемый пользователь не зарегистрирован");
            throw new ObjectNotFoundException("Данный пользователь не зарегестрирован");
        }

        return userStorage.findById(id);
    }

    public Optional<User> addUser(User user) {
        if (!customValidator.isValid(user)) {
            log.info("Попытка добавить пользователя с некорректной информацией");
            throw new ValidationException("Некорректно заполнено одно из полей");
        }

        return userStorage.addUser(user);

    }

    public Optional<User> updateUser(User user) {
        if (!customValidator.isValid(user)) {
            log.info("Попытка обновить пользователя с некорректной информацией");
            throw new ValidationException("Некорректно заполнено одно из полей");
        }

        if (!userStorage.contains(user.getId())) {
            log.info("Попытка обновить незарегестрированного пользователя");
            throw new ObjectNotFoundException("Попытка обновить незарегестрированного пользователя");
        }

        return userStorage.updateUser(user);
    }

    public String addFriend(Integer id, Integer friendId) {
        if (!userStorage.contains(id) || !userStorage.contains(friendId)) {
            log.info("Не удалось добавить в друзья. Один из пользователей не зарегестрирован");
            throw new ObjectNotFoundException("Один из пользователей не зарегестрирован");
        }
        jdbcTemplate.update("INSERT INTO FRIENDSHIP VALUES (?, ?)", id, friendId);
        log.info("Пользователь {} добавил в друзья пользователя {}", id, friendId);
        return String.format("Пользователь %s добавлен в список друзей", friendId);
    }

    public String deleteFriend(Integer id, Integer friendId) {
        if (!userStorage.contains(id) || !userStorage.contains(friendId)) {
            log.info("Не удалось удалить из друзей. Один из пользователей не зарегестрирован");
            throw new ObjectNotFoundException("Один из пользователей не зарегестрирован");
        }
        jdbcTemplate.update("DELETE FROM FRIENDSHIP WHERE USER_ID = ? AND FRIENDED_USER = ?", id, friendId);
        log.info("Пользователь {} удалил из друзей пользователя {}", id, friendId);
        return String.format("Пользователь %s удален из списка друзей", friendId);
    }

    public List<Optional<User>> getFriendList(Integer id) {
        if (!userStorage.contains(id)) {
            log.info("Не удалось получить список друзей. Пользователь не зарегестрирован");
            throw new ObjectNotFoundException("Один из пользователей не зарегестрирован");
        }
        log.info("Возвращен список друзей пользователя {}", id);
        List<Integer> friendsId = jdbcTemplate.queryForList(
                "SELECT FRIENDED_USER FROM FRIENDSHIP WHERE USER_ID = ?",
                Integer.class, id);
        return friendsId.stream()
                .map(userStorage::findById)
                .collect(Collectors.toList());
    }

    public List<Optional<User>> commonFriendsList(Integer id, Integer otherId) {
        if (!userStorage.contains(id) && !userStorage.contains(otherId)) {
            log.info("Не удалось получить список общих друзей. Один из пользователей не зарегестрирован");
            throw new ObjectNotFoundException("Один из пользователей не зарегестрирован");
        }
        List<Integer> commonFriendsId = jdbcTemplate.queryForList("SELECT FRIENDED_USER FROM FRIENDSHIP WHERE USER_ID=?",
                        Integer.class, id).stream()
                .filter(o -> jdbcTemplate.queryForList("SELECT FRIENDED_USER FROM FRIENDSHIP WHERE USER_ID=?",
                        Integer.class, otherId).contains(o))
                .collect(Collectors.toList());
        log.info("Получен список общих друзей пользователей {} и {}", id, otherId);
        return commonFriendsId.stream()
                .map(userStorage::findById)
                .collect(Collectors.toList());
    }
}






