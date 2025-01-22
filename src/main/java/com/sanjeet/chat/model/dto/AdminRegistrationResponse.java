package com.sanjeet.chat.model.dto;


import java.util.Date;

public class AdminRegistrationResponse {

    private long id;
    private String email;
    private String password;
    private String phoneNo;
    private String sessionToken;
    private Date createdAt;


    public AdminRegistrationResponse() {
    }

    public AdminRegistrationResponse(long id,  String email ,String password,String phoneNo, String sessionToken,Date createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.sessionToken = sessionToken;
        this.createdAt = createdAt;
    }

    public AdminRegistrationResponse(long id,  String email,String phoneNo,Date createdAt) {
        this.id = id;
        this.email = email;
        this.phoneNo = phoneNo;
        this.createdAt = createdAt;
    }

    public AdminRegistrationResponse(long id, String email, String password, String phoneNo, Date createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
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

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AdminRegistrationResponse{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", sessionToken='" + sessionToken + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
