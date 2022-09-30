package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.dao.GenreDao;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class GenreService {

    private final GenreDao genreDAO;

    public List<Genre> getGenres() {
        return genreDAO.getGenres();
    }

    public Genre getGenreById(int id) {
        try {
            return genreDAO.getGenreById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Ошибка запроса жанра.");
            throw new NotFoundException("Ошибка запроса жанра, проверьте корректность данных.");
        }
    }
}



