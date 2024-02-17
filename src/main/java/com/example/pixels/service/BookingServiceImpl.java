package com.example.pixels.service;

import com.example.pixels.model.Booking;
import com.example.pixels.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingServiceImpl implements BookingService{
    @Autowired
    BookingRepository bookingRepository;
    @Override
    public List<Booking> getMyBookings(Long userId) {
        return bookingRepository.findAllByUserId(userId);
    }
}
