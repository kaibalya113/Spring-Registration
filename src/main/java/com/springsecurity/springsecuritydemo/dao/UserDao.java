package com.springsecurity.springsecuritydemo.dao;

import com.springsecurity.springsecuritydemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    //User findByEmail(String email);

    User findByEmailId(String emailId);
}
