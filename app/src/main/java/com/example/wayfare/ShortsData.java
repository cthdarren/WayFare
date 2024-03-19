package com.example.wayfare;

public class ShortsData {
    private String shortsPath, shortsUser, shortsTitle;
    private int shortsImage;

    public String getShortsPath() {
        return shortsPath;
    }

    public String getShortsUser() {
        return shortsUser;
    }

    public String getShortsTitle() {
        return shortsTitle;
    }

    public int getShortsImage() {
        return shortsImage;
    }

    public ShortsData(String shortsPath, String shortsUser, String shortsTitle, int shortsImage) {
        this.shortsPath = shortsPath;
        this.shortsUser = shortsUser;
        this.shortsTitle = shortsTitle;
        this.shortsImage = shortsImage;
    }
}
