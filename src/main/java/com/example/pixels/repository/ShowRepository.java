package com.example.pixels.repository;

import com.example.pixels.model.Movie;
import com.example.pixels.model.Screen;
import com.example.pixels.model.Show;
import com.example.pixels.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show,Long> {
    List<Show> findAllByMovieMovieId(Long movieId);

    Theater findByTheaterTheaterId(Long theaterId);

    Screen findByScreenNumberScreenId(Long screenId);

    Movie findByMovieMovieId(Long movieId);
}
