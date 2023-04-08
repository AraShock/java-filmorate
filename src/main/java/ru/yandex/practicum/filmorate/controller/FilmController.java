package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.ValidationService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    public Map<Integer, Film> films = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private int idFilm = 1;
    public static final LocalDate DATE_OF_FIRST_FILM = LocalDate.of(1895, 12, 28);
    public static final int LENGTH_OF_DESCRIPTION = 200;

    protected int generateIdFilm() {
        return idFilm++;
    }

    @GetMapping
    public List<Film> findAll() {
        List<Film> filmList = new ArrayList<>(films.values());
        log.info("Количество всех фильмов: {}", filmList.size());
        return filmList;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (ValidationService.validationFilm(film)) {
            film.setId(generateIdFilm());
            films.put(film.getId(), film);
            log.info("Добавлен фильм: {}", film);
            return film;
        }
        return film;
    }

    @PutMapping

    public Film put(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            if (ValidationService.validationFilm(film)) {
                films.put(film.getId(), film);
                log.info("Изменен фильм: {}", film);
                return film;
            }
        } else {
            log.warn("невозможно обновить несуществующий фильм");
            throw new NotFoundException("Ошибка обновления - невозможно обновить несуществующий фильм");
        }
        return film;
    }
}
