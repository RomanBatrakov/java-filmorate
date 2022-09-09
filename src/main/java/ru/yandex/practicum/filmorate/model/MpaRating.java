package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MpaRating {

    @JsonProperty("id")
    private int id;
    private String name;
}
