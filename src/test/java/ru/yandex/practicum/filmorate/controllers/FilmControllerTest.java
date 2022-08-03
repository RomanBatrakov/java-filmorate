package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    Film film = new Film();
    FilmController controller = new FilmController();

    @BeforeEach
    void createFilm() {
        film.setId(1);
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100);
    }

    @Test
    public void shouldReturnFalseWhenIncorrectReleaseDate() {
        film.setReleaseDate(LocalDate.of(1000, 1, 1));
        boolean value = controller.filmValidation(film);
        assertFalse(value, "Некорректный releaseDate.");
    }
}