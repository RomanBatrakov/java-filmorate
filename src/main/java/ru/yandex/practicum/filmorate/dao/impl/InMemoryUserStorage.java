package ru.yandex.practicum.filmorate.dao.impl;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Data
@Component
public class InMemoryUserStorage implements UserStorage {
    private int usersIdCount = 0;
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<User, HashSet<Integer>> friends = new HashMap<>();

    @Override
    public List<User> listUsers() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int id) {
        if (users.containsKey(id)) {
            log.debug("Текущий пользователь {}", users.get(id));
            return users.get(id);
        } else {
            log.warn("Ошибка запроса пользователя.");
            throw new NotFoundException("Ошибка запроса пользователя, проверьте корректность данных.");
        }
    }

    @Override
    public User createUser(@NonNull User user) {
        if (userValidation(user)) {
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
    public User updateUser(@NonNull User user) {
        if (userValidation(user) && users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("Обновлен пользователь: {}", user);
            return user;
        } else {
            log.warn("Ошибка при обновлении пользователя: {}", user);
            throw new NotFoundException("Ошибка обновления пользователя, проверьте корректность данных.");
        }
    }

    @Override
    public void addFriend(int id, int friendId) {
        if (users.containsKey(id) && users.containsKey(friendId)) {
            User user1 = users.get(id);
            User user2 = users.get(friendId);
            friends.get(user1).add(friendId);
            friends.get(user2).add(id);
            log.debug("Теперь {} и {} друзья.", user1, user2);
        } else {
            log.warn("Ошибка при добавлении друга.");
            throw new NotFoundException("Ошибка добавления друга, проверьте корректность данных.");
        }
    }

    public void deleteFriend(int id, int friendId) {
        if (users.containsKey(id) && users.containsKey(friendId)) {
            User user1 = users.get(id);
            User user2 = users.get(friendId);
            friends.get(user1).remove(friendId);
            friends.get(user2).remove(id);
            log.debug("Теперь {} и {} не друзья.", user1, user2);
        } else {
            log.warn("Ошибка при удалении друга.");
            throw new ValidationException("Ошибка удаления друга, проверьте корректность данных.");
        }
    }

    @Override
    public List<User> getUserFriends(int id) {
        List<User> userFriends = new ArrayList<>();
        if (users.containsKey(id)) {
            User user = users.get(id);
            friends.get(user);
            List<Integer> friendsId = new ArrayList<>(friends.get(user));
            for (Integer userId: friendsId) {
                userFriends.add(users.get(userId));
            }
            return userFriends;
        } else {
            log.warn("Ошибка при получении списка друзей.");
            throw new ValidationException("Ошибка списка друзей, проверьте корректность данных.");
        }
    }

    @Override
    public List<User> getCommonFriendList(int id, int friendId) {
        List<User> commonFriendList = new ArrayList<>();
        if (users.containsKey(id) && users.containsKey(friendId)) {
            User user1 = users.get(id);
            User user2 = users.get(friendId);
            for (int friendsId : friends.get(user2)) {
                if (friends.get(user1).contains(friendsId)) {
                    commonFriendList.add(users.get(friendsId));
                }
            }
            return commonFriendList;
        } else {
            log.warn("Ошибка при получении списка общих друзей.");
            throw new ValidationException("Ошибка списка общих друзей, проверьте корректность данных.");
        }
    }

    public boolean userValidation(User user) {
        return !user.getLogin().contains(" ");
    }

    private void addNewId(User user) {
        int id = usersIdCount + 1;
        while (users.containsKey(id)) {
            id += id;
        }
        user.setId(id);
        usersIdCount = id;
    }
}
