package com.example.pixels.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PremiumPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Plan name required.")
    @Column(unique = true, nullable = false)
    private String planName;

    @DecimalMin(value = "0.0", message = "Plan price must be greater than zero.")
    private Double planPrice;

    @Min(value = 1, message = "Plan months must be at least 1.")
    private Integer planMonths;

    private String planDescription;

    private boolean activePlan;

    private String currency;

    //IDEA: create a payment receipt, payment page

}
