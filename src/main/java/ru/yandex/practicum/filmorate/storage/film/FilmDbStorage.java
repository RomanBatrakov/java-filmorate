package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> listFilms() {
        return jdbcTemplate.query(
                "select * from FILMS",
                new BeanPropertyRowMapper<>(Film.class)
        );
    }

    @Override
    public Film getFilmById(int id) {
        // выполняем запрос к базе данных.
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from FILMS where FILM_ID = ?", id);
        if (userRows.next()) {
            log.info("Найден пользователь: {} {}", userRows.getString("film_id"), userRows.getString("name"));
            // вы заполните данные пользователя в следующем уроке
            Film film = new Film();
            film.setId(userRows.getInt("film_id"));
            film.setName(userRows.getString("name"));
            film.setDescription(userRows.getString("description"));
            film.setReleaseDate(userRows.getDate("release_date").toLocalDate());
            film.setDuration(userRows.getInt("duration"));
            film.setRate(userRows.getInt("rating_id"));
            return film;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Ошибка запроса фильма, проверьте корректность данных.");
        }
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "insert into films (name, description, release_date, duration, rating_id) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            return stmt;
        }, keyHolder);
        film.setId((Integer) keyHolder.getKey());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return null;
    }
}
