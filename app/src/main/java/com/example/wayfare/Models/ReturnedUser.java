package com.example.wayfare.Models;

public class ReturnedUser {
    private final String username;
    private final String pictureUrl;

    ReturnedUser(String username, String pictureUrl) {
        this.username = username;
        this.pictureUrl = pictureUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getUsername() {
        return username;
    }
}
