package com.example.wayfare.Models;

import java.time.Instant;
import java.util.List;

public class UserModel {
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final boolean isVerified;
    private final Instant dateCreated;
    private final String pictureUrl;
    private final String aboutMe;
    private final List<String> badges;
    private final String role;

    public UserModel(String username, String firstName, String lastName, String email, String phoneNumber, boolean isVerified, Instant dateCreated, String pictureUrl, String aboutMe, List<String> badges, String role) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isVerified = isVerified;
        this.dateCreated = dateCreated;
        this.pictureUrl = pictureUrl;
        this.aboutMe = aboutMe;
        this.badges = badges;
        this.role = role;
    }
}
