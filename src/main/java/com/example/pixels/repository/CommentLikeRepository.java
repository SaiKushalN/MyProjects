package com.example.pixels.repository;

import com.example.pixels.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByUserNameAndCommentId(String userEmail, Long commentId);
}
