package com.example.pixels.repository;

import com.example.pixels.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByMovieId(Long movieId);

    boolean existsByUserIdAndMovieId(Long userId, Long movieId);
}
