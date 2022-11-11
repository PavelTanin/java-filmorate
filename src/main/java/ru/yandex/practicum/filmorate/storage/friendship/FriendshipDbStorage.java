package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Integer id, Integer friendId) {
        jdbcTemplate.update("INSERT INTO FRIENDSHIP VALUES (?, ?)", id, friendId);
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        log.info("Пользователь {} удален", id);
        jdbcTemplate.update("DELETE FROM FRIENDSHIP WHERE USER_ID = ? AND FRIENDED_USER = ?", id, friendId);
    }

    @Override
    public List<User> getFriendList(Integer id) {
        return jdbcTemplate.query("SELECT u.* FROM FRIENDSHIP as f " +
                "INNER JOIN USERS AS u ON f.FRIENDED_USER = u.ID " +
                "WHERE f.USER_ID = ?", new UserMapper(), id);
    }

    @Override
    public List<User> commonFriendsList(Integer id, Integer otherId) {
        return jdbcTemplate.query("SELECT u.* FROM FRIENDSHIP as f " +
                "INNER JOIN USERS AS u ON f.FRIENDED_USER = u.ID " +
                "WHERE f.USER_ID = ? " +
                "INTERSECT " +
                "SELECT u.* FROM FRIENDSHIP as f " +
                "INNER JOIN USERS AS u ON f.FRIENDED_USER = u.ID " +
                "WHERE f.USER_ID = ?", new UserMapper(), id, otherId);
    }

    @Override
    public boolean containsFriend(Integer id, Integer friendId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FRIENDSHIP WHERE USER_ID = ? AND FRIENDED_USER = ?",
                Integer.class, id, friendId) > 0;
    }
}
