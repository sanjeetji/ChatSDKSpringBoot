package com.sanjeet.chat.model.dto;

public class AuthResponse {

    private String sessionToken;

    public AuthResponse(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    // Getter
    public String getSessionToken() {
        return sessionToken;
    }
}
