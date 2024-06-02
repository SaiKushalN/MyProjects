package com.example.pixels.repository;

import com.example.pixels.entity.CommentDislike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentDislikeRepository extends JpaRepository<CommentDislike, Long> {
    Optional<CommentDislike> findByUserNameAndCommentId(String userEmail, Long commentId);
}
