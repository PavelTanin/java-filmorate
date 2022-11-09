package ru.yandex.practicum.filmorate.storage.likes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryLikesStorage implements LikesStorage {

    InMemoryFilmStorage inMemoryFilmStorage;

    private final Map<Integer, List<Integer>> likeList = new HashMap<>();

    @Override
    public void addLike(Integer id, Integer userId) {
        if (likeList.containsKey(id)) {
            likeList.get(id).add(userId);
            inMemoryFilmStorage.findById(id).setRate(inMemoryFilmStorage.findById(id).getRate() + 1);
        } else {
            likeList.put(id, new ArrayList<>(userId));
            inMemoryFilmStorage.findById(id).setRate(inMemoryFilmStorage.findById(id).getRate() + 1);
        }
    }

    @Override
    public void deleteLike(Integer id, Integer userId) {
        likeList.get(id).remove(userId);
        inMemoryFilmStorage.findById(id).setRate(inMemoryFilmStorage.findById(id).getRate() - 1);
    }

    @Override
    public boolean containsLike(Integer id, Integer userId) {
        return likeList.get(id).contains(userId);
    }
}
