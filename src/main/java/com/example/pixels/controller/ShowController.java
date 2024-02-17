package com.example.pixels.controller;

import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.model.Screen;
import com.example.pixels.model.Seat;
import com.example.pixels.model.Show;
import com.example.pixels.service.MovieService;
import com.example.pixels.service.ScreenService;
import com.example.pixels.service.ShowService;
import com.example.pixels.service.TheaterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shows")
public class ShowController {

    @Autowired
    ShowService showService;

    @Autowired
    MovieService movieService;

    @Autowired
    TheaterService theaterService;

    @Autowired
    ScreenService screenService;

    @GetMapping("/{movieId}/all")
    public List<Show> getAllShowsByMovieId(@PathVariable("movieId") Long movieId){
        return showService.getAllShowsByMovieId(movieId);
    }
    @PostMapping("/createShow")
    public Show createShow(@Valid @RequestBody Show show) throws ItemNotFoundException {
        System.out.println(show);
        Screen screen = screenService.getScreenById(show.getScreenNumber().getScreenId());
        show.setMovie(movieService.getMovieById(show.getMovie().getMovieId()));
        show.setTheater(theaterService.getTheaterById(show.getTheater().getTheaterId()));
        show.setScreenNumber(screen);
//        show.setSeats(screen.getScreenSeats());
        return showService.createShow(show);
    }

    @GetMapping("/{showId}/showSeats")
    public List<Seat> getAllSeatsByShowId(@PathVariable("showId") Long showId) throws ItemNotFoundException {
        Show show = showService.getShowById(showId);
        return show.getScreenNumber().getScreenSeats();
    }

    @DeleteMapping("/delete/{showId}")
    public String deleteByShowId(@PathVariable("showId") Long showId) throws ItemNotFoundException {
        return showService.deleteByShowId(showId);
    }

}
