package com.example.pixels.entity;

import com.example.pixels.dto.IdOnlySerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;


@Data
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

    private String fullName = firstName+" "+lastName;

    private String userRole;

    private String subscription = "Free";

    private LocalDate subStartDate = null;

    private LocalDate subEndDate = null;

    private boolean userVerified = false;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonSerialize(contentUsing = IdOnlySerializer.class)
    private List<Review> userReviews;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonSerialize(contentUsing = IdOnlySerializer.class)
    private List<Comment> userComments;
}

