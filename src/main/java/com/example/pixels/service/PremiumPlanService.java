package com.example.pixels.service;

import com.example.pixels.entity.PremiumPlan;
import com.example.pixels.model.PremiumPlanEditModel;
import com.example.pixels.model.PremiumPlanModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PremiumPlanService {
    PremiumPlan addPremiumPlan(PremiumPlanModel premiumPlanModel);

    List<PremiumPlan> getAllPremiumPlans();

    PremiumPlan findByPlanNameIgnoreCase(String planName);

    public void addBasic3Plan();

    String deletePlan(Long premiumPlanId);

    PremiumPlan editPlan(Long premiumPlanId, PremiumPlanEditModel premiumPlanEditModel);
}
