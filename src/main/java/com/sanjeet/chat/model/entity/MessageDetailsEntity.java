package com.sanjeet.chat.model.entity;

import jakarta.persistence.*;

import java.util.Date;


@Entity(name = "message_details")
public class MessageDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String message;
    private String secretKey;
    private String receiverPhone;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public MessageDetailsEntity() {
    }

    public MessageDetailsEntity(long id, String message, String secretKey,String receiverPhone) {
        this.id = id;
        this.message = message;
        this.secretKey = secretKey;
        this.receiverPhone = receiverPhone;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date(); // Set current timestamp before saving
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "MessageDetailsEntity{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
