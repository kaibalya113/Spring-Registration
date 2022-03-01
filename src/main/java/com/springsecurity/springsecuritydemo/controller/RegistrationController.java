package com.springsecurity.springsecuritydemo.controller;

import com.springsecurity.springsecuritydemo.entity.User;
import com.springsecurity.springsecuritydemo.entity.VerificationToken;
import com.springsecurity.springsecuritydemo.event.RegistractionCompleteEvent;
import com.springsecurity.springsecuritydemo.model.PasswordModel;
import com.springsecurity.springsecuritydemo.model.UserModel;
import com.springsecurity.springsecuritydemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("/hello")
    public String index(){
        return "Spring";
    }
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserModel userModel, HttpServletRequest httpServletRequest){
        User user = userService.registerUser(userModel);
        /* we need to send email to user.
            this will send message to user for active the user from email.
            for that we have to create event.
            THIS event will send email: applicationeventpublisher

        */
        applicationEventPublisher.publishEvent
                (new RegistractionCompleteEvent(user, applicationUrl(httpServletRequest)));
        return ResponseEntity.ok(user);
    }

    private String applicationUrl(HttpServletRequest httpServletRequest) {
        return "http://"+httpServletRequest.getServerName()+":"+httpServletRequest.getServerPort()+"/"+
                httpServletRequest.getContextPath();
    }

    @GetMapping("/verifyRegistraction")
    public String verifyregistration(@RequestParam("token") String token){
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")){
            return "user verified";
        }else{
            return "user not verified";
        }
    }

    @GetMapping("/resendVerificationtoken")
    public String resendVerificationToken(@RequestParam("token") String oldToken,
                                          HttpServletRequest request){
        VerificationToken verificationToken = userService.generateNewToken(oldToken);
        User user = verificationToken.getUser();
        resendVerificationmail(user, applicationUrl(request), verificationToken);
        return "Verification Link Sent";
    }

    private void resendVerificationmail(User user, String applicationUrl, VerificationToken verificationToken) {
        String url = applicationUrl + "/verifyRegistraction?token="+verificationToken.getToken();
        log.info("click the link to verify your account : {}", url);
    }

    @PostMapping("/resetpassword")
    public String resetpassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request){
        User user = userService.findUserByEmail(passwordModel.getEmailId());
        String url = "";
        if(!Objects.isNull(user)){
            String token = UUID.randomUUID().toString();
            userService.createPasswordresetTokenForUser(user, token);
            url = passwordresetTokenMail(user, applicationUrl(request), token);
        }
        return url;
    }

    @PostMapping("/savepassword")
    public String savePassword(@RequestParam("token") String token,
                               @RequestBody PasswordModel passwordModel){
        String result = userService.validatePasswordresetToken(token);
        if(!result.equalsIgnoreCase("valid")){
            return "Invalid token";
        }
        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if(user.isPresent()){
            userService.changePassword(user.get(), passwordModel.getNewPassword());
            return "Password Reset Successfull";
        }else{
            return "Invalid Token";
        }
    }

    private String passwordresetTokenMail(User user, String applicationUrl, String token) {
        String url = applicationUrl + "savepassword?token="+token;
        log.info("click the link to reset your password : {}", url);
        return url;
    }
}
