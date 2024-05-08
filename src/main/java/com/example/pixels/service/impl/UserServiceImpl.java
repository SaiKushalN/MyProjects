package com.example.pixels.service.impl;

import com.example.pixels.config.EmailService;
import com.example.pixels.config.OurUserInfoDetailsService;
import com.example.pixels.entity.*;
import com.example.pixels.model.CriticModel;
import com.example.pixels.model.UserModel;
import com.example.pixels.repository.PasswordResetTokenRepository;
import com.example.pixels.repository.UserRepository;
import com.example.pixels.repository.VerificationTokenRepository;
import com.example.pixels.service.PremiumPlanService;
import com.example.pixels.service.PremiumService;
import com.example.pixels.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    //validateUser

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private PremiumService premiumService;

    @Autowired
    OurUserInfoDetailsService ourUserInfoDetailsService;

    @Autowired
    PremiumPlanService premiumPlanService;

    @Override
    public User registerUser(UserModel userModel) throws IllegalAccessException {
        if(getLoggedInUserDetails() != null)
            throw new IllegalAccessException("Please logout to register.");
        Optional<User> userFromDb = userRepository.findUserByUserEmail(userModel.getUserEmail());
        if(userFromDb.isPresent())
            throw new DataIntegrityViolationException("User already exists.");

        User user = new User();
        user.setUserEmail(userModel.getUserEmail());
        user.setFirstName(capitalizeName(userModel.getFirstName()));
        user.setLastName(capitalizeName(userModel.getLastName()));
        user.setFullName(user.getFirstName() +" "+ user.getLastName());
        user.setUserRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));

        userRepository.save(user);
        return user;
    }

    private String capitalizeName(String name) {
        if (name == null || name.isEmpty()) {
            return name; // or return an empty string as per your requirement
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    @Override
    public UserDetails getLoggedInUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails){
            return (UserDetails) authentication.getPrincipal();
        }
        return null;
    }

    public User getLoggedInUser() {
        UserDetails userDetails = getLoggedInUserDetails();
        if(userDetails == null)
            throw new NullPointerException("Please login to continue.");
        return getUserByEmail(userDetails.getUsername());
    }

    @Override
    public List<User> getAllCritics() {
        Optional<List<User>> users = userRepository.findAllByUserRoleContains("CRITIC");
        if(users.isEmpty())
            throw new NoSuchElementException("No Critics found.");
        return users.get();
    }

    @Override
    public List<String> getAllCriticNames() {
        Optional<List<String>> criticNames = Optional.ofNullable(userRepository.findAllCriticFullNames());
        if(criticNames.isEmpty())
            throw new NoSuchElementException("No Critics found.");
        return criticNames.get();
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


    //Test this
    @Override
    @Transactional
    public String getPremiumSubscription(String planName, int discountRate) {
        User user = getUserByEmail(getLoggedInUserDetails().getUsername());
//        PremiumUser premiumUserFromDb = premiumService.getPremiumUser(user);

        if (user.getPremiumUser() != null) {
            return "You are already a premium member. Thank you for choosing Premium subscription.";
        }

        PremiumPlan premiumPlan = premiumPlanService.findByPlanNameIgnoreCase(planName);

        //No payment page for critic 3 months

        PremiumUser premiumUser = new PremiumUser();
        premiumUser.setSubStartDate(LocalDate.now());
        LocalDate endDate = LocalDate.now().plusMonths(premiumPlan.getPlanMonths());
        premiumUser.setSubEndDate(endDate);
        premiumUser.setSubscriptionType(premiumPlan.getPlanName());
        premiumUser.setUser(user);
        premiumUser = premiumService.savePremiumUser(premiumUser);

        user.setPremiumUser(premiumUser);
        userRepository.save(user);

        String message = "";

        if(discountRate!=0)
        {
            double originalPrice = premiumPlan.getPlanPrice();
            double discountAmount = originalPrice * discountRate / 100.0;
            double discountedPrice = originalPrice - discountAmount;

             message = "Original price: " + premiumPlan.getPlanPrice() +
                    " , but with the discount code provided, your card is charged with " +
                    discountedPrice + ".\n";
        }

        emailService.sendSimpleMessage(user.getUserEmail(), "Hello Premium Member..!",
                "You are a Premium member now. " + message +
                        "Thank you for choosing Premium subscription. Your membership will expire on " + endDate + ".");
        ourUserInfoDetailsService.refreshUserAuthentication(user.getUserEmail());

        return "You are a Premium member now. Thank you for choosing Premium subscription. Your membership will expire on " + endDate + ".";
    }


    @Override
    @Transactional
    public String criticRequest(CriticModel criticModel) {
        User user = getUserByEmail(getLoggedInUserDetails().getUsername());
        Set<String> criticAssociationNumbers = Set.of("1234","5678","9123","4567");
        if((user.getUserRole()).contains("CRITIC"))
            return "You are already a Critic. Thank you for making our community best.";
        if(criticAssociationNumbers.contains(criticModel.getCriticAssociationNumber()))
        {
            user.setUserRole(user.getUserRole()+","+"CRITIC");
            String message = "Your Critic Association number has been verified. You are now recognized as a critic. " +
                    "Please contribute responsibly to make the Pixels community valuable.";
            //IDEA:TEST
            if(user.getPremiumUser()==null) {
                try {
                    PremiumPlan premiumPlan = premiumPlanService.findByPlanNameIgnoreCase("BASIC 3");
                }catch (NoSuchElementException ex) {
                    premiumPlanService.addBasic3Plan();
                }

                getPremiumSubscription("BASIC 3",0);
                message = "Your Critic Association number has been verified. You are now recognized as a critic. " +
                        "Please contribute responsibly to make the Pixels community valuable.\n\nAnd you got a free 3 months of " +
                        "Premium subscription. Enjoy..!";
            }
            else{
                LocalDate newEndDate = user.getPremiumUser().getSubEndDate().plusMonths(3);
                user.getPremiumUser().setSubEndDate(newEndDate);
                message = "Your Critic Association number has been verified. You are now recognized as a critic. " +
                        "Please contribute responsibly to make the Pixels community valuable.\n\nAnd we can see you are already a premium member, " +
                        "your subscription will be extened by 3 months, so your new end date will be " +
                        newEndDate+". Enjoy..!";
            }

            userRepository.save(user);

            emailService.sendSimpleMessage(user.getUserEmail(),"Congrats your request has been processed."
                    ,message);

            ourUserInfoDetailsService.refreshUserAuthentication(user.getUserEmail());
            return "Your critic request has been successfully processed and verified.";
        }
        return "Invalid Critic Association Number. Please check with your association.";
    }

    @Override
    public String deleteUserById(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "User deleted successfully and Logged out.";
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findUserByUserEmail(email);
        if(user.isPresent())
            return user.get();
        else
            throw new NoSuchElementException("User not found.");
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
        verificationTokenRepository.delete(verificationToken);
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
}
