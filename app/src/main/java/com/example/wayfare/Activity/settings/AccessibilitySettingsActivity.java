package com.example.wayfare.Activity.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.wayfare.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class AccessibilitySettingsActivity extends AppCompatActivity {

    ImageView privacy_back;
    SwitchMaterial theme_change_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings_accessibility);

        theme_change_switch = findViewById(R.id.theme_change_switch);
        privacy_back = findViewById(R.id.accessibility_back);

        privacy_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //TODO DARK MODE LIGHT MODE CHANGE just tell them that changes will be applied on app restart
//        theme_change_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked)
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                else {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                }
//            }
//        });
    }

}