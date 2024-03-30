package com.example.wayfare.Models;

import java.time.Instant;

public class ReviewModel {
    private final String id;
    private final String title;
    private final Integer score;
    private final String reviewContent;
    private final String dateCreated;
    private final String dateModified;
    private UserModel user;
//    private TourListing listing;

    public ReviewModel(String id, String title, Integer score, String reviewContent, String dateCreated, String dateModified, UserModel userId) {
        this.id = id;
        this.title = title;
        this.score = score;
        this.reviewContent = reviewContent;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getScore() {
        return score;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDateModified() {
        return dateModified;
    }

    public UserModel getUser() {
        return user;
    }
}
