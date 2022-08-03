package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

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
        boolean value2 = controller.userValidation(user);
        assertFalse(value2, "Некорректный login - есть пробелы.");
    }
}