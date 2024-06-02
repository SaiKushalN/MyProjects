package com.example.pixels.model;

import com.example.pixels.entity.Movie;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewModel {
    private Double ratingPoints;
    private String reviewDescription;
    private Boolean suggest;
    private String childSafety;
}
