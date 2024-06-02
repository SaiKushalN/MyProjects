package com.example.pixels.controller;

import com.example.pixels.entity.PremiumPlan;
import com.example.pixels.model.PaymentDetailsModel;
import com.example.pixels.model.PremiumPlanEditModel;
import com.example.pixels.model.PremiumPlanModel;
import com.example.pixels.service.PremiumPlanService;
import com.example.pixels.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class PremiumPlanController {

    @Autowired
    PremiumPlanService premiumPlanService;

    @Autowired
    UserService userService;

    @GetMapping("/getAllPremiumPlans")
    public List<PremiumPlan> getAllPremiumPlans(){
        return premiumPlanService.getAllPremiumPlans();
    }

    @PostMapping("/admin/addPremiumPlan")
    public PremiumPlan addPremiumPlan(@Valid @RequestBody PremiumPlanModel premiumPlanModel){
        return premiumPlanService.addPremiumPlan(premiumPlanModel);
    }

    @DeleteMapping("/admin/deletePlan/{premiumPlanId}")
    public String deletePlan(@PathVariable("premiumPlanId") Long premiumPlanId){
        return premiumPlanService.deletePlan(premiumPlanId);
    }

    @PostMapping("/user/getPremiumSubscription/{planName}")
    public String getPremium(@PathVariable("planName") String planName,
                             @RequestParam(required = false, defaultValue = "") String couponCode,
                              @Valid @RequestBody PaymentDetailsModel paymentDetailsModel){
        validatePaymentDetails(paymentDetailsModel);
        int discountRate = calculateDiscountRate(couponCode);
        return userService.getPremiumSubscription(planName, discountRate);
    }

    public void validatePaymentDetails(PaymentDetailsModel paymentDetailsModel) {
        if (paymentDetailsModel.getCardNumber().length() != 16) {
            throw new IllegalArgumentException("Card number must be exactly 16 digits long.");
        }

        if (!paymentDetailsModel.getExpiryDate().matches("\\d{2}/\\d{4}")) {
            throw new IllegalArgumentException("Expiry date must be in the format MM/YYYY.");
        }

        if (!paymentDetailsModel.getSecurityCode().toString().matches("\\d{3,4}")) {
            throw new IllegalArgumentException("Security code must be 3 or 4 digits long.");
        }

        if (paymentDetailsModel.getZipCode().length() != 5) {
            throw new IllegalArgumentException("ZIP code must be exactly 5 digits long.");
        }
    }

    @PutMapping("/admin/editPlan/{premiumPlanId}")
    public PremiumPlan editPlan(@PathVariable("premiumPlanId") Long premiumPlanId,
                           @RequestBody PremiumPlanEditModel premiumPlanEditModel){
        return premiumPlanService.editPlan(premiumPlanId, premiumPlanEditModel);
    }

    private int calculateDiscountRate(String couponCode) {
        //IDEA:Better if I have coupon table which has all coupons with respected discount rates.
        return switch (couponCode) {
            case "NEW" -> 10;
            case "OLDAGAIN" -> 5;
            case "STUDENT" -> 50;
            case "" -> 0;
            default ->
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid coupon code");
        };
    }

    //IDEA: cancel premium membership or remove premium user after that period ends
}
