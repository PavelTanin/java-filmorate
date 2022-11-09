package ru.yandex.practicum.filmorate.storage.likes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class LikesDbStorage implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Integer id, Integer userId) {
        jdbcTemplate.update("INSERT INTO LIKES VALUES (?, ?)", id, userId);
        jdbcTemplate.update("UPDATE FILMS SET RATE = (SELECT RATE " +
                "FROM FILMS WHERE ID = ?) + 1 WHERE ID = ?", id, id);
    }

    @Override
    public void deleteLike(Integer id, Integer userId) {
        jdbcTemplate.update("DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?", id, userId);
        jdbcTemplate.update("UPDATE FILMS SET RATE = (SELECT RATE " +
                "FROM FILMS WHERE ID = ?) - 1 WHERE ID = ?", id, id);
    }

    @Override
    public boolean containsLike(Integer id, Integer userId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?",
                Integer.class, id, userId) > 0;
    }
}
