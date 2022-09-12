package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserService implements UserStorage {

    UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int id, int friendId) {
        if(id != friendId) {
            userStorage.addFriend(id, friendId);
        } else{
            log.warn("Ошибка при добавлении друга.");
            throw new NotFoundException("Ошибка добавления друга, проверьте корректность данных.");
        }
    }

    public void deleteFriend(int id, int friendId) {
        if (id != friendId) {
            userStorage.deleteFriend(id, friendId);
        } else {
            log.warn("Ошибка при удалении друга.");
            throw new ValidationException("Ошибка удаления друга, проверьте корректность данных.");
        }
    }

    public List<User> getUserFriends(int id) {
        try {
            return userStorage.getUserFriends(id);
        } catch (ValidationException e){
            log.warn("Ошибка при получении списка друзей.");
            throw new ValidationException("Ошибка списка друзей, проверьте корректность данных.");
        }
    }

    public List<User> getCommonFriendList(int id, int friendId) {
        try {
            return userStorage.getCommonFriendList(id, friendId);
        } catch (ValidationException e){
            log.warn("Ошибка при получении списка общих друзей.");
            throw new ValidationException("Ошибка списка общих друзей, проверьте корректность данных.");
        }
    }

    @Override
    public List<User> listUsers() {
        return userStorage.listUsers();
    }


    @Override
    public User getUserById(int id) {
        try {
            return userStorage.getUserById(id);
        } catch (NotFoundException e){
            log.warn("Ошибка запроса пользователя.");
            throw new NotFoundException("Ошибка запроса пользователя, проверьте корректность данных.");
        }
    }

    @Override
    public User createUser(@NonNull User user) {
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(@NonNull User user) {
        return userStorage.updateUser(user);
    }

}
