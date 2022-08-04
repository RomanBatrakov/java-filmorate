package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    User user = new User();
    UserController controller = new UserController();

    @BeforeEach
    void createUser() {
        user.setId(1);
        user.setEmail("user@yandex.ru");
        user.setLogin("user");
        user.setName("user");
        user.setBirthday(LocalDate.of(2000, 1, 1));
    }

    @Test
    public void shouldReturnFalseWhenIncorrectLogin() {
        user.setLogin("user user");
        boolean value = controller.userValidation(user);
        assertFalse(value, "Некорректный login - есть пробелы.");
    }

    @Test
    public void shouldReturnFalseWhenIncorrectUser() {
        user.setEmail("useruser");
        user.setLogin("");
        user.setBirthday(LocalDate.of(2025, 2, 3));
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(3, violations.size(), "Аннотации пропускают некорректно созданного user");
    }
}