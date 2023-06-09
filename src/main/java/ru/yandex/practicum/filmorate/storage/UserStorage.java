package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User update(User user);

    void delete(User user);

    List<User> findAll();

    User get(int id);

    void addFriend(int userId, int friendId);

    void removeFriend(int userId, int friendId);

    List<User> getCommonFriends(int userId, int otherId);

    List<User> getUserFriends(int userId);
}
