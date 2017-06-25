package com.home.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.inject.Singleton;
import java.util.Date;

@Singleton
public class AppUser {
    private boolean isLoggedIn;
    private String firstName;
    private String lastName;
    private String email;
    //private Date dob;
    private String password; //TODO encrypt password

    @JsonIgnore
    public boolean isLoggedIn() {
        return isLoggedIn;
    }
    @JsonIgnore
    public void setLoggedIn() {
        isLoggedIn = true;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }*/

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
