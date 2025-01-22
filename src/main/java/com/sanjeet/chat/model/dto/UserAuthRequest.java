package com.sanjeet.chat.model.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class UserAuthRequest {

    private int userId;
    private int apiKey;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getApiKey() {
        return apiKey;
    }

    public void setApiKey(int apiKey) {
        this.apiKey = apiKey;
    }
}
