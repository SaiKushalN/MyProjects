package com.example.pixels.repository;

import com.example.pixels.model.Movie;
import com.example.pixels.model.Screen;
import com.example.pixels.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScreenRepository extends JpaRepository<Screen, Long> {
    Theater findByTheaterTheaterId(Long theaterId);
}
