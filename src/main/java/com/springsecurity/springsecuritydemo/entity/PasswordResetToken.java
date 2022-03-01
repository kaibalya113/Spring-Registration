package com.springsecurity.springsecuritydemo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class PasswordResetToken {

    // Expire time will be 10 Minutes
    private static final int EEXPIRATION_TIME = 10;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String token;
    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_PASSWORD_TOKEN"))
    private User user;
    public PasswordResetToken(User user, String token){
        super();
        this.user = user;
        this.token = token;
        this.expirationTime = calculateExpirationdate(EEXPIRATION_TIME);
    }
    public PasswordResetToken(String token){
        super();
        this.token = token;
        this.expirationTime = calculateExpirationdate(EEXPIRATION_TIME);
    }

    private Date calculateExpirationdate(int eexpirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, eexpirationTime);
        return new Date(calendar.getTime().getTime());
    }
}
