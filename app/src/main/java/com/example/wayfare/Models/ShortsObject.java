package com.example.wayfare.Models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShortsObject  {
    //    private String id;
    private String shortsUrl,id;
    private String userId;
    private String userName;
    private String posterPictureUrl;
    private String description;
    private String thumbnailUrl;
    private TourListModel listing;
    private ArrayList<String> likes;
    private Date datePosted;
    private List<Comment> comments;

    public ShortsObject(){}
    public ShortsObject(String id, String shortsUrl, String userName, String userId, String description, String thumbnailUrl, Date datePosted, TourListModel listing, ArrayList<String> likes) {
        this.id = id;
        this.shortsUrl = shortsUrl;
        this.userId = userId;
        this.userName = userName;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.listing = listing;
        this.likes = likes;
        this.datePosted = datePosted;
    }

    public String getId() {
        return id;
    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getShortsUrl() {
        return shortsUrl;
    }

    public void setShortsUrl(String shortsUrl) {
        this.shortsUrl = shortsUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TourListModel getListing() {
        return listing;
    }

    public void setListing(TourListModel listing) {
        this.listing = listing;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }
    public void addLike(String like) {
        if (!this.likes.contains(like)) {
            this.likes.add(like);
        }
    }
    public void removeLike(String like) {
        if (this.likes != null) {
            this.likes.remove(like);
        }
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
