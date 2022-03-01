package com.springsecurity.springsecuritydemo.model;

public class UserModel {
    private String fname;
    private String lname;
    private String emailId;
    private String password;
    private String machingPassword;
    // private String role;
    // private boolean enabled = false;


    public UserModel() {
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMachingPassword() {
        return machingPassword;
    }

    public void setMachingPassword(String machingPassword) {
        this.machingPassword = machingPassword;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", emailId='" + emailId + '\'' +
                ", password='" + password + '\'' +
                ", machingPassword='" + machingPassword + '\'' +
                '}';
    }
}
