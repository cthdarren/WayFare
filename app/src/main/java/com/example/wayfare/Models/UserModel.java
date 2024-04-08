package com.example.wayfare.Models;

import java.time.Instant;
import java.util.List;

public class UserModel {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> languagesSpoken;
    private String phoneNumber;
    private boolean isVerified;
    private String dateCreated;
    private String pictureUrl;
    private String aboutMe;
    private List<String> badges;
    private String role;

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


    public List<String> getLanguagesSpoken() {
        return languagesSpoken;
    }

    public void setLanguagesSpoken(List<String> languagesSpoken) {
        this.languagesSpoken = languagesSpoken;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }
}
