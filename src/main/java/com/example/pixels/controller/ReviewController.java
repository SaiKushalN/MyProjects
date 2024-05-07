package com.example.pixels.controller;

import com.example.pixels.dto.ReviewDTO;
import com.example.pixels.entity.Movie;
import com.example.pixels.entity.Review;
import com.example.pixels.entity.ReviewAlert;
import com.example.pixels.entity.User;
import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.model.ReviewModel;
import com.example.pixels.service.ReviewService;
import com.example.pixels.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping("/review")
public class ReviewController {

    @Autowired
    UserService userService;

    @Autowired
    ReviewService reviewService;

    //    @PreAuthorize("hasRole('USER')")

    @PostMapping("/user/review/{movieId}/addReview")
    public Review addReview(@PathVariable("movieId") Long movieId,
                               @RequestBody ReviewModel reviewModel) throws ItemNotFoundException {

        UserDetails userDetails = userService.getLoggedInUserDetails();

        User user = userService.getUserByEmail(userDetails.getUsername());

//        ReviewDTO reviewDTO = new ReviewDTO();
//        reviewDTO.setReview(reviewService.addReview(movieId, reviewModel, user));
//        if ((user.getPremiumUser() != null)) {
//            reviewDTO.setPremiumUser("PREMIUM USER");
//        } else {
//            reviewDTO.setPremiumUser("");
//        }
//        return reviewDTO;
        return reviewService.addReview(movieId, reviewModel, user);
    }

    @PostMapping("/user/review/{reviewId}/like")
    public Review likeReview(@PathVariable("reviewId") Long reviewId){
        User user = userService.getUserByEmail(userService.getLoggedInUserDetails().getUsername());
        return reviewService.likeReview(reviewId, user);
    }

    @PostMapping("/user/review/{reviewId}/dislike")
    public Review dislikeReview(@PathVariable("reviewId") Long reviewId){
        User user = userService.getUserByEmail(userService.getLoggedInUserDetails().getUsername());
        return reviewService.dislikeReview(reviewId, user);
    }

//    @GetMapping("/byUserName/{userName}")
//    public List<Review> getReviewsByUserName(@PathVariable("userName") String userName)

    @GetMapping("/user/myReviews")
    public List<Review> myReviews(){
        User user = userService.getUserByEmail(userService.getLoggedInUserDetails().getUsername());
        return reviewService.myReviews(user);
    }

    @GetMapping("/review/{movieId}/getReviews")
    public List<Review> getReviews(@PathVariable("movieId") Long movieId) {
        List<Review> reviews = reviewService.getReviews(movieId);
        for(Review review:reviews){
            if(review.getUser()!=null) {
                if (review.getUser().getPremiumUser() != null) {
                    review.setSaveName(review.getSaveName().toUpperCase());
                }
            }
        }
        return reviews;
    }

    @GetMapping("/review/{reviewId}")
    public Review getReviewById(@PathVariable("reviewId") Long reviewId){
        return reviewService.getReviewById(reviewId);
    }



    //Implement cannot edit review after a minute
    @PutMapping("/user/review/{reviewId}")
    public Review editReview(@Valid @PathVariable("reviewId") Long reviewId, @RequestBody ReviewModel reviewModel) throws ItemNotFoundException {
        return reviewService.editReview(reviewId, reviewModel);
    }

    //If we delete a review all likes and comments will be deleted. So I think its better to keep it that way
//    @DeleteMapping("/{reviewId}")
//    public String deleteReviewById(@PathVariable("reviewId") Long reviewId){
//        return reviewService.deleteReviewById(reviewId);
//    }

}
