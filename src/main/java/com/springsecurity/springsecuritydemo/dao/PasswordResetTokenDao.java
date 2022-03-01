package com.springsecurity.springsecuritydemo.dao;

import com.springsecurity.springsecuritydemo.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenDao extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
}
