package com.example.pixels.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Booking {

    @Id
    private Long bookingId;

    @NotNull(message = "User id is mandatory.")
    private Long userId;

    @NotNull(message = "Show is mandatory.")
    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show showId;

//    @NotEmpty(message = "Seats are mandatory.")
//    private List<Seat> bookedSeats;
}
