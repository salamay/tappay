package com.tappay.service.webservice.KYC.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "kyc")
public class Kyc {
    @Id
    private int uid;
    private String first_name;
    private String last_name;
    private String phone_number;
    private String id_type;
    private String file_type;
    private String id_file_url;
    private String status;
    private int user_uid;

    public Kyc() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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

    public String getId_type() {
        return id_type;
    }

    public void setId_type(String id_type) {
        this.id_type = id_type;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getId_file_url() {
        return id_file_url;
    }

    public void setId_file_url(String id_file_url) {
        this.id_file_url = id_file_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(int user_uid) {
        this.user_uid = user_uid;
    }
}
