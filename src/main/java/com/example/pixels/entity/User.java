package com.example.pixels.entity;

import com.example.pixels.dto.IdOnlySerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is mandatory.")
    private String firstName;

    @NotBlank(message = "Last name is mandatory.")
    private String lastName;

    @NotBlank(message = "Email is mandatory.")
    @Column(unique = true)
    private String userEmail;

    @NotBlank(message = "Password is required.")
    @Column(length = 60)
    @JsonIgnore
    private String password;

    private String fullName;

    private String userRole;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY
            , cascade = CascadeType.ALL)
    @JsonSerialize(using = IdOnlySerializer.class)
    private PremiumUser premiumUser;

    private boolean userVerified = false;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonSerialize(contentUsing = IdOnlySerializer.class)
    private List<Review> userReviews;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonSerialize(contentUsing = IdOnlySerializer.class)
    private List<Comment> userComments;
}

