package com.example.wayfare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.wayfare.Fragment.CalendarFragment;
import com.example.wayfare.Fragment.CreateListing.CreateListingFragment;
import com.example.wayfare.Fragment.HostingToursFragment;
import com.example.wayfare.Fragment.SettingsFragment;
import com.example.wayfare.Fragment.TodayFragment;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.UserModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthHelper;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.ViewModel.UserViewModel;
import com.example.wayfare.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import java.util.Objects;

public class WayfarerActivity extends AppCompatActivity {

    private UserViewModel viewModel;
    private ProgressBar progBar;
    private BottomNavigationView bottomNav;

    private boolean loggedIn;
    private boolean backing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (new AuthHelper(getApplicationContext()).sharedPreferences.getString("Theme", "").equals("DARK")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        // Hide the status bar.
        setContentView(R.layout.activity_wayfarer);


        progBar = findViewById(R.id.progressBar);
        progBar.setVisibility(View.VISIBLE);


        if (new AuthHelper(getApplicationContext()).isLoggedIn()) {
            loggedIn = true;
            viewModel = new ViewModelProvider(this).get(UserViewModel.class);
            new AuthService(getApplicationContext()).getResponse("/account", true, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
                @Override
                public void onError(String message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WayfarerActivity.this, message, Toast.LENGTH_LONG).show();
                            loggedIn = false;
                            progBar.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void onResponse(ResponseModel json) {
                    if (json.success) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                viewModel.updateUserData(new Gson().fromJson(json.data, UserModel.class));
                                if (getSupportFragmentManager().getBackStackEntryCount() == 0)
                                    replaceFragment(new TodayFragment());
                                progBar.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        Toast.makeText(WayfarerActivity.this, json.data.getAsString(), Toast.LENGTH_SHORT).show();
                        loggedIn = false;
                        progBar.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            progBar.setVisibility(View.GONE);
            loggedIn = false;
        }

        if (loggedIn == false) {
            Intent intent = new Intent(WayfarerActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        Helper.getExchangeRate();
        BottomNavigationView bottomNav = findViewById(R.id.bottomHostingNav);

        bottomNav.setOnItemSelectedListener(item -> {
            if (!backing) {
                if (item.getItemId() == R.id.hosting_today) {
                    replaceFragment(new TodayFragment());
                } else if (item.getItemId() == R.id.hosting_tour) {
                    replaceFragment(new HostingToursFragment());
                } else if (item.getItemId() == R.id.hosting_account) {
                    replaceFragment(new SettingsFragment());
                }else if(item.getItemId() == R.id.hosting_calendar){
                    replaceFragment(new CalendarFragment());
                }
            }
            backing = false;
            return true;
        });

        //Disables refreshing of fragment if you click a fragment that you're currently on already
        bottomNav.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                //DO NOTHING
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                String curr = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
                String prev = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 2).getName();
                int idToGo;
                if (
                        // if curr == one of the objects on the navbar, means you're routing to another fragment on the navbar
                        Objects.equals(curr, "com.example.wayfare.Fragment.SettingsFragment") |
                        Objects.equals(curr, "com.example.wayfare.Fragment.TodayFragment")
                ){
                    if (prev != null) {
                        switch (prev) {
                            case "com.example.wayfare.Fragment.SettingsFragment" -> {
                                idToGo = R.id.hosting_account;
                            }
                            //these names are temporary, if you create them in the future do update accordingly
                            case "com.example.wayfare.Fragment.CalendarFragment" -> {
                                idToGo = R.id.hosting_calendar;
                            }
                            //these names are temporary, if you create them in the future do update accordingly
                            case "com.example.wayfare.Fragment.HostingTourFragment" -> {
                                idToGo = R.id.hosting_tour;
                            }
                            default -> {
                                idToGo = R.id.hosting_today;
                            }
                        }
                        //By setting backing, navbar item will not create a new fragment on select
                        backing = true;
                        bottomNav.setSelectedItemId(idToGo);
                    }
                }
                getSupportFragmentManager().popBackStack();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(fragment.getClass().getName()) // Name can be null
                .commitAllowingStateLoss();
    }
}