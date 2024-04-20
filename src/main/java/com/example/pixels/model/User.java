package com.example.pixels.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "First name is mandatory.")
    private String firstName;

    @NotBlank(message = "Last name is mandatory.")
    private String lastName;

    @NotBlank(message = "Email is mandatory.")
    private String userEmail;

    @NotNull(message = "User address is mandatory.")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "addressId")
    private Address userAddress;

    private String userRole = "User";

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private String subscription = "Free";

    private LocalDate subStartDate = null;

    private LocalDate subEndDate = null;

    private Boolean userVerified = Boolean.FALSE;

    @OneToMany(mappedBy = "reviewUser")
    private List<Review> userReviews;

    @OneToMany(mappedBy = "user")
    private List<Comment> userComments;
}

