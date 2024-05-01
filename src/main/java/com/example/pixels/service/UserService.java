package com.example.pixels.service;

import com.example.pixels.entity.User;
import com.example.pixels.entity.VerificationToken;
import com.example.pixels.model.UserModel;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Optional;

public interface UserService {
    User registerUser(UserModel userModel) throws IllegalAccessException;

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);

    VerificationToken generateNewToken(String oldToken);

    User getUserByEmail(String email);

    void createPasswordResetTokenForUser(User user, String token);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);

    UserDetails getLoggedInUserDetails();

    String deleteUserById(Long userId);

    User getUserById(Long userId);

    User editUser(Long userId, UserModel userModel);

    String getPremiumSubscription(Integer months);
}
