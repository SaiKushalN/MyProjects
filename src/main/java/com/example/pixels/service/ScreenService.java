package com.example.pixels.service;

import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.model.Screen;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ScreenService {
    List<Screen> getByTheaterId(Long theaterId);

    List<Screen> saveScreens(Long theaterId, List<Screen> screens);

    Screen getScreenById(Long screenId) throws ItemNotFoundException;

//    Screen getScreenByScreenIdAndTheaterId(Long theaterId, Long screenId);

    }
