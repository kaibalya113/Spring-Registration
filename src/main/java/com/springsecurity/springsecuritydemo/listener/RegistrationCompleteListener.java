package com.springsecurity.springsecuritydemo.listener;

import com.springsecurity.springsecuritydemo.entity.User;
import com.springsecurity.springsecuritydemo.event.RegistractionCompleteEvent;
import com.springsecurity.springsecuritydemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RegistrationCompleteListener implements ApplicationListener<RegistractionCompleteEvent> {

    @Autowired
    private UserService userService;
    @Override
    public void onApplicationEvent(RegistractionCompleteEvent event) {
        // create the verification token for the user with link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user);
        // send mail to user
        String url = event.getApplicationUrl() + "verifyRegistraction?token="+token;
        // send verification email
        log.info("click the link to verify your account : {}", url);
    }
}
