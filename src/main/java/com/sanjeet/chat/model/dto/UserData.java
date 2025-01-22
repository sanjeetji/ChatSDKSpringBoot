package com.sanjeet.chat.model.dto;


public class UserData {

    private String username;
    private String userImage;
    private String phoneNumber;

    public UserData() {
    }

    public UserData(String username, String userImage, String phoneNumber) {
        this.username = username;
        this.userImage = userImage;
        this.phoneNumber = phoneNumber;
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



}
