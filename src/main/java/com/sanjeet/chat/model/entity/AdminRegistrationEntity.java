package com.sanjeet.chat.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "admin_Details")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class AdminRegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String phoneNo;
    @Column(nullable = false)
    private String password;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(nullable = true, length = 1024)
    private String sessionToken;


    public AdminRegistrationEntity() {
    }

    public AdminRegistrationEntity(String email, String phoneNo, String password, Date createdAt, String sessionToken) {
        this.email = email;
        this.phoneNo = phoneNo;
        this.password = password;
        this.createdAt = createdAt;
        this.sessionToken = sessionToken;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date(); // Set current timestamp before saving
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        id = id;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AdminRegistrationEntity{" +
                "id=" + id +
                ", phoneNo='" + phoneNo + '\'' +
                ", email='" + email + '\'' +
                ", sessionToken='" + sessionToken + '\'' +
                ", password='" + password + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
