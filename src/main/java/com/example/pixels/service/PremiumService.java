package com.example.pixels.service;

import com.example.pixels.entity.PremiumPlan;
import com.example.pixels.entity.PremiumUser;
import com.example.pixels.entity.User;
import com.example.pixels.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PremiumService {
    String getPremiumStatus(User user);

    PremiumUser getPremiumUser(User user);

    PremiumUser savePremiumUser(PremiumUser premiumUser);
}
