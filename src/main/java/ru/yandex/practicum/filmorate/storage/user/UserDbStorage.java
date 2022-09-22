package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {

    private int id;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM USERS", new UserMapper());
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.of(jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE ID = ?", new UserMapper(), id));
    }

    @Override
    public Optional<User> addUser(User user) {
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update("INSERT INTO USERS (NAME, LOGIN, EMAIL, BIRTHDATE) VALUES (?, ?, ?, ?)", user.getName(),
                user.getLogin(), user.getEmail(), user.getBirthday());
        user.setId(getCreatedId());
        log.info("Добавлен пользователь {} - {}", user.getId(), user.getLogin());
        return Optional.of(user);

    }

    @Override
    public Optional<User> updateUser(User user) {
        jdbcTemplate.update("UPDATE USERS SET NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDATE = ? WHERE ID = ?",
                user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        log.info("Обновлена информация о пользователе {}", user.getId());
        return Optional.of(user);
    }

    @Override
    public boolean contains(Integer id) {
        return jdbcTemplate.queryForList("SELECT ID FROM USERS", Integer.class).contains(id);
    }

    private Integer getCreatedId() {
        return jdbcTemplate.queryForObject("SELECT ID FROM USERS ORDER BY ID DESC LIMIT 1", Integer.class);
    }

    /*@Override
    public Integer idGenerator() {
        id++;
        return id;
    }*/

}
