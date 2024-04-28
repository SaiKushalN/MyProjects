package com.example.pixels.controller;

import com.example.pixels.entity.User;
import com.example.pixels.entity.VerificationToken;
import com.example.pixels.event.RegistrationCompleteEvent;
import com.example.pixels.model.PasswordModel;
import com.example.pixels.model.UserModel;
import com.example.pixels.service.UserService;
import io.micrometer.core.instrument.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.ws.rs.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping("/user/details")
    public UserDetails getCurrentUserUsingAuthentication() {
        return userService.getLoggedInUserDetails();
    }

    @GetMapping("/user/{userId}")
    public User getUserById(@PathVariable("userId") Long userId){
        return userService.getUserById(userId);
    }

    @GetMapping("/register")
    public String getForRegister(){
        return "No get for register.";
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request){
        if(! userModel.getPassword().equals(userModel.getMatchingPassword()))
            return "Password not matched.";
        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(user,
                applicationUrl(request)));
        return "Success";
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> editUser(@PathVariable("userId") Long userId, @RequestBody UserModel userModel){
        if(! userModel.getPassword().equals(userModel.getMatchingPassword()))
            return new ResponseEntity<>("Password not matched.", HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(userService.editUser(userId, userModel), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public String deleteUserById(@PathVariable("userId") Long userId){
        return userService.deleteUserById(userId);
    }

    @PostMapping("/user/getPremiumSubscription/{months}")
    public String getPremium(@PathVariable("months") Integer months){
        Set<Integer> validMonths = Set.of(1, 3, 6, 12);
        if(!validMonths.contains(months))
            return "Please select available plan.";
        return userService.getPremiumSubscription(months);
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token){
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")){
            return "User verified.";
        }
        else if(result.equalsIgnoreCase("expired")){
            return "Token expired.";
        }
        else return "Bad user.";
    }

    @GetMapping("/resendVerifyToken")
    public String resendVerifyToken(@RequestParam("token") String oldToken,
                                    HttpServletRequest request){
        VerificationToken verificationToken = userService.generateNewToken(oldToken);
        User user =verificationToken.getUser();
        resendVerifyTokenMail(user, applicationUrl(request), verificationToken);
        return "Verification link sent.";
    }

    private void resendVerifyTokenMail(User user, String applicationUrl, VerificationToken verificationToken){
        String url = applicationUrl + "/verifyRegistration?token="+verificationToken.getToken();

        log.info("Click the link to verify your account: {}", url);
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request){
        User user = userService.getUserByEmail(passwordModel.getEmail());
        String url = "";
        if(user != null){
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            url = passwordResetTokenMail(user, applicationUrl(request), token);
        }
        return url;
//        return "Verification link sent.";
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel ) {
        String result = userService.validatePasswordResetToken(token);
        if(!result.equalsIgnoreCase("valid")) {
            return "Invalid token";
        }
        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if(user.isPresent()){
            userService.changePassword(user.get(), passwordModel.getNewPassword());
            return "Password reset successfully.";
        }
        else {
            return "Invalid token.";
        }
    }

    private String passwordResetTokenMail(User user, String applicationUrl, String token) {
        String url = applicationUrl + "/savePassword?token="+token;

        log.info("Click the link to Reset your password: {}", url);
        return url;
    }

    private String applicationUrl(HttpServletRequest request){
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
