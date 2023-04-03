package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.controller.FilmController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    @NotNull
    private int id;
    @NotBlank
    private String name;
    @Size(min = 1, max = FilmController.LENGTH_OF_DESCRIPTION)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    private long duration;
}