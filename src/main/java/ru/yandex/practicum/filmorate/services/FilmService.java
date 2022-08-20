package ru.yandex.practicum.filmorate.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.DublicateLike;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storages.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storages.user.InMemoryUserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public String addLike(Integer id, Integer userId) {
        if (!inMemoryFilmStorage.contains(id) && !inMemoryUserStorage.contains(userId)) {
            log.info("Попытка добавить лайк несуществующиму фильму или несуществующим пользователем");
            throw new ObjectNotFoundException("Нет такого фильма или пользователя");
        }
        if (inMemoryFilmStorage.findById(id).getLikes().contains(userId)) {
            log.info("Попытка повторно поставить лайк. Фильм {} - Пользователь {}", inMemoryFilmStorage.findById(id).getName(),
                    inMemoryUserStorage.findById(userId).getId());
            throw new DublicateLike("Пользователь уже оценил выбранный фильм");
        }

        log.info("Поставлен лайк");
        inMemoryFilmStorage.findById(id).getLikes().add(userId);
        inMemoryFilmStorage.findById(id).setRate(inMemoryFilmStorage.findById(id).getRate() + 1);
        return String.format("Пользователю %s понравился фильм %s", inMemoryUserStorage.findById(userId).getLogin(),
                inMemoryFilmStorage.findById(id).getName());
    }

    public String deleteLike(Integer id, Integer userId) {
        if (!inMemoryFilmStorage.contains(id) && !inMemoryUserStorage.contains(userId)) {
            log.info("Попытка удалить лайк несуществующиму фильму или несуществующим пользователем");
            throw new ObjectNotFoundException("Нет такого фильма или пользователя");
        }
        if (!inMemoryFilmStorage.findById(id).getLikes().contains(userId)) {
            log.info("Попытка повторно удалить лайк, который не был поставлен. Фильм {} - Пользователь {}",
                    inMemoryFilmStorage.findById(id).getName(),
                    inMemoryUserStorage.findById(userId).getId());
            throw new DublicateLike("Пользователь еще не оценил выбранный фильм");
        }

        inMemoryFilmStorage.findById(id).getLikes().add(userId);
        inMemoryFilmStorage.findById(id).setRate(inMemoryFilmStorage.findById(id).getRate() - 1);
        return String.format("Пользователю %s больше не нравится фильм %s", inMemoryUserStorage.findById(userId).getLogin(),
                inMemoryFilmStorage.findById(id).getName());
    }

    public List<Film> getPopularFilms(Integer count) {
        log.info("Получен список %d популярных фильмов");
        return inMemoryFilmStorage.findAll().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film film1, Film film2) {
        return  -1 * film1.getRate() - film2.getRate();
    }
}
