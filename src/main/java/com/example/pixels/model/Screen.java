package com.example.pixels.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "screenId")

public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long screenId;

    @Column
    private Long screenNumber;

    @NotEmpty(message = "Screen seats are mandatory.")
    @OneToMany(mappedBy = "screen",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Seat> screenSeats;

    @ManyToOne
    @JoinColumn(name = "theater_id")
    private Theater theater;

}
