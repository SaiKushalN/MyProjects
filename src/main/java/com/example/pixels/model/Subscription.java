package com.example.pixels.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String subscriptionType;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

//    @PrePersist
//    public void prePersist() {
//        if (startDate == null) {
//            startDate = LocalDate.now();
//        }
//    }

    public void initializeDatesBasedOnType() {
        if ("Free".equalsIgnoreCase(subscriptionType)) {
            // For free subscription, there's no explicit start/end date.
            this.startDate = null;
            this.endDate = null;
        } else {
            // For paid subscriptions, set start date to now and calculate end date.
            this.startDate = LocalDate.now();
            switch (subscriptionType) {
                case "1":
                    this.endDate = this.startDate.plusMonths(1);
                    break;
                case "3":
                    this.endDate = this.startDate.plusMonths(3);
                    break;
                case "12":
                    this.endDate = this.startDate.plusMonths(12);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid subscription type");
            }
        }
    }


//    Type Selection
//    Subscription subscription = new Subscription();
//    subscription.setUser(user);
//    subscription.setSubscriptionType("1 Month"); // Set based on user input
//    subscription.initializeDatesBasedOnType();
//    subscriptionRepository.save(subscription);
}
