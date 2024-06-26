package com.example.pixels.entity;

import com.example.pixels.dto.IdOnlySerializer;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import com.fasterxml.jackson.databind.JsonSerializer;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Movie name is missing.")
    private String movieName;

    private LocalDate releaseDate;

    private String movieGenre;

    @NotNull(message = "Movie Runtime is missing.")
    private Integer movieRuntime;

    @Range(min = 0, max = 5, message = "Movie rating must be between 0 and 5.")
    private Double movieRating;

    private String movieImageUrl;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL
            , fetch = FetchType.LAZY)
    @JsonSerialize(contentUsing = IdOnlySerializer.class)
//    @ToString.Exclude
    private List<Review> reviews;

}
