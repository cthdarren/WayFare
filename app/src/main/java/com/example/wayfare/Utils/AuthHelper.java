package com.example.wayfare.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.wayfare.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class AuthHelper {
    public static String PREFS_NAME = "sharedPref";
    public static String JSON_DATA_KEY = "user_info";
    public static SharedPreferences sharedPreferences;

    public AuthHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        // default state of jsonString in sharedPref is empty
        // use isLoggedIn true for checking if user is authed
        String jsonString = sharedPreferences.getString(JSON_DATA_KEY, "");
        return !jsonString.isEmpty();
    }




}
