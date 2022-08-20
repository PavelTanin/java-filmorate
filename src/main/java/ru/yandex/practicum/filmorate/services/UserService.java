package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public String addFriend(Integer id, Integer friendId) {
        if (!inMemoryUserStorage.contains(id) || !inMemoryUserStorage.contains(friendId)) {
            log.info("Не удалось добавить в друзья. Один из пользователей не зарегестрирован");
            throw new ObjectNotFoundException("Нет зарегистрированных пользователей");
        }

        inMemoryUserStorage.findById(id).addFriend(friendId);
        inMemoryUserStorage.findById(friendId).addFriend(id);
        log.info("Пользователи {} и {} добавлены в друзья", inMemoryUserStorage.findById(id).getName(),
                inMemoryUserStorage.findById(friendId).getName());
        return String.format("%s и %s теперь друзья", inMemoryUserStorage.findById(id).getName(),
                inMemoryUserStorage.findById(friendId).getName());
    }

    public String deleteFriend(Integer id, Integer friendId) {
        if (!inMemoryUserStorage.contains(id) || !inMemoryUserStorage.contains(friendId)) {
            log.info("Не удалось удалить из друзей. Один из пользователей не зарегестрирован");
            throw new ObjectNotFoundException("Нет зарегистрированных пользователей");
        }

        inMemoryUserStorage.findById(id).deleteFriend(friendId);
        inMemoryUserStorage.findById(friendId).deleteFriend(id);
        log.info("Пользователи {} и {} больше не друзья", inMemoryUserStorage.findById(id).getName(),
                inMemoryUserStorage.findById(friendId).getName());
        return String.format("%s и %s больше не друзья", inMemoryUserStorage.findById(id).getName(),
                inMemoryUserStorage.findById(friendId).getName());
    }

    public List<User> getFriendList(Integer id) {
        if (!inMemoryUserStorage.contains(id)) {
            log.info("Не удалось получить список друзей. Пользователь не зарегестрирован");
            throw new ObjectNotFoundException("Нет зарегистрированных пользователей");
        }

        List<User> result = new ArrayList<>();
        for (Integer friendId : inMemoryUserStorage.findById(id).getFriendList()) {
            result.add(inMemoryUserStorage.findById(friendId));
        }
        log.info("Возвращен список друзей пользователя {}", inMemoryUserStorage.findById(id).getName());
        return result;
    }

    public List<User> commonFriendsList(Integer id, Integer otherId) {
        if (!inMemoryUserStorage.contains(id) && !inMemoryUserStorage.contains(otherId)) {
            log.info("Не удалось получить список общих друзей. Один из пользователей не зарегестрирован");
            throw new ObjectNotFoundException("Нет зарегистрированных пользователей");
        }

        Set<Integer> firstUserFriendList = inMemoryUserStorage.findById(id).getFriendList();
        Set<Integer> secondUserFriendList = inMemoryUserStorage.findById(otherId).getFriendList();

        if (firstUserFriendList.isEmpty() || secondUserFriendList.isEmpty()) {
            log.info("Нет совпадений в списке друзей");
            return new ArrayList<>();
        }

        List<User> result = new ArrayList<>();
        for (Integer friendId : firstUserFriendList) {
            if (secondUserFriendList.contains(friendId)) {
                result.add(inMemoryUserStorage.findById(friendId));
            }
        }
        log.info("Получен список общих друзей пользователей {} и {}", inMemoryUserStorage.findById(id).getName(),
                inMemoryUserStorage.findById(otherId).getName());
        return result;
    }
}



