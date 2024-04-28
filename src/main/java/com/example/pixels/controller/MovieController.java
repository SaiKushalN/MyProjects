package com.example.pixels.controller;

import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.error.SameDataUpdateExceptionHandler;
import com.example.pixels.entity.Movie;
import com.example.pixels.model.MovieModel;
import com.example.pixels.service.MovieService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    MovieService movieService;

    private final Logger LOGGER= LoggerFactory.getLogger(MovieController.class);

    @GetMapping("/all")
    public List<Movie> getMovies() throws ItemNotFoundException {
        return movieService.getAllMovies();
    }

    @GetMapping("/movieName/{movieName}")
    public Movie getMovieByName(@PathVariable("movieName") String movieName) throws ItemNotFoundException {return movieService.getMovieByName(movieName);}

    @GetMapping("/movieGenre/{movieGenre}")
    public List<Movie> getMoviesByGenre(@PathVariable("movieGenre") String movieGenre) throws ItemNotFoundException {return movieService.getMoviesByGenre(movieGenre);}

    @GetMapping("/releaseDate/{releaseDate}")
    public List<Movie> getMoviesByReleaseDate(@PathVariable("releaseDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate releaseDate) throws ItemNotFoundException {return movieService.getMoviesByReleaseDate(releaseDate);}

    @GetMapping("/movieRating/{movieRating}")
    public List<Movie> getMoviesByMovieRating(@PathVariable("movieRating") Double movieRating) throws ItemNotFoundException {return movieService.getMoviesByMovieRating(movieRating);}

//    @PostMapping("/addMovie")
//    public ResponseEntity<Object> saveMovie(@Valid @RequestBody Movie movie) {
//        LOGGER.info("New Movie Added.");
//        return ResponseEntity.ok(movieService.saveMovie(movie));
//    }

    @PostMapping("/addMovie")
    public ResponseEntity<Object> saveMovie(@Valid @RequestBody MovieModel movieModel) {
        LOGGER.info("New Movie Added.");
        return ResponseEntity.ok(movieService.saveMovie(movieModel));
    }

//    @PostMapping("/addListOfMovies")
//    public List<Movie> saveAllMovie(@RequestBody List<Movie> movies) {
//        return movieService.saveAllMovie(movies);
//    }

//    @PutMapping("/updateMovie/{movieId}")
//    public Movie updateMovie(@RequestBody Movie movie,
//                             @PathVariable("movieId") Long movieId) throws ItemNotFoundException, SameDataUpdateExceptionHandler {
//        return movieService.updateMovie(movie, movieId);
//    }

    @PutMapping("/updateMovie/{movieId}")
    public Movie updateMovie(@RequestBody MovieModel movieModel,
                             @PathVariable("movieId") Long movieId) throws ItemNotFoundException, SameDataUpdateExceptionHandler {
        return movieService.updateMovie(movieModel, movieId);
    }

    @DeleteMapping("/deleteMovie/{movieId}")
    public String deleteMovieById(@PathVariable("movieId") Long movieId) throws ItemNotFoundException {
        movieService.deleteMovieById(movieId);
        return "Movie deleted successfully.";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST  )
    public Map<String, String> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMsg = error.getDefaultMessage();
            errors.put(fieldName, errorMsg);
        });
        return errors;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public Map<String, String> handleParseException(HttpMessageNotReadableException exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Input error", exception.getMessage());
        return errors;
    }
}

