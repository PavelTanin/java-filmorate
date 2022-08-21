package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserStorage userStorage;
    private final CustomValidator customValidator;

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(Integer id) {
        if (!userStorage.contains(id)) {
            log.info("Запрашиваемый пользователь не зарегистрирован");
            throw new ObjectNotFoundException("Данный пользователь не зарегестрирован");
        }

        return userStorage.findById(id);
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

    public String addFriend(Integer id, Integer friendId) {
        if (!userStorage.contains(id) || !userStorage.contains(friendId)) {
            log.info("Не удалось добавить в друзья. Один из пользователей не зарегестрирован");
            throw new ObjectNotFoundException("Нет зарегистрированных пользователей");
        }

        userStorage.findById(id).getFriendList().add(friendId);
        userStorage.findById(friendId).getFriendList().add(id);
        log.info("Пользователи {} и {} добавлены в друзья", userStorage.findById(id).getName(),
                userStorage.findById(friendId).getName());
        return String.format("%s и %s теперь друзья", userStorage.findById(id).getName(),
                userStorage.findById(friendId).getName());
    }

    public String deleteFriend(Integer id, Integer friendId) {
        if (!userStorage.contains(id) || !userStorage.contains(friendId)) {
            log.info("Не удалось удалить из друзей. Один из пользователей не зарегестрирован");
            throw new ObjectNotFoundException("Нет зарегистрированных пользователей");
        }

        userStorage.findById(id).getFriendList().remove(friendId);
        userStorage.findById(friendId).getFriendList().remove(id);
        log.info("Пользователи {} и {} больше не друзья", userStorage.findById(id).getName(),
                userStorage.findById(friendId).getName());
        return String.format("%s и %s больше не друзья", userStorage.findById(id).getName(),
                userStorage.findById(friendId).getName());
    }

    public List<User> getFriendList(Integer id) {
        if (!userStorage.contains(id)) {
            log.info("Не удалось получить список друзей. Пользователь не зарегестрирован");
            throw new ObjectNotFoundException("Нет зарегистрированных пользователей");
        }

        List<User> result = new ArrayList<>();
        for (Integer friendId : userStorage.findById(id).getFriendList()) {
            result.add(userStorage.findById(friendId));
        }
        log.info("Возвращен список друзей пользователя {}", userStorage.findById(id).getName());
        return result;
    }

    public List<User> commonFriendsList(Integer id, Integer otherId) {
        if (!userStorage.contains(id) && !userStorage.contains(otherId)) {
            log.info("Не удалось получить список общих друзей. Один из пользователей не зарегестрирован");
            throw new ObjectNotFoundException("Нет зарегистрированных пользователей");
        }

        Set<Integer> firstUserFriendList = userStorage.findById(id).getFriendList();
        Set<Integer> secondUserFriendList = userStorage.findById(otherId).getFriendList();

        if (firstUserFriendList.isEmpty() || secondUserFriendList.isEmpty()) {
            log.info("Нет совпадений в списке друзей");
            return new ArrayList<>();
        }

        List<User> result = new ArrayList<>();
        for (Integer friendId : firstUserFriendList) {
            if (secondUserFriendList.contains(friendId)) {
                result.add(userStorage.findById(friendId));
            }
        }
        log.info("Получен список общих друзей пользователей {} и {}", userStorage.findById(id).getName(),
                userStorage.findById(otherId).getName());
        return result;
    }

}


