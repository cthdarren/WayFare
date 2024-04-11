package com.example.wayfare.Models;

import com.example.wayfare.Utils.Helper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class BookingItemModel {
    public final String id;
    public final String listingId;
    public final String thumbnailUrl;
    public final String title;
    public final String location;
    public final String timeToBooking;
    public final String dateOfBooking;
    public final String timeOfBooking;
    public final String wayfarerPicUrl;
    public final String wayfarerUsername;
    public final String listingUrl;
    public final boolean reviewed;

    public BookingItemModel(String id, String listingId, String thumbnailUrl, String title, String location, int startTimeOfBooking, String dateOfBooking, String wayfarerPicUrl, String wayfarerUsername, String listingUrl, boolean reviewed) {
        this.id = id;
        this.listingId = listingId;
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.location = location;
        this.wayfarerPicUrl = wayfarerPicUrl;
        this.wayfarerUsername = wayfarerUsername;
        this.listingUrl = listingUrl;
        this.reviewed = reviewed;
        String timePostfix = "am";
        String localBookingTime;

        if (startTimeOfBooking == 0)
            localBookingTime = "12am";

        else if (startTimeOfBooking > 12) {
            timePostfix = "pm";
            localBookingTime = String.valueOf(startTimeOfBooking - 12) + timePostfix;
        }
        else
            localBookingTime = String.valueOf(startTimeOfBooking ) + timePostfix;

        this.timeOfBooking = localBookingTime;

        this.dateOfBooking = LocalDate.parse(dateOfBooking.substring(0,10)).format( DateTimeFormatter.ofPattern("dd MMM YYYY"));


        timeToBooking = Helper.getDifferenceInTimeString(Instant.parse(dateOfBooking).plus(startTimeOfBooking-8, ChronoUnit.HOURS), Instant.now());
    }
}

