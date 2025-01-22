package com.sanjeet.chat.model.dto;


import java.util.Date;

public class ClientRegistrationResponse {

    private long id;
    private String companyName;
    private String phoneNo;
    private String email;
    private String apiKey;
    private boolean isActive;
    private String password;
    private String sessionToken="";
    private Date createdAt;


    public ClientRegistrationResponse(long id, String companyName, String phoneNo, String email, String apiKey,boolean isActive,String password, String sessionToken) {
        this.id = id;
        this.companyName = companyName;
        this.phoneNo = phoneNo;
        this.email = email;
        this.apiKey = apiKey;
        this.isActive = isActive;
        this.password = password;
        this.sessionToken = sessionToken;
    }

    public ClientRegistrationResponse(long id, String companyName, String phoneNo, String email, String apiKey) {
        this.id = id;
        this.companyName = companyName;
        this.phoneNo = phoneNo;
        this.email = email;
        this.apiKey = apiKey;
    }


    public ClientRegistrationResponse(long id, String companyName, String phoneNo, String email, String apiKey, boolean isActive,String password,Date createdAt) {
        this.id = id;
        this.companyName = companyName;
        this.phoneNo = phoneNo;
        this.email = email;
        this.apiKey = apiKey;
        this.isActive = isActive;
        this.password = password;
        this.createdAt = createdAt;
    }

    public ClientRegistrationResponse(long id, String companyName, String phoneNo, String email, String apiKey, boolean isActive,String sessionToken) {
        this.id = id;
        this.companyName = companyName;
        this.phoneNo = phoneNo;
        this.email = email;
        this.apiKey = apiKey;
        this.isActive = isActive;
        this.sessionToken = sessionToken;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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
        return "ClientRegistrationResponse{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", email='" + email + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", isActive=" + isActive +
                ", password='" + password + '\'' +
                ", sessionToken='" + sessionToken + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
