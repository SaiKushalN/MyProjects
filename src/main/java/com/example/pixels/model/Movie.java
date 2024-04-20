package com.example.pixels.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    @NotBlank(message = "Movie name is missing.")
    private String movieName;

    private LocalDate releaseDate;

    private String movieGenre;

    @NotNull(message = "Movie Runtime is missing.")
    private Integer movieRuntime;

    @Range(min = 0, max = 5, message = "Movie rating must be between 0 and 5.")
    private Double movieRating;

    private String movieImageUrl;

}
