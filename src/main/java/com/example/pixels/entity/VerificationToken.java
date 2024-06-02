package com.example.pixels.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VerificationToken {

    private static final int Expiration_time=10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
    private User user;

    public VerificationToken(String token){
        super();
        this.token = token;
        this.expirationTime = calculateExpirationDate(Expiration_time);
    }

    public VerificationToken(User user, String token){
        super();
        this.token=token;
        this.user = user;
        this.expirationTime = calculateExpirationDate(Expiration_time);
    }

    private Date calculateExpirationDate(int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,expirationTime);
        return new Date(calendar.getTime().getTime());
    }
}
