package com.sanjeet.chat.model.dto;

public class AdminLoginRequest {


    @jakarta.validation.constraints.Email(message = "Invalid email format")
    @jakarta.validation.constraints.NotBlank(message = "Email is required")
    private String email;
    @jakarta.validation.constraints.NotBlank(message = "Password is required")
    private String password;


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

    @Override
    public String toString() {
        return "AdminLoginRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
