package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private int id;
    private final Map<Integer, Film> filmList = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        film.setId(idGenerator());
        filmList.put(film.getId(), film);
        log.info("Добавлен фильм {} - {}", id, film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        var updatedUser = filmList.get(film.getId());
        updatedUser.setName(film.getName());
        updatedUser.setDescription(film.getDescription());
        updatedUser.setReleaseDate(film.getReleaseDate());
        updatedUser.setDuration(film.getDuration());
        return film;
    }

    @Override
    public void deleteFilm(Integer id) {
        log.info("Фильм {} удален", id);
        filmList.remove(id);
    }

    @Override
    public List<Film> findAll() {
        log.info("Получен список фильмов");
        return new ArrayList<>(filmList.values());
    }

    @Override
    public Film findById(Integer id) {
        log.info("Найден фильм {}", id);
        return filmList.get(id);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return new ArrayList<>(filmList.values()).stream()
                .sorted((Film o1, Film o2) -> o1.getRate() - o2.getRate())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public boolean contains(Integer id) {
        return filmList.containsKey(id);
    }


    public Integer idGenerator() {
        id++;
        return id;
    }
}
