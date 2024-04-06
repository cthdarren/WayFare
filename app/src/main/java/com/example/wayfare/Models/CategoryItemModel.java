package com.example.wayfare.Models;

import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;

public class CategoryItemModel {
    public final String name;
    public final String enumName;
    public final Drawable icon;
    public boolean selected;

    public CategoryItemModel(String name, String enumName, Drawable icon) {
        this.name = name;
        this.enumName = enumName;
        this.icon= icon;
        this.selected = false;
    }

}

