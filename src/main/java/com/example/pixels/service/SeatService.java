package com.example.pixels.service;

import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.model.Seat;
import org.springframework.stereotype.Service;

@Service
public interface SeatService {
    Seat getSeatById(Long seatId) throws ItemNotFoundException;
}
