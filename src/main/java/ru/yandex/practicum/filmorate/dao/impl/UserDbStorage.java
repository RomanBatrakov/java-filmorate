package ru.yandex.practicum.filmorate.dao.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Data
@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> listUsers() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User getUserById(int id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public User createUser(User user) {
        String sqlQuery = "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId((Integer) keyHolder.getKey());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(),
                user.getName(), Date.valueOf(user.getBirthday()), user.getId());
        return user;
    }

    /**
     * Тут возможно потребуется поменять местами @param id и@param friendId.
     */
    @Override
    public void addFriend(int id, int friendId) {
        String sqlQuery = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        String sqlQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public List<User> getUserFriends(int id) {
        String sqlQuery = "SELECT * FROM users u JOIN (SELECT friend_id" +
                " FROM friends WHERE user_id = ?) l ON u.user_id = l.friend_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    /**
     * Наподумать
     * @param id
     * @param friendId
     * @return
     */
    @Override
    public List<User> getCommonFriendList(int id, int friendId) {
        String sqlQuery = "SELECT * FROM users u JOIN (SELECT friend_id" +
                " FROM friends WHERE user_id = ?) l ON u.user_id = l.friend_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("user_id"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        return user;
    }
}
