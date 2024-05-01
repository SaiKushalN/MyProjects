package com.example.pixels.service.impl;

import com.example.pixels.entity.*;
import com.example.pixels.model.CommentModel;
import com.example.pixels.repository.CommentDislikeRepository;
import com.example.pixels.repository.CommentLikeRepository;
import com.example.pixels.repository.CommentRepository;
import com.example.pixels.service.CommentService;
import com.example.pixels.service.ReviewService;
import com.example.pixels.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ReviewService reviewService;

    @Autowired
    UserService userService;

    @Autowired
    CommentLikeRepository commentLikeRepository;

    @Autowired
    CommentDislikeRepository commentDislikeRepository;

    @Override
    public List<Comment> getAllCommentsOfReview(Long reviewId) {
        Optional<List<Comment>> comments = commentRepository.findALlByReviewId(reviewId);
        if(comments.isEmpty())
            throw new NoSuchElementException("No comments found for this review.");
        return comments.get();
    }

    @Override
    public Comment getCommentById(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isEmpty())
            throw new NoSuchElementException("Comment not found.");
        return comment.get();
    }

    @Override
    public Comment addComment(Long reviewId, CommentModel commentModel) {
        UserDetails userDetails = userService.getLoggedInUserDetails();
        Review review = reviewService.getReviewById(reviewId);
        User user = userService.getUserByEmail(userDetails.getUsername());
        Comment comment = new Comment();
        comment.setCommentDescription(commentModel.getCommentDescription());
        comment.setUserName(user.getUserEmail());
        comment.setUser(user);
        comment.setReview(review);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment likeComment(Long commentId, User user) {
        Comment comment = getCommentById(commentId);
        Optional<CommentLike> commentLikePrevious =
                commentLikeRepository.findByUserNameAndCommentId(user.getUserEmail(), commentId);

        Optional<CommentDislike> commentDislikePrevious =
                commentDislikeRepository.findByUserNameAndCommentId(user.getUserEmail(), commentId);

        if(commentDislikePrevious.isPresent()){
            commentDislikeRepository.delete(commentDislikePrevious.get());
            comment.decrementDislikesCount();
        }

        if(commentLikePrevious.isEmpty()) {
            CommentLike commentLike = new CommentLike();
            commentLike.setUserName(user.getUserEmail());
            commentLike.setComment(comment);
            commentLikeRepository.save(commentLike);
            comment.incrementLikesCount();
            return commentRepository.save(comment);
        }

        return comment;
    }

    @Override
    @Transactional
    public Comment dislikeComment(Long commentId, User user) {
        Comment comment = getCommentById(commentId);

        Optional<CommentDislike> commentDislikePrevious =
                commentDislikeRepository.findByUserNameAndCommentId(user.getUserEmail(), commentId);

        Optional<CommentLike> commentLikePrevious =
                commentLikeRepository.findByUserNameAndCommentId(user.getUserEmail(), commentId);

        if(commentLikePrevious.isPresent()){
            commentLikeRepository.delete(commentLikePrevious.get());
            comment.decrementLikesCount();
        }

        if(commentDislikePrevious.isEmpty()) {
            CommentDislike commentDislike = new CommentDislike();
            commentDislike.setUserName(user.getUserEmail());
            commentDislike.setComment(comment);
            commentDislikeRepository.save(commentDislike);
            comment.incrementDislikesCount();
            return commentRepository.save(comment);
        }

        return comment;
    }

    @Override
    public List<Comment> myComments(User user) {
        Optional<List<Comment>> comments = commentRepository.findAllByUserName(user.getUserEmail());
        if(comments.isEmpty())
            throw new NoSuchElementException("You have no Comments");
        return comments.get();
    }
}
