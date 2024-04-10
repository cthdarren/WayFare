package com.example.wayfare.Models;

import java.time.Instant;

public class ReturnedUser {
    private final String username;
    private final String pictureUrl;
    private final String dateCreated;
    private final boolean isVerified;

    ReturnedUser(String username, String pictureUrl, String dateCreated, boolean isVerified) {
        this.username = username;
        this.pictureUrl = pictureUrl;
        this.dateCreated = dateCreated;
        this.isVerified = isVerified;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getUsername() {
        return username;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public String getDateCreated() {
        return dateCreated;
    }
}
