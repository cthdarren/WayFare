package com.example.wayfare.Models;

import java.time.Instant;
import java.util.List;

// we are creating variables thats gonna hold all the data that represents one of our items
public class TourListModel {
    public TourListModel(String title, String id, String description, String[] thumbnailUrls, String category, Location tourListingLocation, String region, double price, int minPax, int maxPax, double rating, int reviewCount, String userId, List<TimeRange> timeRangeList, String dateCreated) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbnailUrls = thumbnailUrls;
        this.category = category;
        this.location = tourListingLocation;
        this.region = region;
        this.price = price;
        this.minPax = minPax;
        this.maxPax = maxPax;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.userId = userId;
        this.timeRangeList = timeRangeList;
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String[] getThumbnailUrls() {
        return thumbnailUrls;
    }

    public String getCategory() {
        return category;
    }
    public Location getLocation() {
        return location;
    }

    public String getRegion(){ return region;}

    public double getPrice() {
        return price;
    }

    public int getMinPax() {
        return minPax;
    }

    public int getMaxPax() {
        return maxPax;
    }

    public double getRating() {
        return rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public String getUserId() {
        return userId;
    }

    public List<TimeRange> getTimeRangeList() {
        return timeRangeList;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    private String title;
    private String id;
    private String description;
    private String[] thumbnailUrls;
    private String category;
    private Location location;
    private String region;
    private double price;
    private int minPax;
    private int maxPax;
    private double rating;
    private int reviewCount;
    private String userId;
    private List<TimeRange> timeRangeList;
    private final String dateCreated;
    private Instant dateCreatedInstant;

    public void setDateCreatedInstant(Instant value) {
        dateCreatedInstant = value;
    }

    public Instant getDateCreatedInstant() {
        return dateCreatedInstant;
    }

    public static class TimeRange extends TimeSlotItemModel {
        public int getStartTime() {
            return startTime;
        }

        public int getEndTime() {
            return endTime;
        }


        public TimeRange(int startTime, int endTime){
            super(startTime, endTime);
        }

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
        public String getXY(){ return String.valueOf(x) + ", " + String.valueOf(y);}

        private double x;
        private double y;

        public Location(double x, double y){
            this.x = x;
            this.y = y;
        }
    }
}
