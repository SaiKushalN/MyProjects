package com.example.pixels.service;

import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.error.SameDataUpdateExceptionHandler;
import com.example.pixels.model.Theater;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TheaterService {
    List<Theater> getAllTheaters();

    Theater getTheaterById(Long theaterId) throws ItemNotFoundException;

    Theater saveTheater(Theater theater);

    String deleteTheaterById(Long theaterId) throws ItemNotFoundException;

    List<Theater> getTheaterByName(String theaterName);

    List<Theater> getTheatersByCity(String city);

    List<Theater> getTheatersByZipcode(Long zipcode);

    List<Theater> getTheatersByState(String state);

    Theater updateTheater(Theater theater, Long theaterId) throws ItemNotFoundException, SameDataUpdateExceptionHandler;
}
