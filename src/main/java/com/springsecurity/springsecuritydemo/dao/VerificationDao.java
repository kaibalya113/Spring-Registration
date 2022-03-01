package com.springsecurity.springsecuritydemo.dao;

import com.springsecurity.springsecuritydemo.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationDao extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}
