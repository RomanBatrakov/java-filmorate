package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    List<User> listUsers();

    User getUser(Long id);

    User createUser(User user);

    User updateUser(User user);

    Map<Long, User> getUsers();
}
