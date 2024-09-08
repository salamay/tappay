package com.tappay.service.webservice.Balance.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table("user_balance")
public class UserBalance {
    @Id
    private String id;
    private int uid;
    private String currency;
    private String wallet_type;
    private String country_code;
    private BigDecimal balance;
    private int user_uid;

    public UserBalance() {
        id= UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getWallet_type() {
        return wallet_type;
    }

    public void setWallet_type(String wallet_type) {
        this.wallet_type = wallet_type;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(int user_uid) {
        this.user_uid = user_uid;
    }
}
