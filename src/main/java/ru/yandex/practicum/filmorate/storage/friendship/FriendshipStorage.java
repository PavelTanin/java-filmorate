package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {

    void addFriend(Integer id, Integer friendId);

    void deleteFriend(Integer id, Integer friendId);

    List<User> getFriendList(Integer id);

    List<User> commonFriendsList(Integer id, Integer otherId);

    boolean containsFriend(Integer id, Integer friendId);

}
