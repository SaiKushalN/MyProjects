package com.example.pixels.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieModel {
    private String movieName;
    private String movieGenre;
    private LocalDate releaseDate;
    private Integer movieRuntime;
    private String movieImageUrl;
}
