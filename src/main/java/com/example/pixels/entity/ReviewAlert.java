package com.example.pixels.entity;

import com.example.pixels.dto.IdOnlySerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ReviewAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = true)
    @JsonSerialize(using = IdOnlySerializer.class)
    private Movie movie;

    private String userType;

    private String criticName;

    private String alertType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "premium_user_id")
    @JsonSerialize(using = IdOnlySerializer.class)
    private PremiumUser premiumUser;

}
