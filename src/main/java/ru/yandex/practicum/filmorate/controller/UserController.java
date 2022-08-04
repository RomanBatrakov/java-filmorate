package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private int generatorId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> listUsers() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (user != null && userValidation(user)) {
            addNewId(user);
            if (user.getName().isBlank())
                user.setName(user.getLogin());
            users.put(user.getId(), user);
            log.debug("Сохранен пользователь: {}", user);
            return user;
        } else {
            log.warn("Ошибка при создании пользователя: {}", user);
            throw new ValidationException("Ошибка создания пользователя, проверьте корректность данных.");
        }
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (user != null && userValidation(user) && users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("Пользователь фильм: {}", user);
            return user;
        } else {
            log.warn("Ошибка при обновлении пользователя: {}", user);
            throw new ValidationException("Ошибка обновления пользователя, проверьте корректность данных.");
        }
    }

    public boolean userValidation(User user) {
        return !user.getLogin().contains(" ");
    }

    private void addNewId(User user) {
        int id = generatorId + 1;
        while (users.containsKey(id)) {
            id += id;
        }
        user.setId(id);
        generatorId = id;
    }
}
