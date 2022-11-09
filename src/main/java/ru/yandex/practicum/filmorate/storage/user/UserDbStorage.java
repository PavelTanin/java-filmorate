package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        String query = "INSERT INTO USERS (NAME, LOGIN, EMAIL, BIRTHDATE) VALUES (?, ?, ?, ?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getEmail());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, kh);
        user.setId((Integer) kh.getKey());
        log.info("Добавлен пользователь {}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update("UPDATE USERS SET NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDATE = ? WHERE ID = ?",
                user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        log.info("Обновлена информация о пользователе {}", user.getId());
        return user;
    }

    @Override
    public void deleteUser(Integer id) {
        jdbcTemplate.update("DELETE FROM USERS WHERE ID = ?", id);
        log.info("Пользователь {} удален", id);
    }

    @Override
    public List<User> findAll() {
        log.info("Получен список всех пользователей");
        return jdbcTemplate.query("SELECT * FROM USERS", new UserMapper());
    }

    @Override
    public User findById(Integer id) {
        log.info("Получена информация о пользователе {}", id);
        return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE ID = ?", new UserMapper(), id);
    }

    @Override
    public boolean contains(Integer id) {
        return jdbcTemplate.queryForObject("SELECT COUNT(ID) FROM USERS WHERE ID = ?", Integer.class, id) > 0;
    }


}
