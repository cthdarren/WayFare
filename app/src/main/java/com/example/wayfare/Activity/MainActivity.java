package com.example.wayfare.Activity;

import static com.google.maps.android.Context.getApplicationContext;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.app.UiModeManager;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wayfare.Fragment.AddShortsFragment;
import com.example.wayfare.Fragment.CreateListing.CreateListingFragment2;
import com.example.wayfare.Fragment.MapFragment;
import com.example.wayfare.Fragment.Public.PublicSettingsFragment;
import com.example.wayfare.Fragment.Public.PublicUpcomingFragment;
import com.example.wayfare.Fragment.SettingsFragment;
import com.example.wayfare.Fragment.TodayFragment;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private UserViewModel viewModel;
    private ProgressBar progBar;
    private BottomNavigationView navbar;

    private boolean loggedIn;
    private boolean backing = false;

    public void setBacking(boolean value){
        backing = value;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCNmU-849bB_xLG90P8LtPjvkTXmqTHJVA");
        }

        //PlacesClient placesClient = Places.createClient(this);
        super.onCreate(savedInstanceState);

        if (new AuthHelper(getApplicationContext()).sharedPreferences.getString("Theme", "").equals("DARK")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = (View) binding.getRoot();

        setContentView(view);

        // Makes it such that when a user reclicks the navbar it doesn't refresh
        navbar = findViewById(R.id.bottomNavigationView);
        navbar.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                //DO NOTHING
            }
        });


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
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
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
                                progBar.setVisibility(View.GONE);
                            }
                        });
                    } else {
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

        if (loggedIn)
            if (new AuthHelper(getApplicationContext()).getSharedPrefsValue("WAYFARER_VIEW").equals("TRUE")){
                Intent intent = new Intent(MainActivity.this, WayfarerActivity.class);
                startActivity(intent);
                finish();
            }
        Helper.getExchangeRate(getApplicationContext());

        View decorView = getWindow().getDecorView();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0)
            replaceFragment(new ExploreFragment());

        //binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (!backing) {
            if (item.getItemId() == R.id.explore) {
                replaceFragment(new ExploreFragment());
            } else if (item.getItemId() == R.id.upcoming) {
                if (loggedIn) {
                    replaceFragment(new UpcomingFragment());
                } else {
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
                } else {
                    replaceFragment(new PublicSettingsFragment());
                }
            }
            }
            backing = false;
            return true;
        });
//    }
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                String curr = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
                String prev = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 2).getName();
                int idToGo;
                if (
                        Objects.equals(curr, "com.example.wayfare.Fragment.SettingsFragment") |
                        Objects.equals(curr, "com.example.wayfare.Fragment.Public.PublicSettingsFragment") |
                        Objects.equals(curr, "com.example.wayfare.Fragment.ToursFragment") |
                        Objects.equals(curr, "com.example.wayfare.Fragment.UpcomingFragment") |
                        Objects.equals(curr, "com.example.wayfare.Fragment.Public.PublicUpcomingFragment")
                ){
                if (prev != null) {
                    switch (prev) {
                        case "com.example.wayfare.Fragment.SettingsFragment" -> {
                            idToGo = R.id.account;
                        }
                        case "com.example.wayfare.Fragment.Public.PublicSettingsFragment" -> {
                            idToGo = R.id.account;
                        }
                        case "com.example.wayfare.Fragment.ToursFragment" -> {
                            idToGo = R.id.tours;
                        }
                        case "com.example.wayfare.Fragment.UpcomingFragment" -> {
                            idToGo = R.id.upcoming;
                        }
                        case "com.example.wayfare.Fragment.Public.PublicUpcomingFragment" -> {
                            idToGo = R.id.upcoming;
                        }
                        default -> {
                            idToGo = R.id.explore;
                        }
                    }
                    //By setting backing, navbar item will not create a new fragment on select
                    backing = true;
                    navbar.setSelectedItemId(idToGo);
                    }
                }
                getSupportFragmentManager().popBackStack();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);



//        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                updateViewForFragment();
//            }
//        });
//

    }
//    private void updateViewForFragment() {
//        List<Fragment> fragments = getSupportFragmentManager().getFragments();
//        Fragment currentFragment = null;
//        for (Fragment fragment : fragments) {
//            if (fragment.isVisible()) {
//                currentFragment = fragment;
//                break;
//            }
//        }
//        BottomNavigationView bottomNavbar = findViewById(R.id.bottomNavigationView);
//        BottomNavigationView hostNavbar = findViewById(R.id.bottomHostingNav);
//        if (currentFragment instanceof TodayFragment) {
//            bottomNavbar.setVisibility(View.GONE);
//            hostNavbar.setVisibility(View.VISIBLE);
//        }else{
//            bottomNavbar.setVisibility(View.VISIBLE);
//            hostNavbar.setVisibility(View.GONE);
//        }
//    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(fragment.getClass().getName()) // Name can be null
                .commit();
    }



    @Override
    public void onNightModeChanged(int mode) {
        super.onNightModeChanged(mode);

        // Handle night mode change here
        switch (mode) {
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is activated
                // Update UI or perform any necessary actions
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is deactivated
                // Update UI or perform any necessary actions
                break;
            // Handle other possible night mode states if needed
        }
    }


}