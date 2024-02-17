package com.example.pixels.service;

import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.model.Seat;
import com.example.pixels.model.Show;
import com.example.pixels.model.Theater;
import com.example.pixels.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ShowServiceImpl implements ShowService{

    @Autowired
    ShowRepository showRepository;
    @Override
    public List<Show> getAllShowsByMovieId(Long movieId) {
        return showRepository.findAllByMovieMovieId(movieId);
    }

    @Override
    public String deleteByShowId(Long showId) throws ItemNotFoundException {
        Optional<Show> showFromDb = showRepository.findById(showId);
        if(showFromDb.isEmpty())
            throw new ItemNotFoundException("Show with Id "+showId+" Not Found.");
        showRepository.deleteById(showId);
        return "Show deleted successfully.";
    }

    @Override
    public Show createShow(Show show) {
        return showRepository.save(show);
    }

    @Override
    public Show getShowById(Long showId) throws ItemNotFoundException {
        Optional<Show> show = showRepository.findById(showId);
        if(show.isEmpty())
            throw new ItemNotFoundException("Show is not found");
        return show.get();
    }
}
