package ru.yandex.practicum.filmorate.storage.likes;

public interface LikesStorage {

    void addLike(Integer id, Integer userId);

    void deleteLike(Integer id, Integer userId);

    boolean containsLike(Integer id, Integer userId);

}
