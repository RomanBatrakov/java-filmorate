package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

@Slf4j
@Data
@Service
public class MpaRatingService {
    MpaRatingDao mpaRatingDao;

    public List<MpaRating> getMpaRatings() {
        try {
            return mpaRatingDao.getMpaRatings();
        } catch (NotFoundException e) {
            log.warn("Ошибка запроса списка рейтингов.");
            throw new ValidationException("Ошибка запроса списка рейтингов, проверьте корректность данных.");
        }
    }

    public MpaRating getMpaRatingById(int id) {
        try {
            return mpaRatingDao.getMpaRatingById(id);
        } catch (NotFoundException e) {
            log.warn("Ошибка запроса рейтинга.");
            throw new ValidationException("Ошибка запроса рейтинга, проверьте корректность данных.");
        }
    }
}
