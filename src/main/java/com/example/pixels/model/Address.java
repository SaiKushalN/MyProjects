package com.example.pixels.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank(message = "Address line one is mandatory.")
    private String addressLineOne;

    private String addressLineTwo;

    @NotBlank(message = "City is mandatory.")
    private String city;

    @NotBlank(message = "State is mandatory.")
    private String state;

    @NotBlank(message = "Country is mandatory.")
    private String country;

    @NotNull(message = "Zipcode is mandatory.")
    @Range(min = 10000L, max = 99999L, message = "Zipcode must be between 10000 and 99999")
    private Long zipcode;

    @OneToOne(mappedBy = "theaterAddress")
    private Theater theater;
}
