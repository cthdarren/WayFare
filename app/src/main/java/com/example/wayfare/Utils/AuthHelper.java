package com.example.wayfare.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthHelper {
    private static final String PREFS_NAME = "sharedPref";
    private static final String JSON_DATA_KEY = "user_info";
    private SharedPreferences sharedPreferences;

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
