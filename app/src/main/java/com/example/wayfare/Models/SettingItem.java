package com.example.wayfare.Models;

import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wayfare.Activity.settings.PrivacySettingsActivity;

public class SettingItem {
    public final String name;
    public final Drawable icon;
    public final Class<? extends AppCompatActivity> activity;

    public SettingItem(String name, Drawable icon, Class<? extends AppCompatActivity> activity) {
        this.name = name;
        this.icon= icon;
        this.activity = activity;
    }

}

