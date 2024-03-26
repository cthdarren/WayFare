package com.example.wayfare.Models;

import java.util.List;

// we are creating variables thats gonna hold all the data that represents one of our items
public class TourListModel {
    public TourListModel(String tourListingTitle, String tourListingId, String tourListingDescription, String[] tourListingImgUrls, String tourListingCategory, Location tourListingLocation, double tourListingPrice, int minPax, int maxPax, double tourListingRating, int reviewCount, String tourListingGuideId, List<TimeRange> timeRangeList) {
        this.tourListingTitle = tourListingTitle;
        this.tourListingId = tourListingId;
        this.tourListingDescription = tourListingDescription;
        this.tourListingImgUrls = tourListingImgUrls;
        this.tourListingCategory = tourListingCategory;
        this.tourListingLocation = tourListingLocation;
        this.tourListingPrice = tourListingPrice;
        this.minPax = minPax;
        this.maxPax = maxPax;
        this.tourListingRating = tourListingRating;
        this.reviewCount = reviewCount;
        this.tourListingGuideId = tourListingGuideId;
        this.timeRangeList = timeRangeList;
    }

    public String getTourListingId() {
        return tourListingId;
    }
    public String getTourListingTitle() {
        return tourListingTitle;
    }

    public String getTourListingDescription() {
        return tourListingDescription;
    }

    public String[] getTourListingImgUrls() {
        return tourListingImgUrls;
    }

    public String getTourListingCategory() {
        return tourListingCategory;
    }
    public Location getTourListingLocation() {
        return tourListingLocation;
    }
    public List<TimeRange> getTimeRangeList() {
        return timeRangeList;
    }
    public double getTourListingPrice() {
        return tourListingPrice;
    }

    public int getMinPax() {
        return minPax;
    }

    public int getMaxPax() {
        return maxPax;
    }

    public double getTourListingRating() {
        return tourListingRating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public String getTourListingGuideId() {
        return tourListingGuideId;
    }



    private String tourListingTitle;
    private String tourListingId;
    private String tourListingDescription;
    private String[] tourListingImgUrls;
    private String tourListingCategory;
    private Location tourListingLocation;
    private double tourListingPrice;
    private int minPax;
    private int maxPax;
    private double tourListingRating;
    private int reviewCount;
    private String tourListingGuideId;
    private List<TimeRange> timeRangeList;

    // Constructor, getters, and setters
    // You can generate these using your IDE or write them manually

    public static class TimeRange {
        public int getStartTime() {
            return startTime;
        }

        public int getEndTime() {
            return endTime;
        }

        private int startTime;
        private int endTime;

        // Constructor, getters, and setters
        // You can generate these using your IDE or write them manually
    }

    public static class Location {
        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        private double x;
        private double y;

        // Constructor, getters, and setters
        // You can generate these using your IDE or write them manually
    }
}
