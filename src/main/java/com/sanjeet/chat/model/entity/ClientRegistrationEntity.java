package com.sanjeet.chat.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "client_Details")
public class ClientRegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long clientId;
    @Column(nullable = false)
    private String companyName;
    @Column(nullable = false)
    private String phoneNo;
    @Column(nullable = false)
    private String email;
    private String apiKey;
    private String secretKey;
    private boolean isActive;
    @Column(nullable = true, length = 1024)
    private String sessionToken;
    @Column(nullable = false)
    private String password;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserDetailsEntity> users = new ArrayList<>();


    public ClientRegistrationEntity() {
    }

    public ClientRegistrationEntity(String companyName, String email, String phoneNo, String apiKey, String secretKey, boolean isActive, String sessionToken,String password,Date createdAt) {
        this.companyName = companyName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.isActive = isActive;
        this.sessionToken = sessionToken;
        this.password = password;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date(); // Set current timestamp before saving
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
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

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UserDetailsEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserDetailsEntity> users) {
        this.users = users;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ClientRegistrationEntity{" +
                "clientId=" + clientId +
                ", companyName='" + companyName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", email='" + email + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", isActive=" + isActive +
                ", sessionToken='" + sessionToken + '\'' +
                ", password='" + password + '\'' +
                ", createdAt=" + createdAt +
                ", users=" + users +
                '}';
    }
}
