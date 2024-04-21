package com.example.pixels.service.impl;

import com.example.pixels.entity.User;
import com.example.pixels.entity.VerificationToken;
import com.example.pixels.model.UserModel;
import com.example.pixels.repository.UserRepository;
import com.example.pixels.repository.VerificationTokenRepository;
import com.example.pixels.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setUserEmail(userModel.getUserEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setUserRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));

        userRepository.save(user);
        return user;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }
}
