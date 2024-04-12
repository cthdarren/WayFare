package com.example.wayfare.Models;

import java.util.List;

public class ProfileModel {
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String aboutMe;
    private final String pictureUrl;
    private final List<String> badges;
    private final List<String> languagesSpoken;
    private final Double avgScore;
    private final Integer reviewCount;
    private final String role;
    private final List<ReviewModel> reviews;
    private final List<TourListModel> tours;
    private final String dateCreated;
    private final boolean verified;

    public ProfileModel(String username, String firstName, String lastName, String aboutMe, String pictureUrl, List<String> badges, List<String> languagesSpoken, Double avgScore, Integer reviewCount, String role, List<ReviewModel> reviews, List<TourListModel> tours, String dateCreated, boolean verified) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.aboutMe = aboutMe;
        this.pictureUrl = pictureUrl;
        this.badges = badges;
        this.languagesSpoken = languagesSpoken;
        this.avgScore = avgScore;
        this.reviewCount = reviewCount;
        this.role = role;
        this.reviews = reviews;
        this.tours = tours;
        this.dateCreated = dateCreated;
        this.verified = verified;
    }

    public String getUsername() {
        return username;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public List<String> getBadges() {
        return badges;
    }

    public Double getAvgScore() {
        return avgScore;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public String getRole() {
        return role;
    }

    public List<ReviewModel> getReviews() {
        return reviews;
    }

    public List<TourListModel> getTours() {
        return tours;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getLanguagesSpoken() {
        return languagesSpoken;
    }

    public boolean isVerified() {
        return verified;
    }
}
