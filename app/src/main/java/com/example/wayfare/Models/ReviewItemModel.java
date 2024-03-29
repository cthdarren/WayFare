package com.example.wayfare.Models;

import androidx.appcompat.app.AppCompatActivity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Objects;

public class ReviewItemModel {
    public final String title;
    public final String picUrl;
    public final String firstName;
    public final String reviewContent;
    public final String timeSinceCreation;
    public String editedString = "";

    public ReviewItemModel(String title, String picUrl, String firstName, String reviewContent, String dateCreated, String dateModified) {
        this.title = title;
        this.picUrl = picUrl;
        this.firstName = firstName;
        this.reviewContent = reviewContent;
        Instant modifiedDate = Instant.parse(dateModified);
        Instant createdDate = Instant.parse(dateCreated);

        String timeCategory;
        long number;

        if (!Objects.equals(dateCreated, dateModified)) {
            editedString = "(edited)";
        }
        long epochDiff = Instant.now().getEpochSecond() - modifiedDate.getEpochSecond();
        if (epochDiff > 60 * 60 * 24 * 365) {
            number = epochDiff / (60 * 60 * 24 * 365);
            timeCategory = "year";
        } else if (epochDiff > 60 * 60 * 24 * 12) {
            number = epochDiff / (60 * 60 * 24 * 12);
            timeCategory = "month";
        } else if (epochDiff > 60 * 60 * 24) {
            number = epochDiff / (60 * 60 * 24);
            timeCategory = "day";
        } else if (epochDiff > 60 * 60) {
            number = epochDiff / (60 * 60);
            timeCategory = "hour";
        } else if (epochDiff > 60) {
            number = epochDiff / 60;
            timeCategory = "minute";
        } else {
            number = epochDiff;
            timeCategory = "second";
        }
        if (number > 1) {
            timeCategory += "s";
        }

        timeSinceCreation = String.format("%s %s ago %s", number, timeCategory, editedString);
    }

}

