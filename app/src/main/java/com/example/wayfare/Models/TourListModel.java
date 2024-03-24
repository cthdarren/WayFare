package com.example.wayfare.Models;
// we are creating variables thats gonna hold all the data that represents one of our items
public class TourListModel {
    public TourListModel(String tourListingTitle,  int tourListingImage) {
        this.tourListingTitle = tourListingTitle;
        this.tourListingImage = tourListingImage;
    }

    String tourListingTitle;
    int tourListingImage;

    public String getTourListingTitle() {
        return tourListingTitle;
    }
    public int getTourListingImage() {
        return tourListingImage;
    }
}
