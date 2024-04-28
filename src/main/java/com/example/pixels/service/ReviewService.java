package com.example.pixels.service;

import com.example.pixels.entity.Movie;
import com.example.pixels.entity.Review;
import com.example.pixels.entity.User;
import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.model.ReviewModel;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ReviewService {
    Review addReview(Movie movie, ReviewModel reviewModel, User user) throws ItemNotFoundException;

    Review getReviewById(Long reviewId);

    List<Review> getReviews(Long movieId);

    void sumOfRatings(Long movieId) throws ItemNotFoundException;

    Review likeReview(Long reviewId, User user);

    Review dislikeReview(Long reviewId, User user);

    String deleteReviewById(Long reviewId);

    Review editReview(Long reviewId, ReviewModel reviewModel) throws ItemNotFoundException;
}
