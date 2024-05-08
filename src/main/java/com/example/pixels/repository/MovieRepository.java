package com.example.pixels.repository;

import com.example.pixels.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    public Optional<Movie> findByMovieNameIgnoreCase(String movieName);

    Optional<List<Movie>> findAllByMovieGenreIgnoreCaseOrderByMovieName(String movieGenre);

    Optional<List<Movie>> findAllByReleaseDateOrderByMovieName(LocalDate releaseDate);

    Optional<List<Movie>> findByMovieRatingBetweenOrderByMovieName(int lowerRating, int upperRating);
}
