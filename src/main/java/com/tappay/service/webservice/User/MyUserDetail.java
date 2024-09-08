package com.tappay.service.webservice.User;

import com.tappay.service.webservice.User.model.UserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetail implements UserDetails {
    private UserModel userModel;
    private String email;
    private String password;
    private String account_status;
    private List<GrantedAuthority> authorities;
    public MyUserDetail(UserModel userModel) {
        this.userModel = userModel;
        this.email=userModel.getEmail();
        this.password=userModel.getPassword();
        this.authorities= Arrays.stream(userModel.getRoles().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userModel.isEnabled();
    }


    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userModel.getPassword();
    }

    @Override
    public String getUsername() {
        return userModel.getEmail();
    }
    public int getUid(){
        return userModel.getUid();
    }
    public String getAccount_status(){
        return userModel.getAccount_status();
    }

    public String getDevice_token(){
        return userModel.getDevice_token();
    }
}
