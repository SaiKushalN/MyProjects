package com.example.pixels.service;

import com.example.pixels.entity.Comment;
import com.example.pixels.entity.User;
import com.example.pixels.model.CommentModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    List<Comment> getAllCommentsOfReview(Long reviewId);

    Comment getCommentById(Long commentId);

    Comment addComment(Long reviewId, CommentModel commentModel);

    Comment likeComment(Long commentId, User user);

    Comment dislikeComment(Long commentId, User user);

    List<Comment> myComments(User user);
}
