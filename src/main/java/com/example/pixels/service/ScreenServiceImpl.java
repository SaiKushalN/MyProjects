package com.example.pixels.service;

import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.model.Screen;
import com.example.pixels.model.Theater;
import com.example.pixels.repository.ScreenRepository;
import com.example.pixels.repository.TheaterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Component
@Transactional
public class ScreenServiceImpl implements ScreenService{

    @Autowired
    ScreenRepository screenRepository;

    @Override
    public List<Screen> getByTheaterId(Long theaterId) {
        Theater theater = screenRepository.findByTheaterTheaterId(theaterId);
        List<Screen> screens = theater.getTheaterScreens();
        if(screens.isEmpty())
            throw new NoSuchElementException("No screens with theaterId "+theaterId+" are present.");
        return screens;
    }

//    @Override
//    public Screen getScreenByScreenIdAndTheaterId(Long theaterId, Long screenId) {
//        Theater theater = screenRepository.findByTheaterTheaterId(theaterId);
//        List<Screen> screens = theater.getTheaterScreens();
//        if(screens.isEmpty())
//            throw new NoSuchElementException("No screens with theaterId "+theaterId+" are present.");
//        for (Screen screen : screens) {
//            if (Objects.equals(screen.getScreenId(), screenId)) {
//                return screen;
//            }
//        }
//        throw new NoSuchElementException("No screen with screenId " + screenId + " in theaterId " + theaterId + " found.");
//    }

    @Override
    public List<Screen> saveScreens(Long theaterId, List<Screen> screens) {
        Theater theater = screenRepository.findByTheaterTheaterId(theaterId);
        screens.forEach(screen -> {
            screen.setTheater(theater);
        });
        return screenRepository.saveAll(screens);
    }

    @Override
    public Screen getScreenById(Long screenId) throws ItemNotFoundException {
        return screenRepository.findById(screenId)
                .orElseThrow(() -> new ItemNotFoundException("Screen not found."));
    }

}
