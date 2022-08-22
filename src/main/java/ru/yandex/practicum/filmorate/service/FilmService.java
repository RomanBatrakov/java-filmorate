package ru.yandex.practicum.filmorate.service;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Slf4j
@Service
public class FilmService implements FilmStorage {

    InMemoryFilmStorage filmStorage;

    public void addLike(Long id, Long userId) {
        if (idValidation(id) && userId > 0) {
            Film film = filmStorage.getFilms().get(id);
            film.getLikes().add(userId);
            log.debug("Фильму {} поставили лайк.", film);
        } else {
            log.warn("Ошибка при добавлении лайка фильму.");
            throw new ValidationException("Ошибка добавления лайка, проверьте корректность данных.");
        }
    }

    public void deleteLike(Long id, Long userId) {
        if (idValidation(id) && userId > 0) {
            Film film = filmStorage.getFilms().get(id);
            film.getLikes().remove(userId);
            log.debug("Фильму {} удалили лайк.", film);
        } else {
            log.warn("Ошибка при удалении лайка фильму.");
            throw new NotFoundException("Ошибка удаления лайка, проверьте корректность данных.");
        }
    }

    public List<Film> getMostPopularFilms(int count) {
        if (count > 0) {
            return filmStorage.getFilms().values().stream()
                    .sorted((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()))
                    .limit(count)
                    .collect(Collectors.toList());
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
    public Film getFilm(Long id) {
        if (idValidation(id)) {
            return filmStorage.getFilm(id);
        } else {
            log.warn("Ошибка запроса фильма.");
            throw new NotFoundException("Ошибка запроса фильма, проверьте корректность данных.");
        }
    }

    @Override
    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    private boolean idValidation(Long id) {
        return id != null && filmStorage.getFilms().containsKey(id);
    }
}
