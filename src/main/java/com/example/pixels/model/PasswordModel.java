package com.example.pixels.model;

import lombok.Data;

@Data
public class PasswordModel {
    private String email;
    private String newPassword;
    private String matchPassword;
}
