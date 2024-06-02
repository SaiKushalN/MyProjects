package com.example.pixels.repository;

import com.example.pixels.entity.Review;
import com.example.pixels.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike,Long> {

    List<ReviewLike> findAllByReviewId(Long reviewId);

    Optional<ReviewLike> findByUserNameAndReviewId(String userName, Long reviewId);
}
