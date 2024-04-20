package com.example.pixels.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

//    @ManyToOne
//    @JoinColumn(name = "parent_comment_id")
//    private Comment parentComment;

    @NotBlank(message = "Comment is mandatory")
    private String commentDescription;

    private Long commentLikes = 0L;

    private Long commentDislikes = 0L;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
