package ru.yandex.practicum.filmorate.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.DublicateLike;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storages.film.FilmStorage;
import ru.yandex.practicum.filmorate.storages.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storages.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storages.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public String addLike(Integer id, Integer userId) {
        if (!filmStorage.contains(id) && !userStorage.contains(userId)) {
            log.info("Попытка добавить лайк несуществующиму фильму или несуществующим пользователем");
            throw new ObjectNotFoundException("Нет такого фильма или пользователя");
        }
        if (filmStorage.findById(id).getLikes().contains(userId)) {
            log.info("Попытка повторно поставить лайк. Фильм {} - Пользователь {}", filmStorage.findById(id).getName(),
                    userStorage.findById(userId).getId());
            throw new DublicateLike("Пользователь уже оценил выбранный фильм");
        }

        log.info("Поставлен лайк");
        filmStorage.findById(id).getLikes().add(userId);
        filmStorage.findById(id).setRate(filmStorage.findById(id).getRate() + 1);
        return String.format("Пользователю %s понравился фильм %s", userStorage.findById(userId).getLogin(),
                filmStorage.findById(id).getName());
    }

    public String deleteLike(Integer id, Integer userId) {
        if (!filmStorage.contains(id) && !userStorage.contains(userId)) {
            log.info("Попытка удалить лайк несуществующиму фильму или несуществующим пользователем");
            throw new ObjectNotFoundException("Нет такого фильма или пользователя");
        }
        if (!filmStorage.findById(id).getLikes().contains(userId)) {
            log.info("Попытка повторно удалить лайк, который не был поставлен. Фильм {} - Пользователь {}",
                    filmStorage.findById(id).getName(),
                    userStorage.findById(userId).getId());
            throw new DublicateLike("Пользователь еще не оценил выбранный фильм");
        }

        filmStorage.findById(id).getLikes().add(userId);
        filmStorage.findById(id).setRate(filmStorage.findById(id).getRate() - 1);
        return String.format("Пользователю %s больше не нравится фильм %s", userStorage.findById(userId).getLogin(),
                filmStorage.findById(id).getName());
    }

    public List<Film> getPopularFilms(Integer count) {
        log.info("Получен список %d популярных фильмов");
        return filmStorage.findAll().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film film1, Film film2) {
        return  -1 * film1.getRate() - film2.getRate();
    }
}
