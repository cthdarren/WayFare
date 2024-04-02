package com.example.wayfare.Models;

import androidx.appcompat.app.AppCompatActivity;

public class BookmarkModel {
    public final TourListModel listing;
    public final UserModel user;

    public BookmarkModel(TourListModel listing, UserModel user) {
        this.listing = listing;
        this.user = user;
    }

}

