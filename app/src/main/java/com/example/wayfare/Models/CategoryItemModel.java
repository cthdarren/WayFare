package com.example.wayfare.Models;

import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;

public class CategoryItemModel {
    public final String name;
    public final Drawable icon;

    public CategoryItemModel(String name, Drawable icon) {
        this.name = name;
        this.icon= icon;
    }

}

