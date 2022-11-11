package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFriendshipStorage implements FriendshipStorage {

    InMemoryUserStorage inMemoryUserStorage;

    private final Map<Integer, List<Integer>> friendshipList = new HashMap<>();

    @Override
    public void addFriend(Integer id, Integer friendId) {
        if (friendshipList.containsKey(id)) {
            friendshipList.get(id).add(friendId);
        } else {
            friendshipList.put(id, new ArrayList<>(friendId));
        }
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        friendshipList.get(id).remove(friendId);
    }

    @Override
    public List<User> getFriendList(Integer id) {
        return friendshipList.get(id).stream()
                .map(inMemoryUserStorage::findById)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> commonFriendsList(Integer id, Integer otherId) {
        return getFriendList(id).stream()
                .filter(o -> getFriendList(otherId).contains(o))
                .collect(Collectors.toList());
    }

    @Override
    public boolean containsFriend(Integer id, Integer friendId) {
        return friendshipList.get(id).contains(friendId);
    }

}
