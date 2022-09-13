package ru.yandex.practicum.filmorate.dao.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.UserService;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Data
@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private UserService userService;

    @Override
    public List<Film> listFilms() {
        String sqlQuery = "SELECT * FROM films";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film getFilmById(int id) {
        try {
            String sqlQuery = "SELECT * FROM films WHERE film_id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Ошибка запроса фильма, проверьте корректность данных.");
        }
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, release_date, duration, rating_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId((Integer) keyHolder.getKey());
        String genreSqlQuery = "INSERT INTO film_genres (film_id, id) VALUES (?, ?)";
        film.getGenres().forEach(x -> jdbcTemplate.update(genreSqlQuery, film.getId(), x.getId()));
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlDeleteFilmGenre = "DELETE FROM film_genres WHERE film_id = ?";
        String sqlQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, " +
                "rating_id = ? WHERE film_id = ?";
        String genreSqlQuery = "INSERT INTO film_genres (film_id, id) VALUES (?, ?)";
        jdbcTemplate.update(sqlDeleteFilmGenre, film.getId());
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(),
                Date.valueOf(film.getReleaseDate()), film.getDuration(), film.getMpa().getId(), film.getId());
        film.getGenres().forEach(x -> jdbcTemplate.update(genreSqlQuery, film.getId(), x.getId()));
        return film;
    }

    @Override
    public void addLike(int id, int userId) {
        if (getFilmById(id).getId() == id && userService.getUserById(userId).getId() == userId) {
            String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlQuery, id, userId);
        } else {
            log.warn("Ошибка при добавлении лайка фильму.");
            throw new NotFoundException("Ошибка добавления лайка, проверьте корректность данных.");
        }
    }

    @Override
    public void deleteLike(int id, int userId) {
        if (getFilmById(id).getId() == id && userService.getUserById(userId).getId() == userId) {
            String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
            jdbcTemplate.update(sqlQuery, id, userId);
        } else {
            log.warn("Ошибка при добавлении лайка фильму.");
            throw new NotFoundException("Ошибка добавления лайка, проверьте корректность данных.");
        }
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        String sqlQuery = "SELECT * FROM films f LEFT JOIN (SELECT film_id, COUNT(*) likes_count" +
                " FROM likes GROUP BY film_id) l ON f.film_id = l.film_id ORDER BY l.likes_count DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) {
        try {
            Film film = new Film();
            film.setId(resultSet.getInt("film_id"));
            film.setName(resultSet.getString("name"));
            film.setDescription(resultSet.getString("description"));
            film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
            film.setDuration(resultSet.getInt("duration"));
            int mpaRating = (resultSet.getInt("rating_id"));
            MpaRating mpa = jdbcTemplate.queryForObject("SELECT * FROM rating WHERE id = ?", new BeanPropertyRowMapper<>(MpaRating.class), mpaRating);
            film.setMpa(mpa);
            film.setGenres(getFilmGenres(film.getId()));
            return film;
        } catch (EmptyResultDataAccessException | SQLException e) {
            throw new NotFoundException("Ошибка запроса, проверьте корректность данных.");
        }
    }

    private List<Genre> getFilmGenres(int id) {
        String sqlQuery = "SELECT f.id, name FROM film_genres f" +
                " LEFT JOIN (SELECT * FROM genres) g ON f.id = g.id WHERE film_id = ?";
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(Genre.class), id);
    }
}
