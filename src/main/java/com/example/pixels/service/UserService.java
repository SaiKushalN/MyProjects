package com.example.pixels.service;

import com.example.pixels.entity.User;
import com.example.pixels.model.UserModel;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);
}
