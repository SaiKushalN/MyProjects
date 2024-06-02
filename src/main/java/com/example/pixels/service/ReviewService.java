package com.example.pixels.service;

import com.example.pixels.entity.Review;
import com.example.pixels.entity.User;
import com.example.pixels.model.ReviewModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {
    Review addReview(Long movieId, ReviewModel reviewModel, User user);

    Review getReviewById(Long reviewId);

    List<Review> getReviews(Long movieId);

    void sumOfRatings(Long movieId);

    Review likeReview(Long reviewId, User user);

    Review dislikeReview(Long reviewId, User user);

    String deleteReviewById(Long reviewId);

    Review editReview(Long reviewId, ReviewModel reviewModel);

    List<Review> myReviews(User user);
}
