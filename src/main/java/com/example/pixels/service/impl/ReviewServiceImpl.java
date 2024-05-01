package com.example.pixels.service.impl;

import com.example.pixels.entity.*;
import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.model.ReviewModel;
import com.example.pixels.repository.ReviewDislikeRepository;
import com.example.pixels.repository.ReviewLikeRepository;
import com.example.pixels.repository.ReviewRepository;
import com.example.pixels.service.MovieService;
import com.example.pixels.service.ReviewService;
import com.example.pixels.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalDouble;

@Component
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ReviewLikeRepository reviewLikeRepository;

    @Autowired
    ReviewDislikeRepository reviewDislikeRepository;

    @Autowired
    UserService userService;

    @Autowired
    MovieService movieService;
    @Override
    public Review addReview(Movie movie, ReviewModel reviewModel, User user) {

        Review review = new Review();
        review.setRatingPoints(reviewModel.getRatingPoints());
        review.setReviewDescription(reviewModel.getReviewDescription());
        review.setSuggest(reviewModel.getSuggest());
        review.setChildSafety(reviewModel.getChildSafety());
        review.setUserName(user.getUserEmail());
        review.setUser(user);
        review.setMovie(movie);
        review.setUserName(user.getFirstName()+" "+user.getLastName());

        if (reviewRepository.existsByUserNameAndMovieId(user.getUserEmail(), movie.getId())) {
            throw new IllegalStateException("A review from this user for this movie already exists.");
        }
        else {
            reviewRepository.save(review);
            sumOfRatings(movie.getId());
            return review;
        }
    }

    @Override
    public Review getReviewById(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if(review.isPresent())
            return review.get();
        else
            throw new NoSuchElementException("Review does not exists");
    }

    @Override
    public List<Review> getReviews(Long movieId) {
        return reviewRepository.findAllByMovieId(movieId);
    }

    @Transactional
    public void sumOfRatings(Long movieId) {
        List<Review> reviews = reviewRepository.findAllByMovieId(movieId);
        Movie movie = movieService.getMovieById(movieId);
        OptionalDouble averageRating = reviews.stream()
                .mapToDouble(Review::getRatingPoints)
                .average();
        if (averageRating.isPresent()) {
            movie.setMovieRating(averageRating.getAsDouble());
            movieService.saveMovieEntity(movie);
        }
    }

    @Override
    @Transactional
    public Review likeReview(Long reviewId, User user) {
        Review review = getReviewById(reviewId);
        Optional<ReviewLike> reviewLikePrevious =
                reviewLikeRepository.findByUserNameAndReviewId(user.getUserEmail(), reviewId);

        Optional<ReviewDislike> reviewDislikePrevious =
                reviewDislikeRepository.findByUserNameAndReviewId(user.getUserEmail(), reviewId);

        if(reviewDislikePrevious.isPresent()){
            reviewDislikeRepository.delete(reviewDislikePrevious.get());
            review.decrementDislikesCount();

        }

        if(reviewLikePrevious.isEmpty()) {
            ReviewLike reviewLike = new ReviewLike();
            reviewLike.setUserName(user.getUserEmail());
            reviewLike.setReview(review);
            reviewLikeRepository.save(reviewLike);
            review.incrementLikesCount();
            return reviewRepository.save(review);
        }

        return review;
    }

    @Override
    @Transactional
    public Review dislikeReview(Long reviewId, User user) {
        Review review = getReviewById(reviewId);
        Optional<ReviewDislike> reviewDislikePrevious =
                reviewDislikeRepository.findByUserNameAndReviewId(user.getUserEmail(), reviewId);

        Optional<ReviewLike> reviewLikePrevious =
                reviewLikeRepository.findByUserNameAndReviewId(user.getUserEmail(), reviewId);

        if(reviewLikePrevious.isPresent()){
            reviewLikeRepository.delete(reviewLikePrevious.get());
            review.decrementLikesCount();
        }

        if(reviewDislikePrevious.isEmpty()) {
            ReviewDislike reviewDislike = new ReviewDislike();
            reviewDislike.setUserName(user.getUserEmail());
            reviewDislike.setReview(review);
            reviewDislikeRepository.save(reviewDislike);
            review.incrementDislikesCount();
            return reviewRepository.save(review);
        }

        return review;
    }

    @Override
    public String deleteReviewById(Long reviewId) {
        Review review = getReviewById(reviewId);
        reviewRepository.delete(review);
        sumOfRatings(review.getMovie().getId());
        return "Review deleted successfully";
    }

    @Override
    public Review editReview(Long reviewId, ReviewModel reviewModel) {
        Review review = getReviewById(reviewId);
        UserDetails userDetails = userService.getLoggedInUserDetails();
        if(!(review.getUserName()).equals(userDetails.getUsername()))
            throw new ForbiddenException("Cannot edit other user review.");
//        if(!(review.getReviewComments().isEmpty() && review.getLikesCount()==0 && review.getDislikesCount()==0))
        review.setRatingPoints(reviewModel.getRatingPoints());
        review.setReviewDescription(reviewModel.getReviewDescription());
        review.setSuggest(reviewModel.getSuggest());
        review.setChildSafety(reviewModel.getChildSafety());
        Long movieId = review.getMovie().getId();
        reviewRepository.save(review);
        sumOfRatings(movieId);
        return review;
    }

    @Override
    public List<Review> myReviews(User user) {
        Optional<List<Review>> reviews = reviewRepository.findAllByUserName(user.getUserEmail());
        if(reviews.isEmpty())
            throw new NoSuchElementException("You have no Reviews");
        return reviews.get();
    }
}