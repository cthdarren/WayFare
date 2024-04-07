package com.example.wayfare.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import 	android.content.Intent;
import java.lang.Runnable;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();


        // Hide the status bar.
        setContentView(R.layout.activity_splash);
        if (new AuthHelper(getApplicationContext()).sharedPreferences.getString("Theme", "").equals("DARK")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (new AuthHelper(getApplicationContext()).getSharedPrefsValue("WAYFARER_VIEW").equals("TRUE")){
                    intent = new Intent(SplashActivity.this, WayfarerActivity.class);
                }
                else{
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(intent);
//                overridePendingTransition(R.anim.slide_right_to_left, R.anim.fade_out);
                finish();
            }
        },1000);
    }
}