package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicateLike;
import ru.yandex.practicum.filmorate.exception.NegativeCountParam;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private final CustomValidator customValidator;

    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage, CustomValidator customValidator) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.customValidator = customValidator;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(Integer id) {
        if (!filmStorage.contains(id)) {
            log.info("Запрашиваемый фильм отсутствует");
            throw new ObjectNotFoundException("Фильм не найден");
        }

        return filmStorage.findById(id);
    }

    public Film addFilm(Film film) {
        if (!customValidator.isValid(film)) {
            log.info("Попытка добавить фильм с некорректной информацией");
            throw new ValidationException("Некорректно заполнено одно из полей");
        }

        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        if (!customValidator.isValid(film)) {
            log.info("Попытка обновить фильм с некорректной информацией");
            throw new ValidationException("Некорректно заполнено одно из полей");
        }

        if (!filmStorage.contains(film.getId())) {
            log.info("Попытка обновить несуществующий фильм");
            throw new ObjectNotFoundException("Фильм не найден");
        }

        return filmStorage.updateFilm(film);
    }

    public String addLike(Integer id, Integer userId) {
        if (!filmStorage.contains(id) && !userStorage.contains(userId)) {
            log.info("Попытка добавить лайк несуществующиму фильму или несуществующим пользователем");
            throw new ObjectNotFoundException("Нет такого фильма или пользователя");
        }
        if (filmStorage.findById(id).getLikes().contains(userId)) {
            log.info("Попытка повторно поставить лайк. Фильм {} - Пользователь {}", filmStorage.findById(id).getName(),
                    userStorage.findById(userId).getId());
            throw new DuplicateLike("Пользователь уже оценил выбранный фильм");
        }

        log.info("Поставлен лайк");
        filmStorage.findById(id).getLikes().add(userId);
        filmStorage.findById(id).setRate(filmStorage.findById(id).getRate() + 1);
        return String.format("Пользователю %s понравился фильм %s", userStorage.findById(userId).getLogin(),
                filmStorage.findById(id).getName());
    }

    public String deleteLike(Integer id, Integer userId) {
        if (!filmStorage.contains(id) || !userStorage.contains(userId)) {
            log.info("Попытка удалить лайк несуществующиму фильму или несуществующим пользователем");
            throw new ObjectNotFoundException("Нет такого фильма или пользователя");
        }
        if (!filmStorage.findById(id).getLikes().contains(userId)) {
            log.info("Попытка повторно удалить лайк, который не был поставлен. Фильм {} - Пользователь {}",
                    filmStorage.findById(id).getName(),
                    userStorage.findById(userId).getId());
            throw new DuplicateLike("Пользователь еще не оценил выбранный фильм");
        }

        filmStorage.findById(id).getLikes().add(userId);
        filmStorage.findById(id).setRate(filmStorage.findById(id).getRate() - 1);
        return String.format("Пользователю %s больше не нравится фильм %s", userStorage.findById(userId).getLogin(),
                filmStorage.findById(id).getName());
    }

    public List<Film> getPopularFilms(Integer count) {
        if (count <= 0) {
            throw new NegativeCountParam("Длинна списка не может быть отрицательной или ровняться нулю");
        }
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
