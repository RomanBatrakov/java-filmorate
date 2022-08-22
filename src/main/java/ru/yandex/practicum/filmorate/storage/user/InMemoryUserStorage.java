package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
@Component
public class InMemoryUserStorage implements UserStorage {
    private long generatorId = 0;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> listUsers() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Long id) {
        log.debug("Текущий пользователь {}", users.get(id));
        return users.get(id);
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
            log.debug("Обновлен пользователь: {}", user);
            return user;
        } else {
            log.warn("Ошибка при обновлении пользователя: {}", user);
            throw new NotFoundException("Ошибка обновления пользователя, проверьте корректность данных.");
        }
    }

    public boolean userValidation(User user) {
        boolean userFriends = true;
        for (Long usersId : user.getFriends()) {
            if (!users.containsKey(usersId)) {
                userFriends = false;
                break;
            }
        }
        return !user.getLogin().contains(" ")
                && userFriends;
    }

    private void addNewId(User user) {
        long id = generatorId + 1;
        while (users.containsKey(id)) {
            id += id;
        }
        user.setId(id);
        generatorId = id;
    }
}
