package com.example.wayfare.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Button;

import com.example.wayfare.Fragment.SettingsFragment;
import com.example.wayfare.Fragment.BookmarksFragment;
import com.example.wayfare.Fragment.ExploreFragment;
import com.example.wayfare.Fragment.InboxFragment;
import com.example.wayfare.Fragment.SignInFragment;
import com.example.wayfare.R;
import com.example.wayfare.Fragment.ToursFragment;
import com.example.wayfare.databinding.ActivityMainBinding;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = (View) binding.getRoot();
        setContentView(view);
        View decorView = getWindow().getDecorView();
        replaceFragment(new SignInFragment());
        //binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.explore){
                replaceFragment(new ExploreFragment());
            } else if (item.getItemId() == R.id.bookmarks) {
                replaceFragment(new BookmarksFragment());
            } else if (item.getItemId() == R.id.tours) {
                replaceFragment(new ToursFragment());
            } else if (item.getItemId() == R.id.inbox) {
                replaceFragment(new InboxFragment());
            } else if (item.getItemId() == R.id.account) {
                replaceFragment(new SettingsFragment());
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