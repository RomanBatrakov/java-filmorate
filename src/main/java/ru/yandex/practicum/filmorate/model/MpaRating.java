package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class MpaRating {

    private int id;
    @JsonProperty("name")
    private String title;
}
