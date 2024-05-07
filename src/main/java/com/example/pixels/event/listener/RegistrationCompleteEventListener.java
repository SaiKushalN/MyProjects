package com.example.pixels.event.listener;

import com.example.pixels.config.EmailService;
import com.example.pixels.entity.User;
import com.example.pixels.event.RegistrationCompleteEvent;
import com.example.pixels.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {


    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user);

        //Sending email
        String url = event.getApplicationUrl() + "/verifyRegistration?token="+token;

        //sendverificationemail
//        log.info("Click the link to verify your account: {}", url);
        emailService.sendSimpleMessage(user.getUserEmail(), "Please verify your account.","Thank you for creating an account with us," +
                " Please verify your account by clicking below link:\n\n"+ url);
    }
}
