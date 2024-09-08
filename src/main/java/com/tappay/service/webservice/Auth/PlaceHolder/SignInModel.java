package com.tappay.service.webservice.Auth.PlaceHolder;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;
import java.util.stream.Stream;

public class SignInModel {

    private String email;
    private String password;
    private String device;

    public SignInModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}