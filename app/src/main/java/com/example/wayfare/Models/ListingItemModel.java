package com.example.wayfare.Models;

public class ListingItemModel {
    public final String thumbnailUrl;
    public final String title;
    public final double rating;
    public final int ratingCount;

    public ListingItemModel(String thumbnailUrl, String title, double rating, int ratingCount) {
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.rating = rating;
        this.ratingCount = ratingCount;
    }
}

