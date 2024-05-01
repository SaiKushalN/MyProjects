package com.example.pixels.repository;

import com.example.pixels.entity.ReviewDislike;
import com.example.pixels.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewDislikeRepository extends JpaRepository<ReviewDislike, Long> {
    Optional<ReviewDislike> findByUserNameAndReviewId(String fullName, Long reviewId);

    List<ReviewDislike> findAllByReviewId(Long reviewId);
}
