package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService implements UserStorage {

    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long id, Long friendId) {
        if (idValidation(id) && idValidation(friendId)) {
            User user1 = userStorage.getUsers().get(id);
            User user2 = userStorage.getUsers().get(friendId);
            user1.getFriends().add(friendId);
            user2.getFriends().add(id);
            log.debug("Теперь {} и {} друзья.", user1, user2);
        } else {
            log.warn("Ошибка при добавлении друга.");
            throw new NotFoundException("Ошибка добавления друга, проверьте корректность данных.");
        }
    }

    public void deleteFriend(Long id, Long friendId) {
        if (idValidation(id) && idValidation(friendId)) {
            User user1 = userStorage.getUsers().get(id);
            User user2 = userStorage.getUsers().get(friendId);
            user1.getFriends().remove(friendId);
            user2.getFriends().remove(id);
            log.debug("Теперь {} и {} друзья.", user1, user2);
        } else {
            log.warn("Ошибка при удалении друга.");
            throw new ValidationException("Ошибка удаления друга, проверьте корректность данных.");
        }
    }

    public List<User> getUserFriends(Long id) {
        List<User> userFriends = new ArrayList<>();
        if (idValidation(id)) {
            User user = userStorage.getUsers().get(id);
            for (Long friendId : user.getFriends()) {
                userFriends.add(userStorage.getUsers().get(friendId));
            }
            return userFriends;
        } else {
            log.warn("Ошибка при получении списка друзей.");
            throw new ValidationException("Ошибка списка друзей, проверьте корректность данных.");
        }
    }

    public List<User> getCommonFriendList(Long id, Long friendId) {
        List<User> commonFriendList = new ArrayList<>();
        if (idValidation(id) && idValidation(friendId)) {
            User user1 = userStorage.getUsers().get(id);
            User user2 = userStorage.getUsers().get(friendId);
            for (Long friendsId : user2.getFriends()) {
                if (user1.getFriends().contains(friendsId)) {
                    commonFriendList.add(userStorage.getUsers().get(friendsId));
                }
            }
            return commonFriendList;
        } else {
            log.warn("Ошибка при получении списка общих друзей.");
            throw new ValidationException("Ошибка списка общих друзей, проверьте корректность данных.");
        }
    }

    @Override
    public List<User> listUsers() {
        return userStorage.listUsers();
    }

    @Override
    public User getUser(Long id) {
        if (idValidation(id)) {
            return userStorage.getUser(id);
        } else {
            log.warn("Ошибка запроса пользователя.");
            throw new NotFoundException("Ошибка запроса пользователя, проверьте корректность данных.");
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

    private boolean idValidation(Long id) {
        return id != null && userStorage.getUsers().containsKey(id);
    }
}
