package com.example.pixels.service;

import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.error.SameDataUpdateExceptionHandler;
import com.example.pixels.model.Movie;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface MovieService {
    public List<Movie> getAllMovies() throws ItemNotFoundException;

    public Movie getMovieById(Long movieId) throws ItemNotFoundException;

    public Movie getMovieByName(String movieName) throws ItemNotFoundException;

    public List<Movie> getMoviesByGenre(String movieGenre) throws ItemNotFoundException;

    public List<Movie> getMoviesByReleaseDate(LocalDate releaseDate) throws ItemNotFoundException;

    public List<Movie> getMoviesByMovieRating(Double movieRating) throws ItemNotFoundException;

    public Movie saveMovie(Movie movie);

    public List<Movie> saveAllMovie(List<Movie> movies);

    public Movie updateMovie(Movie movie, Long movieId) throws ItemNotFoundException, SameDataUpdateExceptionHandler;

    public void deleteMovieById(Long movieId) throws ItemNotFoundException;
}
