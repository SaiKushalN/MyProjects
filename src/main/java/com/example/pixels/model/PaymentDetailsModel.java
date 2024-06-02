package com.example.pixels.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDetailsModel {
    @NotBlank(message = "Cardholder name is required.")
    private String nameOnTheCard;

    @NotBlank(message = "Card number is required.")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 digits long.")
    private String cardNumber;

    @NotBlank(message = "Expiry date is required.")
    private String expiryDate;

    @NotNull(message = "Security code is required.")
    private Integer securityCode;  // Integer is fine here since no leading zeros are expected

    @NotBlank(message = "ZIP code is required.")
    @Size(min = 5, max = 5, message = "ZIP code must be exactly 5 digits long.")
    private String zipCode;
}

