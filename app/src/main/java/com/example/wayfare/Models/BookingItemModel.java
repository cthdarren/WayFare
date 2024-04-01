package com.example.wayfare.Models;

import com.example.wayfare.Utils.Helper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookingItemModel {
    public final String thumbnailUrl;
    public final String title;
    public final String location;
    public final String timeToBooking;
    public final String dateOfBooking;
    public final String wayfarerPicUrl;
    public final String wayfarerUsername;

    public BookingItemModel(String thumbnailUrl, String title, String location, int startTimeOfBooking, String dateOfBooking, String wayfarerPicUrl, String wayfarerUsername) {
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.location = location;
        this.wayfarerPicUrl = wayfarerPicUrl;
        this.wayfarerUsername = wayfarerUsername;
        String timePostfix = "am";

        if (startTimeOfBooking > 12)
            timePostfix = "pm";

        if (startTimeOfBooking == 0)
            startTimeOfBooking = 12;

        this.dateOfBooking = LocalDate.parse(dateOfBooking.substring(0,10)).format( DateTimeFormatter.ofPattern("dd MMM YYYY, ")) + String.valueOf(startTimeOfBooking) + timePostfix;


        timeToBooking = Helper.getDifferenceInTimeString(Instant.parse(dateOfBooking), Instant.now());
    }
}

