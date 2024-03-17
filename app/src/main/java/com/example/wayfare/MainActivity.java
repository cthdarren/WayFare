package com.example.wayfare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.wayfare.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // saves time, no need to write findbyidelement each time

        binding.
    }

}