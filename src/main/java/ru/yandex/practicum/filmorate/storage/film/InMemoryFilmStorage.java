package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int generatorId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getFilms() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        if (film != null && filmValidation(film)) {
            addNewId(film);
            films.put(film.getId(), film);
            log.debug("Сохранен фильм: {}", film);
            return film;
        } else {
            log.warn("Ошибка при создании фильма: {}", film);
            throw new ValidationException("Ошибка создания фильма, проверьте корректность данных.");
        }
    }

    @Override
    public Film updateFilm(Film film) {
        if (film != null && filmValidation(film) && films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Обновлен фильм: {}", film);
            return film;
        } else {
            log.warn("Ошибка при обновлении фильма: {}", film);
            throw new ValidationException("Ошибка изменения фильма, проверьте корректность данных.");
        }
    }

    public boolean filmValidation(Film film) {
        LocalDate cinemaBirthday = LocalDate.of(1895, 12, 28);
        return !film.getReleaseDate().isBefore(cinemaBirthday);
    }

    private void addNewId(Film film) {
        int id = generatorId + 1;
        while (films.containsKey(id)) {
            id += id;
        }
        film.setId(id);
        generatorId = id;
    }
}
