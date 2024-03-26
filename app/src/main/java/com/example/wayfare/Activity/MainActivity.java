package com.example.wayfare.Activity;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.wayfare.Fragment.AddShortsFragment;
import com.example.wayfare.Fragment.Public.PublicSettingsFragment;
import com.example.wayfare.Fragment.Public.PublicUpcomingFragment;
import com.example.wayfare.Fragment.SettingsFragment;
import com.example.wayfare.Fragment.UpcomingFragment;
import com.example.wayfare.Fragment.ExploreFragment;
import com.example.wayfare.Fragment.SignInFragment;
import com.example.wayfare.R;
import com.example.wayfare.Fragment.ToursFragment;
import com.example.wayfare.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Wayfare);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = (View) binding.getRoot();
        setContentView(view);
        View decorView = getWindow().getDecorView();
        replaceFragment(new ExploreFragment());
        //binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.explore){
                replaceFragment(new ExploreFragment());
            } else if (item.getItemId() == R.id.upcoming) {
                replaceFragment(new PublicUpcomingFragment());
            } else if (item.getItemId() == R.id.tours) {
                replaceFragment(new ToursFragment());
            } else if (item.getItemId() == R.id.addShorts) {
                Intent intent = new Intent(MainActivity.this, AddShorts.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.fade_in);
            } else if (item.getItemId() == R.id.account) {
                replaceFragment(new PublicSettingsFragment());
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .setReorderingAllowed(true)
                .addToBackStack("name") // Name can be null
                .commit();
    }




}