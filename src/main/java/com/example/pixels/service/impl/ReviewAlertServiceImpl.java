package com.example.pixels.service.impl;

import com.example.pixels.entity.Movie;
import com.example.pixels.entity.PremiumUser;
import com.example.pixels.entity.ReviewAlert;
import com.example.pixels.entity.User;
import com.example.pixels.error.SameDataUpdateExceptionHandler;
import com.example.pixels.model.ReviewAlertModel;
import com.example.pixels.repository.ReviewAlertRepository;
import com.example.pixels.service.ReviewAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Component
public class ReviewAlertServiceImpl implements ReviewAlertService {

    @Autowired
    ReviewAlertRepository reviewAlertRepository;

    //CRUD
    @Override
    public ReviewAlert addMovieReviewAlert(ReviewAlertModel reviewAlertModel, Movie movie, PremiumUser premiumUser) {

        String currentUserType = (reviewAlertModel.getUserType().contains("USER")?"CRITICUSER":"CRITIC");
        String currentCriticName = "";
        if(currentUserType.equals("CRITICUSER")){
            currentCriticName = "ANY";
        }
        else{
            currentCriticName = (reviewAlertModel.getCriticName().isEmpty())?"ANY":reviewAlertModel.getCriticName();
        }

        Optional<ReviewAlert> reviewAlertOptional =
                reviewAlertRepository.findByMovieIdAndUserTypeAndCriticNameAndPremiumUserId(movie.getId(),
                        currentUserType,currentCriticName,premiumUser.getId());
        if(reviewAlertOptional.isPresent())
            throw new IllegalStateException("Alert already present for given movie, user type, and critic name.");

        String alertType = determineAlertType(currentUserType, reviewAlertModel.getCriticName());

        reviewAlertRepository.findAllByPremiumUserId(premiumUser.getId()).ifPresent(alerts -> {
            validatePreviousAlerts(alerts, alertType);
        });

        ReviewAlert reviewAlert = ReviewAlert.builder()
                .movie(movie)
                .userType(currentUserType)
                .premiumUser(premiumUser)
                .criticName(currentCriticName)
                .alertType(alertType)
                .build();

        return saveReviewAlert(reviewAlert);
    }

    private String determineAlertType(String userType, String criticName) {
        return switch (userType) {
            case "CRITICUSER" -> "MOVIE";
            case "CRITIC" -> criticName.isEmpty() ? "MOVIEANYCRITIC" : "MOVIEANDNAME";
            default -> throw new IllegalArgumentException("Unsupported user type");
        };
    }

    private void validatePreviousAlerts(List<ReviewAlert> alerts, String alertType) {
        for (ReviewAlert alert : alerts) {
            switch (alertType) {
                case "MOVIEANYCRITIC":
                    if(alert.getAlertType().equals("MOVIEANDNAME"))
                        reviewAlertRepository.delete(alert);
                    if ("MOVIE".equals(alert.getAlertType())) {
                        throw new DataIntegrityViolationException("Your previous alert will give you same results. No need to add this or change it");
                    }
                    break;
                case "MOVIEANDNAME":
                    if ("MOVIE".equals(alert.getAlertType()) || "MOVIEANYCRITIC".equals(alert.getAlertType())) {
                        throw new DataIntegrityViolationException("Your previous alert will give you same results. No need to add this or change it");
                    }
                    break;
                case "MOVIE":
                    reviewAlertRepository.delete(alert);
            }
        }
    }

    @Override
    public ReviewAlert editReviewAlert(ReviewAlertModel reviewAlertModel, Long alertId, User user) throws IllegalAccessException {
        ReviewAlert reviewAlert = getReviewAlertById(alertId, user);

        String currentUserType = (reviewAlertModel.getUserType().equals("USER")?"CRITICUSER":"CRITIC");

        String criticName = "";
        if(currentUserType.equals("CRITICUSER")){
            criticName = "ANY";
        }
        else{
            criticName = (reviewAlertModel.getCriticName().isEmpty())?"ANY":reviewAlertModel.getCriticName();
        }

        String alertType = switch (currentUserType) {
            case "CRITICUSER" -> "MOVIE";
            case "CRITIC" -> (reviewAlertModel.getCriticName().isEmpty()) ? "MOVIEANYCRITIC" : "MOVIEANDNAME";
            default -> "";
        };

//        reviewAlertRepository.findAllByPremiumUserId(user.getPremiumUser().getId()).ifPresent(alerts -> {
//            validatePreviousAlerts(alerts, alertType);
//        });



        reviewAlert.setUserType(currentUserType);
        reviewAlert.setCriticName(criticName);
        reviewAlert.setAlertType(alertType);

        return saveReviewAlert(reviewAlert);
    }

    @Override
    public String deleteAlert(Long alertId, User user) throws IllegalAccessException {
        Optional<ReviewAlert> reviewAlert = reviewAlertRepository.findById(alertId);
        if(reviewAlert.isEmpty())
            throw new NoSuchElementException("Alert not present.");
        validateReviewAlertAccess(user,reviewAlert.get());
        reviewAlertRepository.delete(reviewAlert.get());
        return "Alert removed.";
    }

    @Override
    public ReviewAlert saveReviewAlert(ReviewAlert reviewAlert){
        return reviewAlertRepository.save(reviewAlert);
    }

    @Override
    public ReviewAlert getReviewAlertById(Long alertId, User user) throws IllegalAccessException {
        Optional<ReviewAlert> reviewAlert = reviewAlertRepository.findById(alertId);
        if(reviewAlert.isEmpty())
            throw new NoSuchElementException("Alert does not exist.");
        validateReviewAlertAccess(user,reviewAlert.get());
        return reviewAlert.get();
    }

    @Override
    public List<ReviewAlert> getMyAllAlerts(User user) throws IllegalAccessException {
        Optional<List<ReviewAlert>> reviewAlerts = reviewAlertRepository
                .findAllByPremiumUserId(user.getPremiumUser().getId());
        if(reviewAlerts.isEmpty())
            throw new NoSuchElementException("No review alerts assigned yet.");
        return reviewAlerts.get();
    }

    public void validateReviewAlertAccess(User user, ReviewAlert reviewAlert) throws IllegalAccessException {
        if(!(user.getPremiumUser()).equals(reviewAlert.getPremiumUser()))
            throw new IllegalAccessException("Cannot access other user's alert.");
    }

    @Override
    public void checkIfAPremiumUser(User user) throws IllegalAccessException {
        if(user.getPremiumUser() == null)
            throw new IllegalAccessException("You are not a premium user.");
    }


}
