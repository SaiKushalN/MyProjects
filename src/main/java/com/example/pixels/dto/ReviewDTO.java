package com.example.pixels.dto;

import com.example.pixels.entity.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private Review review;
    private String premiumUser;
}
