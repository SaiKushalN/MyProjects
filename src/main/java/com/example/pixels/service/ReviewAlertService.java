package com.example.pixels.service;

import com.example.pixels.entity.Movie;
import com.example.pixels.entity.PremiumUser;
import com.example.pixels.entity.ReviewAlert;
import com.example.pixels.entity.User;
import com.example.pixels.model.ReviewAlertModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewAlertService {
    ReviewAlert addMovieReviewAlert(ReviewAlertModel reviewAlertModel, Movie movie, PremiumUser premiumUser);

    String deleteAlert(Long alertId, User user) throws IllegalAccessException;

    ReviewAlert editReviewAlert(ReviewAlertModel reviewAlertModel, Long alertId, User user) throws IllegalAccessException;

    ReviewAlert saveReviewAlert(ReviewAlert reviewAlert);

    ReviewAlert getReviewAlertById(Long alertId, User user) throws IllegalAccessException;

    List<ReviewAlert> getMyAllAlerts(User user) throws IllegalAccessException;

    void checkIfAPremiumUser(User user) throws IllegalAccessException;

}
