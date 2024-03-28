package com.example.wayfare.Models;

import java.time.Instant;
import java.util.List;

public class ProfileModel {
    private final String username;
    private final String aboutMe;
    private final String pictureUrl;
    private final List<String> badges;
    private final Double avgScore;
    private final Integer reviewCount;
    private final String role;
    private final List<ReviewModel> reviews;
    private final List<TourListModel> tours;
    private final Instant dateCreated;

    public ProfileModel(String username, String aboutMe, String pictureUrl, List<String> badges, Double avgScore, Integer reviewCount, String role, List<ReviewModel> reviews, List<TourListModel> tours, Instant dateCreated) {
        this.username = username;
        this.aboutMe = aboutMe;
        this.pictureUrl = pictureUrl;
        this.badges = badges;
        this.avgScore = avgScore;
        this.reviewCount = reviewCount;
        this.role = role;
        this.reviews = reviews;
        this.tours = tours;
        this.dateCreated = dateCreated;
    }
}
