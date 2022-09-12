package ru.yandex.practicum.filmorate.dao.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
@Slf4j
@Data
@Component
public class MpaRatingDaoImpl implements MpaRatingDao {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public List<MpaRating> getMpaRatings() {
        String sqlQuery = "SELECT * FROM rating";
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(MpaRating.class));
    }

    @Override
    public MpaRating getMpaRatingById(int id) {
        String sqlQuery = "SELECT * FROM rating WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, new BeanPropertyRowMapper<>(MpaRating.class), id);
    }
}
