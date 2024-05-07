package com.example.pixels.repository;

import com.example.pixels.entity.Movie;
import com.example.pixels.entity.PremiumUser;
import com.example.pixels.entity.ReviewAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewAlertRepository extends JpaRepository<ReviewAlert, Long> {
    Optional<ReviewAlert> findByMovieIdAndUserTypeAndCriticNameAndPremiumUserId(Long id, String userType, String criticName, Long premiumUserId);

    Optional<List<ReviewAlert>> findAllByPremiumUserId(Long premiumUserId);

//    List<ReviewAlert> findAllByMovie(Movie movie);

//    List<ReviewAlert> findAllByMovieAndAlertType(Movie movie, String alertType);

//    @Query("SELECT ra FROM ReviewAlert ra WHERE ra.movie.id = :movieId AND " +
//            "(ra.userType = :userType OR ra.userType IS NULL) AND " +
//            "(ra.criticName = :criticName OR ra.criticName IS NULL OR :criticName = '')")
//    List<ReviewAlert> findAlertsForReview(@Param("movieId") Long movieId, @Param("userType") String userType, @Param("criticName") String criticName);


    // I think im getting all review alerts check, ra.usertype means -> database reviewalert. usertype which is given usertype
//    @Query("SELECT ra FROM ReviewAlert ra WHERE ra.movie.id = :movieId " +
//            "AND (ra.userType = :userType OR ra.userType = 'CRITICUSER' OR (:userType = 'USER' AND ra.userType = 'CRITICUSER')) " +
//            "AND (ra.criticName = :criticName OR ra.criticName = 'ANY' OR :criticName = 'ANY')")
//    List<ReviewAlert> findAlertsForReview(@Param("movieId") Long movieId, @Param("userType") String userType, @Param("criticName") String criticName);

    //For critic review
    @Query("SELECT ra FROM ReviewAlert ra WHERE (ra.movie.id =:movieId) AND (ra.alertType = 'MOVIE' " +
            "OR ra.alertType = 'MOVIEANYCRITIC' OR (ra.alertType = 'MOVIEANDNAME' AND ra.criticName = :criticName) ) AND (ra.premiumUser.id != :premiumUserId)")
    List<ReviewAlert> findAlertsForCriticReview(@Param("movieId") Long movieId, @Param("criticName") String criticName, @Param("premiumUserId") Long premiumUserId);

    @Query("SELECT ra FROM ReviewAlert ra WHERE ra.movie.id = :movieId AND ra.alertType = 'MOVIE' AND ra.premiumUser != :premiumUserId")
    List<ReviewAlert> findAlertsForUserReview(@Param("movieId") Long movieId, @Param("premiumUserId") Long premiumUserId);

}
