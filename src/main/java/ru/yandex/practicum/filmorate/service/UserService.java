package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.CustomValidator;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserStorage userStorage;

    private final FriendshipStorage friendshipStorage;

    private final CustomValidator customValidator;

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User addUser(User user) {
        if (!customValidator.isValid(user)) {
            log.info("Попытка добавить пользователя с некорректной информацией");
            throw new ValidationException("Некорректно заполнено одно из полей");
        }

        return userStorage.addUser(user);

    }

    public User updateUser(User user) {
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

    public String deleteUser(Integer id) {
        userStorage.deleteUser(id);
        return "Пользователь" + id + "удален";
    }

    public User findById(Integer id) {
        if (!userStorage.contains(id)) {
            log.info("Запрашиваемый пользователь не зарегистрирован");
            throw new ObjectNotFoundException("Данный пользователь не зарегестрирован");
        }
        return userStorage.findById(id);
    }

    public String addFriend(Integer id, Integer friendId) {
        if (!userStorage.contains(id) || !userStorage.contains(friendId) || friendshipStorage.containsFriend(id, friendId)) {
            log.info("Не удалось добавить в друзья. Один из пользователей не зарегестрирован или пользователи уже дружат");
            throw new ObjectNotFoundException("Один из пользователей не зарегестрирован или они уже дружат");
        }
        friendshipStorage.addFriend(id, friendId);
        log.info("Пользователь {} добавил в друзья пользователя {}", id, friendId);
        return String.format("Пользователь %s добавлен в список друзей", friendId);
    }

    public String deleteFriend(Integer id, Integer friendId) {
        if (!userStorage.contains(id) || !userStorage.contains(friendId) || !friendshipStorage.containsFriend(id, friendId)) {
            log.info("Не удалось удалить из друзей. Один из пользователей не зарегестрирован или пользователи не дружат");
            throw new ObjectNotFoundException("Один из пользователей не зарегестрирован или пользователи уже не дружат");
        }
        friendshipStorage.deleteFriend(id, friendId);
        log.info("Пользователь {} удалил из друзей пользователя {}", id, friendId);
        return String.format("Пользователь %s удален из списка друзей", friendId);
    }

    public List<User> getFriendList(Integer id) {
        if (!userStorage.contains(id)) {
            log.info("Не удалось получить список друзей. Пользователь не зарегестрирован");
            throw new ObjectNotFoundException("Один из пользователей не зарегестрирован");
        }
        log.info("Возвращен список друзей пользователя {}", id);
        return friendshipStorage.getFriendList(id);
    }

    public List<User> commonFriendsList(Integer id, Integer otherId) {
        if (!userStorage.contains(id) && !userStorage.contains(otherId)) {
            log.info("Не удалось получить список общих друзей. Один из пользователей не зарегестрирован");
            throw new ObjectNotFoundException("Один из пользователей не зарегестрирован");
        }
        log.info("Получен список общих друзей пользователей {} и {}", id, otherId);
        return friendshipStorage.commonFriendsList(id, otherId);
    }
}






