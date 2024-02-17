package com.example.pixels.repository;

import com.example.pixels.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    public Movie findByMovieNameIgnoreCase(String movieName);

    List<Movie> findAllByMovieGenreIgnoreCaseOrderByMovieName(String movieGenre);

    List<Movie> findAllByReleaseDateOrderByMovieName(LocalDate releaseDate);

    List<Movie> findByMovieRatingBetweenOrderByMovieName(int lowerRating, int upperRating);
}
