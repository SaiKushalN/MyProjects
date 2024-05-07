package com.example.pixels.model;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PremiumPlanModel {
    private String planName;
    private Double planPrice;
    private Integer planMonths;
    private String planDescription;
    private boolean activePlan;
}
