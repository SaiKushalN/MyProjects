package com.example.pixels.repository;

import com.example.pixels.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByMovieId(Long movieId);

//    boolean existsByUserName(String userName);

    boolean existsByUserNameAndMovieId(String userName, Long movieId);

    Optional<List<Review>> findAllByUserName(String userEmail);
}
