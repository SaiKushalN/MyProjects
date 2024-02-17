package com.example.pixels.controller;

import com.example.pixels.model.Booking;
import com.example.pixels.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    BookingService bookingService;

    @GetMapping("/{usedId}/all")
    public List<Booking> getMyBookings(@PathVariable("usedId") Long userId) {
        return bookingService.getMyBookings(userId);
    }
}
