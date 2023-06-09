package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public void delete(Film film) {
        filmStorage.delete(film);
    }

    public Film get(int id) {
        return filmStorage.get(id);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public void likeFilm(int filmId, int userId) {
        Film film = get(filmId);
        User user = userStorage.get(userId);
        filmStorage.likeFilm(film, user);
    }

    public void dislikeFilm(int filmId, int userId) {
        Film film = get(filmId);
        User user = userStorage.get(userId);
        filmStorage.dislikeFilm(film, user);
    }

    // вывод 10 наиболее популярных фильмов по количеству лайков
    public List<Film> getPopularFilms(int count) {
        List<Film> films = filmStorage.findAll();
        return films.stream().sorted(Comparator.comparingInt(f -> -f.getLikes().size())).limit(count)
                .collect(Collectors.toList());
    }

    public Genre getGenre(int id) {
        return filmStorage.getGenre(id);
    }

    public List<Genre> findAllGenres() {
        return filmStorage.findAllGenres();
    }

    public Mpa getMpa(int id) {
        return filmStorage.getMpa(id);
    }

    public List<Mpa> findAllMpa() {
        return filmStorage.findAllMpa();
    }
}
