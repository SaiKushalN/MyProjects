package com.example.pixels.entity;


import com.example.pixels.dto.IdOnlySerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.*;


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

//    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Comment> reviewComments;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonSerialize(using = IdOnlySerializer.class)
    private User user;

    private Long likesCount=0L;

    private Long dislikesCount=0L;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    @JsonSerialize(using = IdOnlySerializer.class)
    private Movie movie;

    @ManyToMany
    @JoinTable(
            name = "review_likes",
            joinColumns = @JoinColumn(name = "review_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonSerialize(contentUsing = IdOnlySerializer.class)
    private Set<User> likedByUsers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "review_dislikes",
            joinColumns = @JoinColumn(name = "review_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonSerialize(contentUsing = IdOnlySerializer.class)
    private Set<User> dislikedByUsers = new HashSet<>();
}
