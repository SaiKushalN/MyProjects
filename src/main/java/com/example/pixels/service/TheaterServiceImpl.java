package com.example.pixels.service;

import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.error.SameDataUpdateExceptionHandler;
import com.example.pixels.model.Address;
import com.example.pixels.model.Screen;
import com.example.pixels.model.Seat;
import com.example.pixels.model.Theater;
import com.example.pixels.repository.ScreenRepository;
import com.example.pixels.repository.TheaterRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Component
@Transactional
public class TheaterServiceImpl implements TheaterService{

    @Autowired
    TheaterRepository theaterRepository;

    @Autowired
    ScreenService screenService;

    @Autowired
    SeatService seatService;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    @Override
    public Theater getTheaterById(Long theaterId) throws ItemNotFoundException {
        return theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ItemNotFoundException("Theater not found."));
    }

    @Override
    public Theater saveTheater(Theater theater) {
        return theaterRepository.save(theater);
    }

    @Override
    public String deleteTheaterById(Long theaterId) throws ItemNotFoundException {
        Optional<Theater> theaterFromDb = theaterRepository.findById(theaterId);
        if(theaterFromDb.isEmpty())
            throw new ItemNotFoundException("Theater with Id "+theaterId+" Not Found.");
        theaterRepository.deleteById(theaterId);
        return "Theater deleted successfully.";
    }

    @Override
    public List<Theater> getTheaterByName(String theaterName) {
        Optional<List<Theater>> theaters = Optional.ofNullable(theaterRepository.findByTheaterNameIgnoreCase(theaterName));
        if(theaters.isEmpty())
            throw new NoSuchElementException("Theater with name "+
                    theaterName+" is not found.");
        return theaters.get();
    }

    @Override
    public List<Theater> getTheatersByCity(String city) {
        Optional<List<Theater>> theaters = Optional.ofNullable(theaterRepository.findByTheaterAddressCityIgnoreCase(city));
        if(theaters.isEmpty())
            throw new NoSuchElementException("Theater within city "+
                    city+" are not found.");
        return theaters.get();
    }

    @Override
    public List<Theater> getTheatersByZipcode(Long zipcode) {
        Optional<List<Theater>> theaters = Optional.ofNullable(theaterRepository.findByTheaterAddressZipcode(zipcode));
        if(theaters.isEmpty())
            throw new NoSuchElementException("Theater within zipcode "+
                    zipcode+" are not found.");
        return theaters.get();
    }

    @Override
    public List<Theater> getTheatersByState(String state) {
        Optional<List<Theater>> theaters = Optional.ofNullable(theaterRepository.findByTheaterAddressStateIgnoreCase(state));
        if(theaters.isEmpty())
            throw new NoSuchElementException("Theater within state "+
                    state+" are not found.");
        return theaters.get();
    }

    @Override
    public Theater updateTheater(Theater updatedTheater, Long theaterId)
            throws ItemNotFoundException, SameDataUpdateExceptionHandler {
        Theater existingTheater = getTheaterById(theaterId);

        if (updatedTheater.getTheaterName() != null) {
            existingTheater.setTheaterName(updatedTheater.getTheaterName());
        }

        if (updatedTheater.getTheaterAddress() != null) {
            modelMapper.map(updatedTheater.getTheaterAddress(), existingTheater.getTheaterAddress());
        }

        if (updatedTheater.getTheaterScreens() != null) {
            List<Screen> updatedScreens = updatedTheater.getTheaterScreens();

            updatedScreens.forEach(updatedScreen -> {
                try {
                    Screen existingScreen = screenService.getScreenById(updatedScreen.getScreenId());
                    if (updatedScreen.getScreenNumber() != null) {
                        existingScreen.setScreenNumber(updatedScreen.getScreenNumber());
                    }
                    if(updatedScreen.getScreenSeats() !=null)
                    {
                        List<Seat> updatedSeats = updatedScreen.getScreenSeats();
                        updatedSeats.forEach(updatedSeat -> {
                            Seat existingSeat = null;
                            try {
                                existingSeat = seatService.getSeatById(updatedSeat.getSeatId());
                            } catch (ItemNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            if(updatedSeat.getSeatNumber() != null)
                                existingSeat.setSeatNumber(updatedSeat.getSeatNumber());
                            if(updatedSeat.isSeatBooked())
                                existingSeat.setSeatBooked(true);
                        });
                    }


                } catch (ItemNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return theaterRepository.save(existingTheater);
    }

}
