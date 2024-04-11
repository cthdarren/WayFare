package com.example.wayfare.Models;

import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class SettingItemModel {
    public final String name;
    public final Drawable icon;
    public Class<? extends AppCompatActivity> activity;
    public Fragment fragment;

    public SettingItemModel(String name, Drawable icon, Class<? extends AppCompatActivity> activity) {
        this.name = name;
        this.icon= icon;
        this.activity = activity;
    }
    public SettingItemModel(String name, Drawable icon, Fragment fragment) {
        this.name = name;
        this.icon= icon;
        this.fragment = fragment;
    }

}

