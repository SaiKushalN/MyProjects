package com.example.pixels.model;

import com.example.pixels.entity.Comment;
import com.example.pixels.entity.Review;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    private String firstName;
    private String lastName;

    private String userEmail;

    private String password;
    private String matchingPassword;
}
