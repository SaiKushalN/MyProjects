package com.example.pixels.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
//@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @NotNull(message = "Rating points is mandatory.")
    private Double ratingPoints;

    private String reviewDescription;

    private Boolean suggest;

    private String childSafety;

    @OneToMany(mappedBy = "review")
    private List<Comment> reviewComments;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long likesCount;

    private Long dislikesCount;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
}
