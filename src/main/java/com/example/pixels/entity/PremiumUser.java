package com.example.pixels.entity;

import com.example.pixels.dto.IdOnlySerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PremiumUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate subStartDate = null;

    private LocalDate subEndDate = null;

    private String subscriptionType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonSerialize(using = IdOnlySerializer.class)
//    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "premiumUser", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JsonSerialize(contentUsing = IdOnlySerializer.class)
    private List<ReviewAlert> reviewAlerts;
}
