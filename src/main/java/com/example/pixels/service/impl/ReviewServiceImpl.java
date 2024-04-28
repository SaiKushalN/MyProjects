package com.example.pixels.service.impl;

import com.example.pixels.entity.Movie;
import com.example.pixels.entity.Review;
import com.example.pixels.entity.User;
import com.example.pixels.error.ItemNotFoundException;
import com.example.pixels.model.ReviewModel;
import com.example.pixels.repository.ReviewRepository;
import com.example.pixels.service.MovieService;
import com.example.pixels.service.ReviewService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalDouble;

@Component
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    MovieService movieService;
    @Override
    public Review addReview(Movie movie, ReviewModel reviewModel, User user) throws ItemNotFoundException {

        Review review = new Review();
        review.setRatingPoints(reviewModel.getRatingPoints());
        review.setReviewDescription(reviewModel.getReviewDescription());
        review.setSuggest(reviewModel.getSuggest());
        review.setChildSafety(reviewModel.getChildSafety());
        review.setUser(user);
        review.setMovie(movie);

        if (reviewRepository.existsByUserIdAndMovieId(user.getId(), movie.getId())) {
            throw new IllegalStateException("A review from this user for this movie already exists.");
        }
        else {
            sumOfRatings(movie.getId());
            return reviewRepository.save(review);
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
    public void sumOfRatings(Long movieId) throws ItemNotFoundException {
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
    public Review likeReview(Long reviewId, User user) {
        Review review = getReviewById(reviewId);
//        review.setLikesCount(review.getLikesCount()+1);

        if (review.getDislikedByUsers().contains(user)) {
            review.getDislikedByUsers().remove(user);
            review.setDislikesCount(review.getDislikesCount() - 1);
        }

        boolean added = review.getLikedByUsers().add(user);
        if (added) {
            review.setLikesCount(review.getLikesCount() + 1);
        }
        return reviewRepository.save(review);
    }

    @Override
    public Review dislikeReview(Long reviewId, User user) {
        Review review = getReviewById(reviewId);
//        review.setDislikesCount(review.getDislikesCount()+1);

        if (review.getLikedByUsers().contains(user)) {
            review.getLikedByUsers().remove(user);
            review.setLikesCount(review.getLikesCount() - 1);
        }

        boolean added = review.getDislikedByUsers().add(user);
        if (added) {
            review.setDislikesCount(review.getDislikesCount() + 1);
        }

        return reviewRepository.save(review);
    }

    @Override
    public String deleteReviewById(Long reviewId) {
        Review review = getReviewById(reviewId);
        reviewRepository.delete(review);
        return "Review deleted successfully";
    }

    @Override
    public Review editReview(Long reviewId, ReviewModel reviewModel) throws ItemNotFoundException {
        Review review = getReviewById(reviewId);
        review.setRatingPoints(reviewModel.getRatingPoints());
        review.setReviewDescription(reviewModel.getReviewDescription());
        review.setSuggest(reviewModel.getSuggest());
        review.setChildSafety(reviewModel.getChildSafety());
        Long movieId = review.getMovie().getId();
        sumOfRatings(movieId);
        reviewRepository.save(review);
        return review;
    }

}




//    how can I do this in entity level?
//
//        ALTER TABLE reviews
//        ADD CONSTRAINT unique_user_movie_review UNIQUE (user_id, movie_id);
//
//
//        package com.example.pixels.entity;
//
//        import jakarta.persistence.*;
//        import jakarta.validation.constraints.NotNull;
//        import lombok.AllArgsConstructor;
//        import lombok.Builder;
//        import lombok.Data;
//        import lombok.NoArgsConstructor;
//        import java.util.List;
//        import com.fasterxml.jackson.annotation.*;
//
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Data
//@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
//public class Review {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotNull(message = "Rating points is mandatory.")
//    private Double ratingPoints;
//
//    private String reviewDescription;
//
//    private Boolean suggest;
//
//    private String childSafety;
//
////    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
////    private List<Comment> reviewComments;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    private Long likesCount=0L;
//
//    private Long dislikesCount=0L;
//
//    @ManyToOne
//    @JoinColumn(name = "movie_id")
//    private Movie movie;
//}


