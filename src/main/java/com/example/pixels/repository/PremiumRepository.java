package com.example.pixels.repository;

import com.example.pixels.entity.PremiumUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PremiumRepository extends JpaRepository<PremiumUser, Long> {
    Optional<PremiumUser> findByUserId(Long id);
}
