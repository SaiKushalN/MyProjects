package com.example.pixels.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PremiumPlanEditModel {
    private String planDescription;
    private boolean activePlan;
}
