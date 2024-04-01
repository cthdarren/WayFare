package com.example.wayfare.Models;

public class BookingItemModel {
    public final String thumbnailUrl;
    public final String title;
    public final String timeToBooking;
    public final String dateOfBooking;
    public final String wayfarerPicUrl;
    public final String wayfarerUsername;

    public BookingItemModel(String thumbnailUrl, String title, int startTimeOfBooking, String dateOfBooking, String wayfarerPicUrl, String wayfarerUsername) {
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.timeToBooking = "";
        this.dateOfBooking = dateOfBooking;
        this.wayfarerPicUrl = wayfarerPicUrl;
        this.wayfarerUsername = wayfarerUsername;
    }
}

