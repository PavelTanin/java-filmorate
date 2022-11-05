package ru.yandex.practicum.filmorate.storage.user;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(Integer id);

    List<User> findAll();

    User findById(Integer id);

    void addFriend(Integer id, Integer friendId);

    void deleteFriend(Integer id, Integer friendId);

    public List<User> getFriendList(Integer id);

    public List<User> commonFriendsList(Integer id, Integer otherId);

    boolean contains(Integer id);

    boolean containsFriend(Integer id, Integer friendId);

}
