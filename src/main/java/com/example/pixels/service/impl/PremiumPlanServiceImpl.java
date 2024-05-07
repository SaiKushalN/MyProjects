package com.example.pixels.service.impl;

import com.example.pixels.entity.PremiumPlan;
import com.example.pixels.model.PremiumPlanEditModel;
import com.example.pixels.model.PremiumPlanModel;
import com.example.pixels.repository.PremiumPlanRepository;
import com.example.pixels.service.PremiumPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class PremiumPlanServiceImpl implements PremiumPlanService {

    @Autowired
    PremiumPlanRepository premiumPlanRepository;
    @Override
    public PremiumPlan addPremiumPlan(PremiumPlanModel premiumPlanModel) {

        Optional<PremiumPlan> premiumPlanFromDb = premiumPlanRepository
                .findByPlanNameIgnoreCase(premiumPlanModel.getPlanName());

        if(premiumPlanFromDb.isPresent())
            throw new DataIntegrityViolationException("Plan already exists.");

        PremiumPlan premiumPlan = PremiumPlan.builder()
                .planName(premiumPlanModel.getPlanName())
                .planDescription((premiumPlanModel.getPlanDescription().isEmpty())
                        ?getPremiumDescription(premiumPlanModel.getPlanMonths()):premiumPlanModel.getPlanDescription())
                .planMonths(premiumPlanModel.getPlanMonths())
                .planPrice(premiumPlanModel.getPlanPrice())
                .activePlan(premiumPlanModel.isActivePlan())
                .currency("USD")
                .build();

        return premiumPlanRepository.save(premiumPlan);
    }

    @Override
    public void addBasic3Plan(){
        PremiumPlan premiumPlan = PremiumPlan.builder()
                .planName("Basic 3")
                .planDescription(getPremiumDescription(3))
                .planMonths(3)
                .planPrice(27.0)
                .activePlan(true)
                .currency("USD")
                .build();

        premiumPlanRepository.save(premiumPlan);
    }

    @Override
    public String deletePlan(Long premiumPlanId) {
        premiumPlanRepository.delete(getPremiumPlanById(premiumPlanId));
        return "Plan deleted successfully.";
    }

    @Override
    public PremiumPlan editPlan(Long premiumPlanId, PremiumPlanEditModel premiumPlanEditModel) {
        PremiumPlan premiumPlan = getPremiumPlanById(premiumPlanId);
        if(!premiumPlanEditModel.getPlanDescription().isEmpty())
            premiumPlan.setPlanDescription(premiumPlanEditModel.getPlanDescription());

        premiumPlan.setActivePlan(premiumPlanEditModel.isActivePlan());
        return premiumPlanRepository.save(premiumPlan);
    }

    public String getPremiumDescription(Integer months){
        return months + " months of premium subscription.";
    }

    @Override
    public List<PremiumPlan> getAllPremiumPlans() {
        List<PremiumPlan> premiumPlans = premiumPlanRepository.findAll();
        if(premiumPlans.isEmpty())
            throw new NoSuchElementException("No premium plans are available");
        return premiumPlans;
    }

    @Override
    public PremiumPlan findByPlanNameIgnoreCase(String planName){
        Optional<PremiumPlan> premiumPlan = premiumPlanRepository.findByPlanNameIgnoreCase(planName);
        if(premiumPlan.isEmpty())
            throw new NoSuchElementException("Premium plan does not exist.");
        return premiumPlan.get();
    }

    public PremiumPlan getPremiumPlanById(Long premiumPlanId){
        Optional<PremiumPlan> premiumPlan = premiumPlanRepository.findById(premiumPlanId);
        if(premiumPlan.isEmpty()){
            throw new NoSuchElementException("Premium plan does not exist.");
        }
        return premiumPlan.get();
    }
}
