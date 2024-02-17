package com.example.pixels.controller;

import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.error.SameDataUpdateExceptionHandler;
import com.example.pixels.model.Theater;
import com.example.pixels.service.TheaterService;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/theater")
public class TheaterController {

    @Autowired
    TheaterService theaterService;

    public Long screenValue=1L;

    public int seatValue=1;

    @GetMapping("/all")
    public List<Theater> getAllTheaters() {
        return theaterService.getAllTheaters();
    }

    @GetMapping("/theaterName/{theaterName}")
    public List<Theater> getTheaterByName(@PathVariable("theaterName") String theaterName) {
        return theaterService.getTheaterByName(theaterName);
    };

    @GetMapping("/location/{city}")
    public List<Theater> getTheatersByCity(@PathVariable("city") String city) {
        return theaterService.getTheatersByCity(city);
    };

    @GetMapping("/state/{state}")
    public List<Theater> getTheatersByState(@PathVariable("state") String state) {
        return theaterService.getTheatersByState(state);
    };

    @GetMapping("/zipcode/{zipcode}")
    public List<Theater> getTheatersByZipcode(@PathVariable("zipcode") @Valid
                                                  @Range(min = 10000L, max = 99999L, message = "Zipcode must be between 10000 and 99999")
                                                  Long zipcode) {
        return theaterService.getTheatersByZipcode(zipcode);
    };

    @PostMapping("/addTheater")
    public ResponseEntity<Object> saveTheater(@Valid @RequestBody Theater theater) {

        if(theater.getTheaterScreens() == null)
            throw new NullPointerException("Screens cannot be empty.");

        theater.getTheaterScreens().forEach(screen -> {
            if(screen.getScreenNumber() == null) {
                screen.setScreenNumber(screenValue);
                screenValue++;
            }
            screen.setTheater(theater);
            seatValue=1;
            if(screen.getScreenSeats() == null)
                throw new NullPointerException("Seats cannot be empty.");

            screen.getScreenSeats().forEach(seat -> {
                if(seat.getSeatNumber() == null) {
                    seat.setSeatNumber("" + seatValue);
                    seatValue++;
                }
                seat.setScreen(screen);
            });
        });
        return ResponseEntity.ok(theaterService.saveTheater(theater));
    }

    @GetMapping("/{theaterId}")
    public Theater getTheaterById(@PathVariable("theaterId") Long theaterId) throws ItemNotFoundException {
        return theaterService.getTheaterById(theaterId);
    }

    @PutMapping("/updateTheater/{theaterId}")
    public Theater updateTheater(@RequestBody Theater theater,
                             @PathVariable("theaterId") Long theaterId) throws ItemNotFoundException, SameDataUpdateExceptionHandler {
        return theaterService.updateTheater(theater, theaterId);
    }

    @DeleteMapping("/deleteTheater/{theaterId}")
    public String deleteTheater(@PathVariable("theaterId") Long theaterId) throws ItemNotFoundException {
        return theaterService.deleteTheaterById(theaterId);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationException(MethodArgumentNotValidException exception) {
        List<String> errors = new ArrayList<>();
        exception.getBindingResult().getAllErrors()
                .forEach(violation ->
                errors.add(violation.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<List<String>> handleMethodArgumentTypeException(MethodArgumentTypeMismatchException exception) {
        List<String> errors = new ArrayList<>();
        errors.add(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errors);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<List<String>> handleValidationException(HandlerMethodValidationException exception) {
        List<String> errors = new ArrayList<>();
        exception.getAllValidationResults().forEach(parameterValidationResult -> {
            parameterValidationResult.getResolvableErrors().forEach(error -> {
                errors.add(error.getDefaultMessage());
            });
        });
//        errors.add(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map<String, String> handleParseException(HttpMessageNotReadableException exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Input error", exception.getMessage());
        return errors;
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNullPointerException(NullPointerException exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Input error", exception.getMessage());
        return errors;
    }
}
