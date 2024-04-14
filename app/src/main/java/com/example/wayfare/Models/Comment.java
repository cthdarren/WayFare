package com.example.wayfare.Models;


import java.util.ArrayList;
import java.util.Date;

public class Comment {
    public String id;

    public Comment(String id, String journeyId, String userId, String commentContent, String dateCreated, UserModel user) {
        this.id = id;
        this.journeyId = journeyId;
        this.userId = userId;
        this.commentContent = commentContent;
        this.dateCreated = dateCreated;
        this.user = user;
    }

    public String journeyId;
    public String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String commentContent;
    public String dateCreated;
    public UserModel user;
}
