package com.example.wayfare.Models;

public class BookmarkItemModel {
    public final String title;
    public final String thumbnail;
    public final String username;
    public final String location;
    public final String ratings;
    public final String listingId;

    public BookmarkItemModel(String title, String thumbnail, String username, String location, double ratings, int ratingCount, String listingId){

        this.title = title;
        this.thumbnail = thumbnail;
        this.username = "Hosted by " + username;
        this.location = location;
        this.listingId = listingId;
        if (ratings == 0)
            this.ratings = "No reviews yet";
        else
            this.ratings =  "★ " +String.valueOf(ratings) + "(" + String.valueOf(ratingCount) + ")";
    }

}

