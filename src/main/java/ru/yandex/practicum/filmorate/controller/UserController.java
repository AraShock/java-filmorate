package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.service.ValidationService.validationUser;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private int idUser = 1;

    protected int generateIdUser() {
        return idUser++;
    }

    @GetMapping
    public List<User> findALl() {
        List<User> userList = new ArrayList<>(users.values());
        log.info("Количество всех пользователей: {}", userList.size());
        return userList;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (validationUser(user)) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(generateIdUser());
            users.put(user.getId(), user);
            log.info("Добавлен пользователь: {}", user);
            return user;
        }
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        if (users.containsKey(user.getId())) {
            if (validationUser(user)) {
                if (user.getName() == null || user.getName().isBlank()) {
                    user.setName(user.getLogin());
                }
                users.put(user.getId(), user);
                log.info("Изменен пользователь: {}", user);
                return user;
            }
        } else {
            log.warn("невозможно обновить несуществующего пользователя");
            throw new NotFoundException("Ошибка обновления - невозможно обновить несуществующего пользователя");
        }
        return user;
    }

}
