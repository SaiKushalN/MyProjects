package com.example.pixels.repository;

import com.example.pixels.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    Optional<List<Comment>> findALlByReviewId(Long reviewId);

    Optional<List<Comment>> findAllByUserName(String userEmail);
}
