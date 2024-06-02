package com.example.pixels.controller;

import com.example.pixels.entity.Movie;
import com.example.pixels.entity.PremiumUser;
import com.example.pixels.entity.ReviewAlert;
import com.example.pixels.entity.User;
import com.example.pixels.model.PaymentDetailsModel;
import com.example.pixels.model.ReviewAlertModel;
import com.example.pixels.service.MovieService;
import com.example.pixels.service.PremiumService;
import com.example.pixels.service.ReviewAlertService;
import com.example.pixels.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class PremiumMemberController {

    @Autowired
    UserService userService;

    @Autowired
    PremiumService premiumService;

    @Autowired
    MovieService movieService;

    @Autowired
    ReviewAlertService reviewAlertService;

    @GetMapping("/user/premium/getStatus")
    public String getPremiumStatus() throws IllegalAccessException {
        User user = userService.getLoggedInUser();
        reviewAlertService.checkIfAPremiumUser(user);
        return premiumService.getPremiumStatus(user);
    }

    @GetMapping("/user/premium/getMyAllAlerts")
    public List<ReviewAlert> getMyAllAlerts() throws IllegalAccessException {
        User user = userService.getLoggedInUser();
        reviewAlertService.checkIfAPremiumUser(user);
        return reviewAlertService.getMyAllAlerts(user);
    }

    @GetMapping("/user/premium/getAlertById/{alertId}")
    public ReviewAlert getAlertById(@PathVariable("alertId") Long alertId) throws IllegalAccessException {
        User user = userService.getLoggedInUser();
        reviewAlertService.checkIfAPremiumUser(user);
        return reviewAlertService.getReviewAlertById(alertId, user);
    }

    @PostMapping("/user/premium/setMovieAlert/{movieId}")
    public ReviewAlert addMovieReviewAlert(@RequestBody ReviewAlertModel reviewAlertModel,
                                      @PathVariable("movieId") Long movieId) throws IllegalAccessException {
        User user = userService.getLoggedInUser();
        reviewAlertService.checkIfAPremiumUser(user);
        validateUserType(reviewAlertModel.getUserType());
        Movie movie = movieService.getMovieById(movieId);
        if(reviewAlertModel.getCriticName()!=null && !reviewAlertModel.getCriticName().isEmpty())
        {
            if(!userService.getAllCriticNames().contains(reviewAlertModel.getCriticName()))
                throw new IllegalArgumentException("Critic not available.");
        }
        PremiumUser premiumUser = premiumService.getPremiumUser(user);
        return reviewAlertService.addMovieReviewAlert(reviewAlertModel, movie, premiumUser);
    }

    @PutMapping("/user/premium/editAlert/{alertId}")
    public ReviewAlert editReviewAlert(@RequestBody ReviewAlertModel reviewAlertModel,
                                       @PathVariable("alertId") Long alertId) throws IllegalAccessException {

        User user = userService.getLoggedInUser();
        reviewAlertService.checkIfAPremiumUser(user);
        validateUserType(reviewAlertModel.getUserType());
        if(reviewAlertModel.getCriticName()!=null && !reviewAlertModel.getCriticName().isEmpty())
        {
            if(!userService.getAllCriticNames().contains(reviewAlertModel.getCriticName()))
                throw new IllegalArgumentException("Critic not available.");
        }
        return reviewAlertService.editReviewAlert(reviewAlertModel, alertId, user);
    }

    @DeleteMapping("/user/premium/deleteAlert/{alertId}")
    public String deleteReviewAlert(@PathVariable("alertId") Long alertId) throws IllegalAccessException {
        User user = userService.getLoggedInUser();
        reviewAlertService.checkIfAPremiumUser(user);
        return reviewAlertService.deleteAlert(alertId, user);
    }

    public void validateUserType(String userType){
        Set<String> validUserTypes = Set.of("USER", "CRITIC");
        if(!validUserTypes.contains(userType))
            throw new IllegalArgumentException("Invalid selection, Please select USER or CRITIC.");
    }
}
