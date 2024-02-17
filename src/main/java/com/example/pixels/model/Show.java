package com.example.pixels.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "showId")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showId;

    @NotNull(message = "Movie is mandatory.")
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Future(message = "Show Time must be in the future")
    @NotNull(message = "Show Time is mandatory.")
    private LocalDateTime showTime;

    @ManyToOne
    @JoinColumn(name = "theater_id")
    private Theater theater;

    @NotNull(message = "Screen is mandatory.")
    @ManyToOne
    @JoinColumn(name = "screen_id")
    private Screen screenNumber;

//    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Seat> seats;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "screen_id")
//    private List<Seat> seats;

//    @OneToMany(mappedBy = "show",
//            cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY)
//    private List<Seat> seats;

}
