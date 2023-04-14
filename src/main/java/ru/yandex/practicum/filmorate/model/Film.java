package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class Film {
    private int id;
    @NonNull
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    private String description;
    private int duration;
    private int rate;
    @Builder.Default
    private Set<Integer> likes = new HashSet<>();
}
