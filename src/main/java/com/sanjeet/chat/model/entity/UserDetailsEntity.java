package com.sanjeet.chat.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Date;


@Entity(name = "user_details")
public class UserDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    @Column(name = "user_name", nullable = false)
    private String username;
    @Column(name = "user_image", nullable = false)
    private String userImage;
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @Column(name = "api_key", nullable = false)
    private String apiKey;
    @Column(name = "user_session_token", nullable = true, length = 1024)
    private String userSessionToken;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnore
    private ClientRegistrationEntity client;

    public UserDetailsEntity() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date(); // Set current timestamp before saving
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public ClientRegistrationEntity getClient() {
        return client;
    }

    public void setClient(ClientRegistrationEntity client) {
        this.client = client;
    }

    public String getUserSessionToken() {
        return userSessionToken;
    }

    public void setUserSessionToken(String userSessionToken) {
        this.userSessionToken = userSessionToken;
    }

    @Override
    public String toString() {
        return "UserDetailsEntity{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", userImage='" + userImage + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", userSessionToken='" + userSessionToken + '\'' +
                ", createdAt=" + createdAt +
                ", client=" + client +
                '}';
    }
}
