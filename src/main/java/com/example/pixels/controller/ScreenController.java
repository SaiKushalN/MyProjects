package com.example.pixels.controller;

import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.model.Screen;
import com.example.pixels.model.Theater;
import com.example.pixels.service.ScreenService;
import com.example.pixels.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Screens")
public class ScreenController {

    @Autowired
    ScreenService screenService;

    @GetMapping("theaters/{theaterId}/allScreens")
    public List<Screen> getAllScreens(@PathVariable("theaterId") Long theaterId) {
        return screenService.getByTheaterId(theaterId);
    }

    public Screen getScreenById(Long screenId) throws ItemNotFoundException {
        return screenService.getScreenById(screenId);
    }

    @PostMapping("/theaters/{theaterId}/addScreens")
    public ResponseEntity<List<Screen>> addMultipleScreens(@PathVariable(value = "theaterId") Long theaterId,
                                                @RequestBody List<Screen> screens) {
        return new ResponseEntity<>(screenService.saveScreens(theaterId, screens), HttpStatus.CREATED);
    }

}
