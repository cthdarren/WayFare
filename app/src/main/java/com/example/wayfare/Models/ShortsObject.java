package com.example.wayfare.Models;

public class ShortsObject {
    public ShortsObject(){}
    private String url, title, description, date, user_id;

    public ShortsObject(String url, String title, String description, String date, String user_id) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.date = date;
        this.user_id = user_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
