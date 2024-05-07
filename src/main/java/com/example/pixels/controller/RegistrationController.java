package com.example.pixels.controller;

import com.example.pixels.config.EmailService;
import com.example.pixels.entity.User;
import com.example.pixels.entity.VerificationToken;
import com.example.pixels.event.RegistrationCompleteEvent;
import com.example.pixels.model.CriticModel;
import com.example.pixels.model.PasswordModel;
import com.example.pixels.model.PaymentDetailsModel;
import com.example.pixels.model.UserModel;
import com.example.pixels.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.zip.DataFormatException;

@Slf4j
@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private EmailService emailService;

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
    public User registerUser(@Valid @RequestBody UserModel userModel, final HttpServletRequest request) throws IllegalAccessException, DataFormatException {
        if(! userModel.getPassword().equals(userModel.getMatchingPassword()))
            throw new DataFormatException("Password not matched.");
        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(user,
                applicationUrl(request)));
        return user;
    }

    @GetMapping("/allCritics")
    public List<User> getAllCritics(){
        return userService.getAllCritics();
    }

    @GetMapping("/allCriticsNames")
    public List<String> getAllCriticNames(){
        return userService.getAllCriticNames();
    }

    @PostMapping("/user/criticRequest")
    public String criticRequest(@RequestBody CriticModel criticModel) throws IllegalAccessException {
        return userService.criticRequest(criticModel);
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<Object> editUser(@Valid @PathVariable("userId") Long userId, @RequestBody UserModel userModel){
        if(! userModel.getPassword().equals(userModel.getMatchingPassword()))
            return new ResponseEntity<>("Password not matched.", HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(userService.editUser(userId, userModel), HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}")
    public String deleteUserById(@PathVariable("userId") Long userId){
        return userService.deleteUserById(userId);
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

        emailService.sendSimpleMessage(user.getUserEmail(), "Please verify your account.","Thank you for creating an account with us," +
                " Please verify your account by clicking below link:\n\n"+ url);
//        log.info("Click the link to verify your account: {}", url);
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

        emailService.sendSimpleMessage(user.getUserEmail(), "Reset your Password.","Click the below link to Reset your password:\n\n"+ url);

//        log.info("Click the link to Reset your password: {}", url);
        return url;
    }

    private String applicationUrl(HttpServletRequest request){
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
