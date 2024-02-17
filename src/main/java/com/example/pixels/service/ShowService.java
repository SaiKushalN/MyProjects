package com.example.pixels.service;

import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.model.Seat;
import com.example.pixels.model.Show;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShowService {
    List<Show> getAllShowsByMovieId(Long movieId);

    String deleteByShowId(Long showId) throws ItemNotFoundException;

    Show createShow(Show show);

    Show getShowById(Long showId) throws ItemNotFoundException;

//    List<Seat> getAllSeatsByShowId(Long showId);
}
