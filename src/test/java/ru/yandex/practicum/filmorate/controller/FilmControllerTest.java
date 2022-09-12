package ru.yandex.practicum.filmorate.controller;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    Film film = new Film();
    FilmService filmService;

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
        boolean value = filmService.filmValidation(film);
        assertFalse(value, "Некорректный releaseDate.");
    }

    @Test
    public void shouldReturnFalseWhenIncorrectFilm() {
        film.setName("");
        film.setDescription(Strings.repeat("a", 201));
        film.setDuration(-5);
        film.setReleaseDate(null);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(4, violations.size(), "Аннотации пропускают некорректно созданный film");
    }
}