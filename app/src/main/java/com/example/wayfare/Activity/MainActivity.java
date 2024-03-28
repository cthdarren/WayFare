package com.example.wayfare.Activity;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wayfare.Fragment.AddShortsFragment;
import com.example.wayfare.Fragment.Public.PublicSettingsFragment;
import com.example.wayfare.Fragment.Public.PublicUpcomingFragment;
import com.example.wayfare.Fragment.SettingsFragment;
import com.example.wayfare.Fragment.UpcomingFragment;
import com.example.wayfare.Fragment.ExploreFragment;
import com.example.wayfare.Fragment.SignInFragment;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.UserModel;
import com.example.wayfare.R;
import com.example.wayfare.Fragment.ToursFragment;
import com.example.wayfare.Utils.AuthHelper;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.ViewModel.UserViewModel;
import com.example.wayfare.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private UserViewModel viewModel;
    private ProgressBar progBar;

    private boolean loggedIn;
    //TODO when i log in to the app update a viewmodel with all the user details so you can share
    // around the settings/profile fragments
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = (View) binding.getRoot();
        setContentView(view);

        progBar = findViewById(R.id.progressBar);
        progBar.setVisibility(View.VISIBLE);

        if (new AuthHelper(getApplicationContext()).isLoggedIn()){
            loggedIn = true;
            viewModel = new ViewModelProvider(this).get(UserViewModel.class);
            new AuthService(getApplicationContext()).getResponse("/account", Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
                @Override
                public void onError(String message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                            loggedIn = false;
                            progBar.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void onResponse(ResponseModel json) {
                    if(json.success){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                viewModel.updateUserData(new Gson().fromJson(json.data, UserModel.class));
                                progBar.setVisibility(View.GONE);
                            }
                        });
                    }
                    else{
                        Toast.makeText(MainActivity.this, json.data.getAsString(), Toast.LENGTH_SHORT).show();
                        loggedIn = false;
                        progBar.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            loggedIn = false;
            progBar.setVisibility(View.GONE);
        }


        View decorView = getWindow().getDecorView();
        replaceFragment(new ExploreFragment());
        //binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.explore){
                replaceFragment(new ExploreFragment());
            } else if (item.getItemId() == R.id.upcoming) {
                if (loggedIn){
                    replaceFragment(new UpcomingFragment());
                }
                else {
                    replaceFragment(new PublicUpcomingFragment());
                }
            } else if (item.getItemId() == R.id.tours) {
                replaceFragment(new ToursFragment());
            } else if (item.getItemId() == R.id.addShorts) {
                Intent intent = new Intent(MainActivity.this, AddShorts.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.fade_in);
            } else if (item.getItemId() == R.id.account) {
                if (loggedIn) {
                    replaceFragment(new SettingsFragment());
                }
                else {
                    replaceFragment(new PublicSettingsFragment());
                }
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