package com.example.pixels.repository;

import com.example.pixels.entity.PremiumPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PremiumPlanRepository extends JpaRepository<PremiumPlan, Long> {
    Optional<PremiumPlan> findByPlanNameIgnoreCase(String planName);
}
