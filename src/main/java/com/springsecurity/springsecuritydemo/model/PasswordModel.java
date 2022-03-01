package com.springsecurity.springsecuritydemo.model;

import lombok.Data;

@Data
public class PasswordModel {
    private String emailId;
    private String oldPassword;
    private String newPassword;

}
