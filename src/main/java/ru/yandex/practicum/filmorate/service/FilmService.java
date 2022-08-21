package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Slf4j
@Service
public class FilmService implements FilmStorage {
    public void addLike() {

    }
    public void deleteLike() {

    }

    @Override
    public List<Film> getFilms() {
        return null;
    }

    @Override
    public Film getFilm(long id) {
        return null;
    }

    @Override
    public Film createFilm(Film film) {
        return null;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }
    @Override
    public List<Film> getMostPopularFilms() {
        return null;
    }
}
