package com.example.pixels.service;

import com.example.pixels.model.Booking;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    List<Booking> getMyBookings(Long userId);
}
