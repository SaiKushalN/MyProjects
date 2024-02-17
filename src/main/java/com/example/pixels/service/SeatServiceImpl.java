package com.example.pixels.service;

import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.model.Seat;
import com.example.pixels.repository.SeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class SeatServiceImpl implements SeatService{

    @Autowired
    SeatRepository seatRepository;
    @Override
    public Seat getSeatById(Long seatId) throws ItemNotFoundException {
        return seatRepository.findById(seatId)
                .orElseThrow(() -> new ItemNotFoundException("Seat not found."));
    }
}
