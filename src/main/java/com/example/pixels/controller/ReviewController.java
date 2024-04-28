package com.example.pixels.controller;

import com.example.pixels.entity.Movie;
import com.example.pixels.entity.Review;
import com.example.pixels.entity.User;
import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.model.ReviewModel;
import com.example.pixels.service.MovieService;
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
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    MovieService movieService;

    @Autowired
    UserService userService;

    @Autowired
    ReviewService reviewService;

    @PostMapping("/user/{movieId}/addReview")
//    @PreAuthorize("hasRole('USER')")
    public Review addReview(@Valid @PathVariable("movieId") Long movieId,
                            @RequestBody ReviewModel reviewModel) throws ItemNotFoundException {
        Movie movie =  movieService.getMovieById(movieId);
        UserDetails userDetails = userService.getLoggedInUserDetails();

        User user = userService.getUserByEmail(userDetails.getUsername());

        return reviewService.addReview(movie, reviewModel, user);
    }

    @PostMapping("/user/{reviewId}/like")
    public Review likeReview(@PathVariable("reviewId") Long reviewId){
        User user = userService.getUserByEmail(userService.getLoggedInUserDetails().getUsername());
        return reviewService.likeReview(reviewId, user);
    }

    @PostMapping("/user/{reviewId}/dislike")
    public Review dislikeReview(@PathVariable("reviewId") Long reviewId){
        User user = userService.getUserByEmail(userService.getLoggedInUserDetails().getUsername());
        return reviewService.dislikeReview(reviewId, user);
    }

//    @GetMapping("/byUserName/{userName}")
//    public List<Review> getReviewsByUserName(@PathVariable("userName") String userName)

    @GetMapping("/{movieId}/getReviews")
    public List<Review> getReviews(@PathVariable("movieId") Long movieId) {
        return reviewService.getReviews(movieId);
    }

    @GetMapping("/{reviewId}")
    public Review getReviewById(@PathVariable("reviewId") Long reviewId){
        return reviewService.getReviewById(reviewId);
    }

    @PutMapping("/{reviewId}")
    public Review editReview(@Valid @PathVariable("reviewId") Long reviewId, @RequestBody ReviewModel reviewModel) throws ItemNotFoundException {
        return reviewService.editReview(reviewId, reviewModel);
    }

    //If we delete a review all likes and comments will be deleted. So I think its better to keep it that way
//    @DeleteMapping("/{reviewId}")
//    public String deleteReviewById(@PathVariable("reviewId") Long reviewId){
//        return reviewService.deleteReviewById(reviewId);
//    }

    public String getCurrentUserEmailUsingPrincipal(Principal principal) {
        return principal.getName(); // `getName()` will return the username (typically the email in your case).
    }
}
