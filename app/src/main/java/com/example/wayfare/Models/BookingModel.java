package com.example.wayfare.Models;




public class BookingModel {
    private final String id;

    private final TourListModel listing;
    private final String userId;
    private final TourListModel.TimeRange bookingDuration;
    private final String dateBooked;
    private final Double bookingPrice;
    private final int pax;
    private final String remarks;
    private final String status;
    private final ReturnedUser user;
    private final boolean reviewed;

    public BookingModel(String id, TourListModel listing, String userId, TourListModel.TimeRange bookingDuration, String dateBooked, Double bookingPrice, int pax, String remarks, String status, ReturnedUser user, boolean reviewed) {
        this.id = id;
        this.listing = listing;
        this.userId = userId;
        this.bookingDuration = bookingDuration;
        this.dateBooked = dateBooked;
        this.bookingPrice = bookingPrice;
        this.pax = pax;
        this.remarks = remarks;
        this.status = status;
        this.user = user;
        this.reviewed = reviewed;
    }


    public TourListModel getListing() {
        return listing;
    }

    public String getUserId() {
        return userId;
    }

    public TourListModel.TimeRange getBookingDuration() {
        return bookingDuration;
    }

    public String getDateBooked() {
        return dateBooked;
    }

    public Double getBookingPrice() {
        return bookingPrice;
    }

    public int getPax() {
        return pax;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getStatus() {
        return status;
    }

    public ReturnedUser getUser() {
        return user;
    }

    public boolean getReviewed() {
        return reviewed;
    }

    public String getId() {
        return id;
    }
}


