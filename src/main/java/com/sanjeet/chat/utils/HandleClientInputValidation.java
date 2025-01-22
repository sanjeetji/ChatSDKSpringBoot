package com.sanjeet.chat.utils;


import com.sanjeet.chat.model.entity.AdminRegistrationEntity;
import com.sanjeet.chat.model.entity.ClientRegistrationEntity;
import com.sanjeet.chat.model.entity.UserDetailsEntity;

public class HandleClientInputValidation {

    public void handleClientRegistrationInput(ClientRegistrationEntity request){

            if (request.getCompanyName() == null || request.getCompanyName().isEmpty()) {
                throw new IllegalArgumentException("Company name cannot be null or empty");
            }
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty");
            }
            if (request.getPhoneNo() == null || request.getPhoneNo().isEmpty()) {
                throw new IllegalArgumentException("Phone number cannot be null or empty");
            }
            if (request.getPassword() == null || request.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be null or empty");
            }
    }

    public void handleUserRegistrationInput(UserDetailsEntity request){

        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty");
        }
        if (request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException("User Phone cannot be null or empty");
        }
        if (request.getApiKey() == null || request.getApiKey().isEmpty()) {
            throw new IllegalArgumentException("API KEY cannot be null or empty");
        }
        if (request.getUserImage() == null || request.getUserImage().isEmpty()) {
            throw new IllegalArgumentException("User Image cannot be null or empty");
        }
    }

    public void handleAdminRegistrationInput(AdminRegistrationEntity request) {
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (request.getPhoneNo() == null || request.getPhoneNo().isEmpty()) {
            throw new IllegalArgumentException("User Phone cannot be null or empty");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }
}
