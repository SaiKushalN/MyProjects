package com.example.pixels.service.impl;

import com.example.pixels.entity.PremiumPlan;
import com.example.pixels.entity.PremiumUser;
import com.example.pixels.entity.User;
import com.example.pixels.repository.PremiumPlanRepository;
import com.example.pixels.repository.PremiumRepository;
import com.example.pixels.service.PremiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class PremiumServiceImpl implements PremiumService {

    @Autowired
    PremiumRepository premiumRepository;

    @Override
    public String getPremiumStatus(User user) {
        LocalDate endDate = getPremiumUser(user).getSubEndDate();
        return "Your subscription ends at "+endDate+".";
    }

    @Override
    public PremiumUser getPremiumUser(User user) {
        Optional<PremiumUser> premiumUser = premiumRepository.findByUserId(user.getId());
        if(premiumUser.isEmpty())
            throw new NoSuchElementException("You are not a premium member");
        return premiumUser.get();
    }

    @Override
    public PremiumUser savePremiumUser(PremiumUser premiumUser) {
        return premiumRepository.save(premiumUser);
    }


}
