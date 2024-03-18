package com.example.wayfare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.wayfare.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // saves time, no need to write findbyidelement each time

        replaceFragment(new ExploreFragment());
        binding.bottomNavigationView.setBackground(null);

//        binding.bottomNavigationView.setOnItemReselectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.explore -> replaceFragment(new ExploreFragment());
//                case R.id.bookmarks -> replaceFragment(new BookmarksFragment());
//                case R.id.tours -> replaceFragment(new ToursFragment());
//                case R.id.inbox -> replaceFragment(new InboxFragment());
//                case R.id.account -> replaceFragment(new AccountFragment());
//            }
//            return true;
//        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack("name") // Name can be null
                .commit();
    }
}