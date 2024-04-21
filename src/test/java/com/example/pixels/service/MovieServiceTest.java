package com.example.pixels.service;

import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.entity.Movie;
import com.example.pixels.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MovieServiceTest {

    @Autowired
    private MovieService movieService;

    @MockBean
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        Movie movie1 = Movie.builder()
                .id(1L)
                .movieName("Salaar")
                .movieGenre("Drama")
                .releaseDate(LocalDate.ofEpochDay(2023-12-20))
                .movieRating(3.2)
                .build();
        Movie movie2 = Movie.builder()
                .id(1L)
                .movieName("Dunki")
                .movieGenre("Drama")
                .releaseDate(LocalDate.ofEpochDay(2023-12-20))
                .movieRating(1.0)
                .build();
        List<Movie> movies = Arrays.asList(movie1,movie2);

        Mockito.when(movieRepository
                        .findByMovieNameIgnoreCase("Salaar"))
                .thenReturn(movie1);
        Mockito.when(movieRepository
                        .findAll())
                .thenReturn((List<Movie>) movies);
        Mockito.when(movieRepository
                        .findAllByMovieGenreIgnoreCaseOrderByMovieName("Drama"))
                .thenReturn(movies);
        Mockito.when(movieRepository
                .findAllByReleaseDateOrderByMovieName(LocalDate.ofEpochDay(2023-12-20)))
                .thenReturn(movies);
        Mockito.when(movieRepository
                        .findByMovieRatingBetweenOrderByMovieName(3,4))
                .thenReturn(movies);
    }

    @Test
    @DisplayName("Get Movie based on valid movie name.")
    public void whenValidMovieName_thenMovieShouldFound() throws ItemNotFoundException {
        String movieName = "Salaar";
        Movie found =
                movieService.getMovieByName(movieName);
        assertEquals(movieName, found.getMovieName());
    }

    @Test
    @DisplayName("No Movie based on invalid movie name.")
    public void whenInValidMovieName_thenMovieShouldNotFound() throws ItemNotFoundException {
        String movieName = "Salar";
        Mockito.when(movieRepository.findByMovieNameIgnoreCase(movieName))
                .thenReturn(null);
        assertThrows(ItemNotFoundException.class, () -> {
            movieService.getMovieByName(movieName);
        });
    }

    @Test
    @DisplayName("Get all movies.")
    public void whenAllMoviesCalled_thenAllMoviesShouldFound() throws ItemNotFoundException {
        List<Movie> found =
                movieService.getAllMovies();
        assertNotNull(found);
        assertEquals(2, found.size());
    }

    @Test
    @DisplayName("Get movies by genre.")
    public void whenValidMovieGenre_thenAllMoviesWithGenreShouldFound()
            throws ItemNotFoundException {
        String genre = "Drama";
        List<Movie> found =
                movieService.getMoviesByGenre(genre);
        assertEquals(2, found.size());
        assertEquals("Dunki",found.get(1).getMovieName());
    }

    @Test
    @DisplayName("Get movies by release date")
    public void whenValidReleaseDate_thenAllMoviesWithReleaseDateShouldFound()
            throws ItemNotFoundException {
        LocalDate date = LocalDate.ofEpochDay(2023-12-20);
        List<Movie> found =
                movieService.getMoviesByReleaseDate(date);
        assertEquals(2, found.size());
        assertEquals("Dunki",found.get(1).getMovieName());
    }

    @Test
    @DisplayName("Get movies by Rating")
    public void whenValidRating_thenAllMoviesWithRatingShouldFound()
            throws ItemNotFoundException {
        Double rating = 3.0;
        List<Movie> found =
                movieService.getMoviesByMovieRating(rating);
        assertEquals(2, found.size());
        assertEquals("Salaar",found.get(0).getMovieName());
    }

}