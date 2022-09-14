package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class FilmService implements FilmStorage {
    private final FilmStorage filmStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int id, int userId) {
        filmStorage.addLike(id, userId);
    }

    public void deleteLike(int id, int userId) {
        filmStorage.deleteLike(id, userId);
    }

    public List<Film> getMostPopularFilms(int count) {
        if (count > 0) {
            return filmStorage.getMostPopularFilms(count);
        } else {
            log.warn("Ошибка запроса списка популярных фильмов.");
            throw new ValidationException("Ошибка запроса списка популярных фильмов, проверьте корректность данных.");
        }
    }

    @Override
    public List<Film> listFilms() {
        return filmStorage.listFilms();
    }

    @Override
    public Film getFilmById(int id) {
        try {
            return filmStorage.getFilmById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Ошибка запроса фильма.");
            throw new NotFoundException("Ошибка запроса фильма, проверьте корректность данных.");
        }
    }

    @Override
    public Film createFilm(@Valid Film film) {
        if (filmValidation(film)) {
            filmStorage.createFilm(film);
            log.debug("Сохранен фильм: {}", film);
            return film;
        } else {
            log.warn("Ошибка при создании фильма: {}", film);
            throw new ValidationException("Ошибка создания фильма, проверьте корректность данных.");
        }
    }

    @Override
    public Film updateFilm(@NonNull Film film) {
        if (getFilmById(film.getId()).getId() == film.getId() && filmValidation(film)) {
            filmStorage.updateFilm(film);
            log.debug("Обновлен фильм: {}", film);
            return film;
        } else {
            log.warn("Ошибка при обновлении фильма: {}", film);
            throw new NotFoundException("Ошибка изменения фильма, проверьте корректность данных.");
        }
    }

    public boolean filmValidation(@NonNull Film film) {
        LocalDate cinemaBirthday = LocalDate.of(1895, 12, 28);
        return !film.getReleaseDate().isBefore(cinemaBirthday);
    }
}
