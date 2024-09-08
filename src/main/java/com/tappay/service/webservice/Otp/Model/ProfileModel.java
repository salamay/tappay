package com.tappay.service.webservice.Otp.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Date;

@Entity
@Table(name = "profile")
public class ProfileModel {
    @Id
    private int uid;
    private String email;
    private String account_status;
    private boolean is_email_verified=false;
    private String first_name="";
    private String last_name="";
    private String phone_number="";
    private String kyc_id_verification_type="";
    private String kyc_id_verification__url="";
    private String kyc_verification_status="";
    private String profile_image_url="";
    private Date date_joined;
    private int user_uid;

    public ProfileModel() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccount_status() {
        return account_status;
    }

    public void setAccount_status(String account_status) {
        this.account_status = account_status;
    }

    public boolean isIs_email_verified() {
        return is_email_verified;
    }

    public void setIs_email_verified(boolean is_email_verified) {
        this.is_email_verified = is_email_verified;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getKyc_id_verification_type() {
        return kyc_id_verification_type;
    }

    public void setKyc_id_verification_type(String kyc_id_verification_type) {
        this.kyc_id_verification_type = kyc_id_verification_type;
    }

    public String getKyc_id_verification__url() {
        return kyc_id_verification__url;
    }

    public void setKyc_id_verification__url(String kyc_id_verification__url) {
        this.kyc_id_verification__url = kyc_id_verification__url;
    }

    public String getKyc_verification_status() {
        return kyc_verification_status;
    }

    public void setKyc_verification_status(String kyc_verification_status) {
        this.kyc_verification_status = kyc_verification_status;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public Date getDate_joined() {
        return date_joined;
    }

    public void setDate_joined(Date date_joined) {
        this.date_joined = date_joined;
    }

    public int getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(int user_uid) {
        this.user_uid = user_uid;
    }
}
