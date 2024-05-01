package com.example.pixels.controller;

import com.example.pixels.entity.Comment;
import com.example.pixels.entity.Review;
import com.example.pixels.entity.User;
import com.example.pixels.model.CommentModel;
import com.example.pixels.service.CommentService;
import com.example.pixels.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @GetMapping("/comment/{reviewId}/all")
    public List<Comment> getAllCommentsOfReview(@PathVariable("reviewId") Long reviewId){
        return commentService.getAllCommentsOfReview(reviewId);
    }

    @GetMapping("/comment/{commentId}")
    public Comment getCommentById(@PathVariable("commentId") Long commentId){
        return commentService.getCommentById(commentId);
    }

    @GetMapping("/user/myComments")
    public List<Comment> myComments(){
        User user = userService.getUserByEmail(userService.getLoggedInUserDetails().getUsername());
        return commentService.myComments(user);
    }

    @PostMapping("/user/comment/{reviewId}/addComment")
    public Comment addComment(@Valid @PathVariable("reviewId") Long reviewId, @RequestBody CommentModel commentModel){
        return commentService.addComment(reviewId,commentModel);
    }

    @PostMapping("/user/comment/{commentId}/like")
    public Comment likeComment(@PathVariable("commentId") Long commentId){
        User user = userService.getUserByEmail(userService.getLoggedInUserDetails().getUsername());
        return commentService.likeComment(commentId, user);
    }

    @PostMapping("/user/comment/{commentId}/dislike")
    public Comment dislikeComment(@PathVariable("commentId") Long commentId){
        User user = userService.getUserByEmail(userService.getLoggedInUserDetails().getUsername());
        return commentService.dislikeComment(commentId, user);
    }
}
