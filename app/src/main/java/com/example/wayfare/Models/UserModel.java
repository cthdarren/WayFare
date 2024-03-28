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
    private final String dateCreated;
    private final String pictureUrl;
    private final String aboutMe;
    private final List<String> badges;
    private final String role;

    public UserModel(String username, String firstName, String lastName, String email, String phoneNumber, boolean isVerified, String dateCreated, String pictureUrl, String aboutMe, List<String> badges, String role) {
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

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public List<String> getBadges() {
        return badges;
    }

    public String getRole() {
        return role;
    }


}
