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

    @NotBlank(message = "Zipcode is mandatory.")
    private String zipcode;

    @OneToOne(mappedBy = "userAddress")
    private User user;
}
