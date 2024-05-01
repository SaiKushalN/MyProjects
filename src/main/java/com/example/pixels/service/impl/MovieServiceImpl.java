package com.example.pixels.service.impl;

import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.error.SameDataUpdateExceptionHandler;
import com.example.pixels.entity.Movie;
import com.example.pixels.model.MovieModel;
import com.example.pixels.repository.MovieRepository;
import com.example.pixels.service.MovieService;
import com.example.pixels.service.ReviewService;
import com.example.pixels.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@Component
@Transactional
public class MovieServiceImpl implements MovieService {
    @Autowired
    MovieRepository movieRepository;

    //TestWritten
    @Override
    public List<Movie> getAllMovies() {
        Optional<List<Movie>> movies = Optional.of(movieRepository.findAll());
        if(movies.get().isEmpty()){
            throw new NoSuchElementException("No Movies Available.");
        }
//        movies.get().forEach(movie -> {reviewService.sumOfRatings(movie.getId());});
        return movies.get();
    }

    @Override
    public Movie getMovieById(Long movieId) {
        Optional<Movie> movie = movieRepository.findById(movieId);
        if(movie.isEmpty()){
            throw new NoSuchElementException("No Movies Available.");
        }
        return movie.get();
    }

    //TestWritten
    @Override
    public Movie getMovieByName(String movieName) {
        Optional<Movie> movie = Optional.ofNullable(movieRepository.findByMovieNameIgnoreCase(movieName));

        if(movie.isEmpty()){
            throw new NoSuchElementException("Movie with name "+ movieName+" Not Available.");
        }
        return movie.get();
    }

    //TestWritten
    @Override
    public List<Movie> getMoviesByGenre(String movieGenre) {
        Optional<List<Movie>> movies = Optional.of(movieRepository.findAllByMovieGenreIgnoreCaseOrderByMovieName(movieGenre));
        if(movies.get().isEmpty()){
            throw new NoSuchElementException("No Movies Available.");
        }
        return movies.get();
    }

    //TestWritten
    @Override
    public List<Movie> getMoviesByReleaseDate(LocalDate releaseDate) {
        Optional<List<Movie>> movies = Optional.of(movieRepository.findAllByReleaseDateOrderByMovieName(releaseDate));
        if(movies.get().isEmpty()){
            throw new NoSuchElementException("No Movies Available.");
        }
        return movies.get();
    }

    //TestWritten
    @Override
    public List<Movie> getMoviesByMovieRating(Double movieRating) {
        int lowerRating = movieRating.intValue();
        int upperRating = lowerRating + 1;
        Optional<List<Movie>> movies = Optional.of(movieRepository.findByMovieRatingBetweenOrderByMovieName(lowerRating, upperRating));
        if(movies.get().isEmpty()){
            throw new NoSuchElementException("No Movies Available.");
        }
        return movies.get();
    }

    @Override
    public Movie saveMovie(MovieModel movieModel)  {

        Movie movie = new Movie();
        movie.setMovieName(movieModel.getMovieName());
        movie.setMovieGenre(movieModel.getMovieGenre());
        movie.setMovieRuntime(movieModel.getMovieRuntime());
        movie.setMovieImageUrl(movieModel.getMovieImageUrl());
        movie.setReleaseDate(movieModel.getReleaseDate());
        return movieRepository.save(movie);
    }

    @Override
    public Movie saveMovieEntity(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public List<Movie> saveAllMovie(List<Movie> movies) {
        return movieRepository.saveAll(movies);
    }

    @Override
    public Movie updateMovie(MovieModel movieModel, Long movieId) {
        Optional<Movie> movieFromDb = movieRepository.findById(movieId);

        if(movieFromDb.isEmpty())
            throw new NoSuchElementException("Movie with Id "+movieId+" Not Found");

        Movie movieGetFromDb = movieFromDb.get();

        if(Objects.nonNull(movieModel.getMovieGenre()) &&
                !"".equalsIgnoreCase(movieModel.getMovieGenre())) {
            movieGetFromDb.setMovieGenre(movieModel.getMovieGenre());
        }
        if(Objects.nonNull(movieModel.getMovieName()) &&
                !"".equalsIgnoreCase(movieModel.getMovieName())) {
            movieGetFromDb.setMovieName(movieModel.getMovieName());
        }
        if (Objects.nonNull(movieModel.getMovieRuntime())) {
            movieGetFromDb.setMovieRuntime(movieModel.getMovieRuntime());
        }
        if(Objects.nonNull(movieModel.getReleaseDate()) &&
                !"".equalsIgnoreCase(String.valueOf(movieModel.getReleaseDate()))) {
            movieGetFromDb.setReleaseDate(movieModel.getReleaseDate());
        }
        if (movieModel.getMovieImageUrl() != null && !movieModel.getMovieImageUrl().isEmpty()) {
            movieGetFromDb.setMovieImageUrl(movieModel.getMovieImageUrl());
        }
        return movieRepository.save(movieGetFromDb);
    }

    @Override
    public void deleteMovieById(Long movieId) {
        Optional<Movie> movieFromDb = movieRepository.findById(movieId);
        if(movieFromDb.isEmpty())
            throw new NoSuchElementException("No Movies Available.");

        movieRepository.deleteById(movieId);
    }


}
