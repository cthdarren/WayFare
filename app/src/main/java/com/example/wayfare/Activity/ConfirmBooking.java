package com.example.wayfare.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.wayfare.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;

public class ConfirmBooking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
        MaterialToolbar toolbar = findViewById(R.id.materialToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Bundle extras = getIntent().getExtras();
        String title = null;
        String rating = null;
        String location = null;
        String price = null;
        String reviewCount = null;
        String thumbnail = null;
        String timing = null;

        if (extras != null) {
            // Retrieve the data
            title = extras.getString("title");
            rating = extras.getString("rating");
            location = extras.getString("location");
            price = extras.getString("price");
            thumbnail = extras.getString("thumbnail");
            reviewCount = extras.getString("reviewCount");
            timing = extras.getString("timing");
        }

        MaterialTextView tvTitle = findViewById(R.id.title);
        MaterialTextView tvRating = findViewById(R.id.rating);
        MaterialTextView tvLocation = findViewById(R.id.location);
        MaterialTextView tvPrice = findViewById(R.id.price);
        ImageView ivThumbnail = findViewById(R.id.image);
        MaterialTextView tvReviewCount = findViewById(R.id.reviewCount);
        MaterialTextView tvTiming = findViewById(R.id.timing);

        tvTitle.setText(title);
        tvRating.setText(rating);
        tvLocation.setText(location);
        tvPrice.setText(price);
        tvReviewCount.setText(reviewCount);
        tvTiming.setText(timing);

        Glide.with(this)
                .load(thumbnail)
                .into(ivThumbnail);


        Button button = findViewById(R.id.confirmButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the confirm button");
                
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle clicks on the Home/Up button in the action bar
        if (item.getItemId() == android.R.id.home) {
            // Finish the activity
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}