package com.example.pixels.entity;

import com.example.pixels.dto.IdOnlySerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Comment is mandatory")
    private String commentDescription;

    private Long likesCount = 0L;

    private Long dislikeCount = 0L;

    private String userName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    @JsonSerialize(using = IdOnlySerializer.class)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    @JsonSerialize(using = IdOnlySerializer.class)
    private User user;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonSerialize(contentUsing = IdOnlySerializer.class)
    private List<CommentLike> commentLikes;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonSerialize(contentUsing = IdOnlySerializer.class)
    private List<CommentDislike> commentDisLikes;

    public void incrementLikesCount() {
        this.likesCount += 1;
    }

    public void decrementLikesCount() {
        if (this.likesCount > 0) {
            this.likesCount -= 1;
        }
    }

    public void incrementDislikesCount() {
        this.dislikeCount += 1;
    }

    public void decrementDislikesCount() {
        if (this.dislikeCount > 0) {
            this.dislikeCount -= 1;
        }
    }
}
