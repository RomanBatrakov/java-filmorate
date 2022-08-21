package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService implements UserStorage {

    InMemoryUserStorage userStorage = new InMemoryUserStorage();

    public void addFriend(String id, String friendId) {
        if (idValidation(id) && idValidation(friendId)) {
            User user1 = userStorage.getUsers().get(Long.parseLong(id));
            User user2 = userStorage.getUsers().get(Long.parseLong(friendId));
            user1.getFriends().add(Long.parseLong(friendId));
            user2.getFriends().add(Long.parseLong(id));
            log.debug("Теперь {} и {} друзья.", user1, user2);
        } else {
            log.warn("Ошибка при добавлении друга.");
            throw new ValidationException("Ошибка добавления друга, проверьте корректность данных.");
        }
    }

    public void deleteFriend(String id, String friendId) {
        if (idValidation(id) && idValidation(friendId)) {
            User user1 = userStorage.getUsers().get(Long.parseLong(id));
            User user2 = userStorage.getUsers().get(Long.parseLong(friendId));
            user1.getFriends().remove(Long.parseLong(friendId));
            user2.getFriends().remove(Long.parseLong(id));
            log.debug("Теперь {} и {} друзья.", user1, user2);
        } else {
            log.warn("Ошибка при удалении друга.");
            throw new ValidationException("Ошибка удаления друга, проверьте корректность данных.");
        }
    }
    public List<User> getUserFriends(String id) {
        List<User> userFriends = new ArrayList<>();
        if (idValidation(id)) {
            User user = userStorage.getUsers().get(Long.parseLong(id));
            for (Long friendId : user.getFriends()) {
                userFriends.add(userStorage.getUsers().get(friendId));
            }
            return userFriends;
        } else {
            log.warn("Ошибка при получении списка друзей.");
            throw new ValidationException("Ошибка списка друзей, проверьте корректность данных.");
        }
    }

    @Override
    public List<User> listUsers() {
        return userStorage.listUsers();
    }

    @Override
    public User getUser(String id) {
        if (idValidation(id)) {
            return userStorage.getUser(id);
        } else {
            log.warn("Ошибка апроса пользователя.");
            throw new ValidationException("Ошибка запроса пользователя, проверьте корректность данных.");
        }
    }

    @Override
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }
//TODO: end here
    public List<User> getCommonFriendList(String id, String friendId) {
        return null;
    }

    private boolean idValidation (String id) {
        return id != null && userStorage.getUsers().containsKey(Long.parseLong(id));
    }
}
