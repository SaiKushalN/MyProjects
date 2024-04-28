package com.example.pixels.service.impl;

import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.error.SameDataUpdateExceptionHandler;
import com.example.pixels.entity.Movie;
import com.example.pixels.model.MovieModel;
import com.example.pixels.repository.MovieRepository;
import com.example.pixels.service.MovieService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Transactional
public class MovieServiceImpl implements MovieService {
    @Autowired
    MovieRepository movieRepository;

    //TestWritten
    @Override
    public List<Movie> getAllMovies() throws ItemNotFoundException {
        Optional<List<Movie>> movies = Optional.of(movieRepository.findAll());
        if(movies.get().isEmpty()){
            throw new ItemNotFoundException("No Movies Available.");
        }
        return movies.get();
    }

    @Override
    public Movie getMovieById(Long movieId) throws ItemNotFoundException {
        Optional<Movie> movie = movieRepository.findById(movieId);
        if(movie.isEmpty()){
            throw new ItemNotFoundException("No Movies Available.");
        }
        return movie.get();
    }

    //TestWritten
    @Override
    public Movie getMovieByName(String movieName)
            throws ItemNotFoundException {
        Optional<Movie> movie = Optional.ofNullable(movieRepository.findByMovieNameIgnoreCase(movieName));

        if(movie.isEmpty()){
            throw new ItemNotFoundException("Movie Not Available.");
        }
        return movie.get();
    }

    //TestWritten
    @Override
    public List<Movie> getMoviesByGenre(String movieGenre) throws ItemNotFoundException {
        Optional<List<Movie>> movies = Optional.of(movieRepository.findAllByMovieGenreIgnoreCaseOrderByMovieName(movieGenre));
        if(movies.get().isEmpty()){
            throw new ItemNotFoundException("No Movies Available.");
        }
        return movies.get();
    }

    //TestWritten
    @Override
    public List<Movie> getMoviesByReleaseDate(LocalDate releaseDate) throws ItemNotFoundException {
        Optional<List<Movie>> movies = Optional.of(movieRepository.findAllByReleaseDateOrderByMovieName(releaseDate));
        if(movies.get().isEmpty()){
            throw new ItemNotFoundException("No Movies Available.");
        }
        return movies.get();
    }

    //TestWritten
    @Override
    public List<Movie> getMoviesByMovieRating(Double movieRating) throws ItemNotFoundException {
        int lowerRating = movieRating.intValue();
        int upperRating = lowerRating + 1;
        Optional<List<Movie>> movies = Optional.of(movieRepository.findByMovieRatingBetweenOrderByMovieName(lowerRating, upperRating));
        if(movies.get().isEmpty()){
            throw new ItemNotFoundException("No Movies Available.");
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
    public Movie updateMovie(MovieModel movieModel, Long movieId) throws ItemNotFoundException, SameDataUpdateExceptionHandler {
        Optional<Movie> movieFromDb = movieRepository.findById(movieId);

        if(movieFromDb.isEmpty())
            throw new ItemNotFoundException("Movie with Id "+movieId+" Not Found");
        Movie movieGetFromDb = movieFromDb.get();
//        if(movieGetFromDb.equals(movie))
//            throw new SameDataUpdateExceptionHandler("Same data passed, please update the content.");
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
//        if(Objects.nonNull(movieModel.getMovieRating()) &&
//                !"".equalsIgnoreCase(String.valueOf(movieModel.getMovieRating()))) {
//            movieGetFromDb.setMovieRating(Double.parseDouble(String.valueOf(movie.getMovieRating())));
//        }
        if (movieModel.getMovieImageUrl() != null && !movieModel.getMovieImageUrl().isEmpty()) {
            movieGetFromDb.setMovieImageUrl(movieModel.getMovieImageUrl());
        }
        return movieRepository.save(movieGetFromDb);
    }

    @Override
    public void deleteMovieById(Long movieId) throws ItemNotFoundException {
        Optional<Movie> movieFromDb = movieRepository.findById(movieId);
        if(movieFromDb.isEmpty())
            throw new ItemNotFoundException("Movie with Id "+movieId+" Not Found.");

        movieRepository.deleteById(movieId);
    }


}
