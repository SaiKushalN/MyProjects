package com.example.pixels.entity;

import com.example.pixels.dto.IdOnlySerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;





@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Rating points is mandatory.")
    private Double ratingPoints;

    private String reviewDescription;

    private Boolean suggest;

    private String childSafety;

    private String userName;

    private Long likesCount = 0L;

    private Long dislikesCount = 0L;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonSerialize(contentUsing = IdOnlySerializer.class)
    private List<Comment> reviewComments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    @JsonSerialize(using = IdOnlySerializer.class)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    @JsonSerialize(using = IdOnlySerializer.class)
    private Movie movie;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonSerialize(contentUsing = IdOnlySerializer.class)
    private List<ReviewLike> reviewLikes;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonSerialize(contentUsing = IdOnlySerializer.class)
    private List<ReviewDislike> reviewDisLikes;

    public void incrementLikesCount() {
        this.likesCount += 1;
    }

    public void decrementLikesCount() {
        if (this.likesCount > 0) {
            this.likesCount -= 1;
        }
    }

    public void incrementDislikesCount() {
        this.dislikesCount += 1;
    }

    public void decrementDislikesCount() {
        if (this.dislikesCount > 0) {
            this.dislikesCount -= 1;
        }
    }

}
