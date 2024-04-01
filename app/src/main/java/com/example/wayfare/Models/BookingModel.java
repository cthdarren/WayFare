package com.example.wayfare.Models;




public class BookingModel {

    private final TourListModel listing;
    private final String userId;
    private final TourListModel.TimeRange bookingDuration;
    private final String dateBooked;
    private final Double bookingPrice;
    private final int pax;
    private final String remarks;
    private final String status;
    private final ReturnedUser user;

    public BookingModel(TourListModel listing, String userId, TourListModel.TimeRange bookingDuration, String dateBooked, Double bookingPrice, int pax, String remarks, String status, ReturnedUser user) {
        this.listing = listing;
        this.userId = userId;
        this.bookingDuration = bookingDuration;
        this.dateBooked = dateBooked;
        this.bookingPrice = bookingPrice;
        this.pax = pax;
        this.remarks = remarks;
        this.status = status;
        this.user = user;
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


}


