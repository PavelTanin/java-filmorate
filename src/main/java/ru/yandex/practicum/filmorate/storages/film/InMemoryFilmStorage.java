package ru.yandex.practicum.filmorate.storages.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.CustomValidator;
import ru.yandex.practicum.filmorate.models.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private int id;
    private final CustomValidator customValidator;
    private final Map<Integer, Film> filmList = new HashMap<>();

    public InMemoryFilmStorage(CustomValidator customValidator) {
        this.customValidator = customValidator;
    }

    @Override
    public List<Film> findAll() {
        log.info("Получен список фильмов");
        return new ArrayList<>(filmList.values());
    }

    @Override
    public Film findById(Integer id) {
        if (!filmList.containsKey(id)) {
            log.info("Запрашиваемый фильм отсутствует");
            throw new ObjectNotFoundException("Такого фильма нет");
        }

        log.info("Найден пользователь {}", id);
        return filmList.get(id);
    }

    @Override
    public Film addFilm(Film film) {
        if (!customValidator.isValid(film)) {
            log.info("Попытка добавить фильм с некорректной информацией");
            throw new ValidationException("Некорректно заполнено одно из полей");
        }

        film.setId(idGenerator());
        filmList.put(film.getId(), film);
        log.info("Добавлен пользователь {} - {}", id, film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!customValidator.isValid(film)) {
            log.info("Попытка обновить фильм с некорректной информацией");
            throw new ValidationException("Некорректно заполнено одно из полей");
        }

        if (!filmList.containsKey(film.getId())) {
            log.info("Попытка обновить несуществующий фильм");
            throw new ObjectNotFoundException("Попытка обновить несуществующий фильм");
        }

        var updatedUser = filmList.get(film.getId());
        updatedUser.setName(film.getName());
        updatedUser.setDescription(film.getDescription());
        updatedUser.setReleaseDate(film.getReleaseDate());
        updatedUser.setDuration(film.getDuration());
        return film;
    }

    @Override
    public Integer idGenerator() {
        id++;
        return id;
    }

    public boolean contains(Integer id) {
        return filmList.containsKey(id);
    }
}
