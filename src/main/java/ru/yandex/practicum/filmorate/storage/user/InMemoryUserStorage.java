package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private int generatorId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> listUsers() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
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

    @Override
    public User updateUser(User user) {
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
