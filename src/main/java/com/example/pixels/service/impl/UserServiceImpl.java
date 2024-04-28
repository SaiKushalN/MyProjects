package com.example.pixels.service.impl;

import com.example.pixels.entity.PasswordResetToken;
import com.example.pixels.entity.Review;
import com.example.pixels.entity.User;
import com.example.pixels.entity.VerificationToken;
import com.example.pixels.model.UserModel;
import com.example.pixels.repository.PasswordResetTokenRepository;
import com.example.pixels.repository.UserRepository;
import com.example.pixels.repository.VerificationTokenRepository;
import com.example.pixels.service.UserService;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

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
//        user.setPassword(userModel.getPassword());

        userRepository.save(user);
        return user;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken==null)
            return "invalid";

        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if((verificationToken.getExpirationTime().getTime())-(calendar.getTime().getTime())<=0)
        {
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }
        user.setUserVerified(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public VerificationToken generateNewToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user,token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if(passwordResetToken==null)
            return "invalid";

        User user = passwordResetToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if((passwordResetToken.getExpirationTime().getTime())-(calendar.getTime().getTime())<=0)
        {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";
        }
        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public UserDetails getLoggedInUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails){
            return (UserDetails) authentication.getPrincipal();
        }
        return null;
    }

    @Override
    public User getUserById(Long userId){
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty())
            throw new NoSuchElementException("User does not exist.");
        UserDetails currentUserDetails = getLoggedInUserDetails();
        User currentUser = getUserByEmail(currentUserDetails.getUsername());
        if(!(currentUser.getUserEmail()).equals(user.get().getUserEmail()) && !(currentUser.getUserRole()).contains("ADMIN"))
            throw new ForbiddenException("Cannot access other user details.");
        return user.get();
    }

    @Override
    public User editUser(Long userId, UserModel userModel) {
        User user = getUserById(userId);
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setUserEmail(userModel.getUserEmail());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public String getPremiumSubscription(Integer months) {
        User user = getUserByEmail(getLoggedInUserDetails().getUsername());
        if((user.getSubscription()).equals("Premium"))
            return "You are already a premium member. Thank you for choosing Premium subscription.";
        user.setSubscription("Premium");
        user.setSubStartDate(LocalDate.now());
        LocalDate endDate = LocalDate.now().plusMonths(months);
        user.setSubEndDate(endDate);
        userRepository.save(user);
        return "You are a Premium member now. Thank you for choosing Premium subscription. Your membership will expire on "+endDate+".";
    }

    @Override
    public String deleteUserById(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
        return "User deleted successfully.";
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findUserByUserEmail(email));
        if(user.isPresent())
            return user.get();
        else
            throw new NoSuchElementException("User not found.");
    }
}
