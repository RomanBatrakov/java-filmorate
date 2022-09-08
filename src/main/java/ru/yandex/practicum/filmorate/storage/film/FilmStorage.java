package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {
    List<Film> listFilms();

    Film getFilmById(int id);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Map<Integer, Film> getFilms();
}
