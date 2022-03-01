package com.springsecurity.springsecuritydemo.service;

import com.springsecurity.springsecuritydemo.dao.PasswordResetTokenDao;
import com.springsecurity.springsecuritydemo.dao.UserDao;
import com.springsecurity.springsecuritydemo.dao.VerificationDao;
import com.springsecurity.springsecuritydemo.entity.PasswordResetToken;
import com.springsecurity.springsecuritydemo.entity.User;
import com.springsecurity.springsecuritydemo.entity.VerificationToken;
import com.springsecurity.springsecuritydemo.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private VerificationDao verificationDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PasswordResetTokenDao passwordResetTokenDao;

    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setEmailId(userModel.getEmailId());
        user.setFname(userModel.getFname());
        user.setLname(userModel.getLname());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userDao.save(user);
        return user;
    }

    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationDao.save(verificationToken);
    }

    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationDao.findByToken(token);
        if(verificationToken == null){
            return "invalid";
        }
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if(verificationToken.getExpirationTime().getTime() - cal.getTime().getTime() <= 0){
            verificationDao.delete(verificationToken);
            return "expired";
        }
        user.setEnabled(true);
        userDao.save(user);
        return "valid";
    }

    public VerificationToken generateNewToken(String oldToken) {
        VerificationToken verificationToken = verificationDao.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationDao.save(verificationToken);
        return verificationToken;

    }

    public User findUserByEmail(String emailId) {
        return userDao.findByEmailId(emailId);
    }

    public void createPasswordresetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetTokenDao.save(passwordResetToken);
    }

    public String validatePasswordresetToken(String token) {

        PasswordResetToken passwordResetToken = passwordResetTokenDao.findByToken(token);
        if(passwordResetToken == null){
            return "invalid";
        }
        User user = passwordResetToken.getUser();
        Calendar cal = Calendar.getInstance();
        if(passwordResetToken.getExpirationTime().getTime() - cal.getTime().getTime() <= 0){
            passwordResetTokenDao.delete(passwordResetToken);
            return "expired";
        }
        user.setEnabled(true);
        userDao.save(user);
        return "valid";
    }

    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenDao.findByToken(token).getUser());
    }

    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.save(user);
    }
}
