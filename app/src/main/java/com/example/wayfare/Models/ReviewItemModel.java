package com.example.wayfare.Models;

import androidx.appcompat.app.AppCompatActivity;

import java.time.Instant;

public class ReviewItemModel {
    public final String title;
    public final String picUrl;
    public final String firstName;
    public final String reviewContent;
    public final String dateCreated;
    public final String dateModified;
    public boolean edited = false;

    public ReviewItemModel(String title, String picUrl, String firstName, String reviewContent, String dateCreated, String dateModified) {
        this.title = title;
        this.picUrl = picUrl;
        this.firstName = firstName;
        this.reviewContent = reviewContent;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;

        if (dateCreated != dateModified){
            edited = true;
        }
    }

}

