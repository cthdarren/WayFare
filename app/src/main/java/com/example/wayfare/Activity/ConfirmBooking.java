package com.example.wayfare.Activity;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.text.HtmlCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.wayfare.BuildConfig;
import com.example.wayfare.Fragment.SignUp2Fragment;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthHelper;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;

import java.util.Currency;
import java.util.Date;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ConfirmBooking extends AppCompatActivity {

    String title = null;
    String rating = null;
    String location = null;
    String price = null;
    String reviewCount = null;
    String thumbnail = null;
    String timing = null;
    String dateChosen = null;
    int startTime;
    int endTime;
    Date date;
    String remark;
    int pax, minPax, maxPax;
    String listingId, currencyPrefix, priceString;
    Double localPrice, totalPrice;
    TextView totalPriceString;

    TourListModel.TimeRange timeSlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
        MaterialToolbar toolbar = findViewById(R.id.materialToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Bundle extras = getIntent().getExtras();
        String localCurrency = new AuthHelper(getApplicationContext()).getSharedPrefsCurrencyName();
        currencyPrefix = Currency.getInstance(localCurrency).getSymbol();

        if (extras != null) {
            // Retrieve the data
            title = extras.getString("title");
            rating = extras.getString("rating");
            location = extras.getString("location");
            price = extras.getString("price");
            priceString = extras.getString("priceString");
            thumbnail = extras.getString("thumbnail");
            reviewCount = extras.getString("reviewCount");
            timing = extras.getString("timing");
            dateChosen = extras.getString("dateChosen");
            startTime = extras.getInt("startTime");
            endTime = extras.getInt("endTime");
            localPrice = extras.getDouble("localprice");
            long timeInMillis = extras.getLong("date_key");
            date = new Date(timeInMillis);
            listingId = extras.getString("listingId");
            minPax = extras.getInt("minPax");
            maxPax = extras.getInt("maxPax");
        }

        timeSlot = new TourListModel.TimeRange(startTime, endTime);

        MaterialTextView tvTitle = findViewById(R.id.title);
        MaterialTextView tvRating = findViewById(R.id.rating);
        MaterialTextView tvLocation = findViewById(R.id.location);
        MaterialTextView tvPrice = findViewById(R.id.price);
        ImageView ivThumbnail = findViewById(R.id.image);
        MaterialTextView tvReviewCount = findViewById(R.id.reviewCount);
        MaterialTextView tvTiming = findViewById(R.id.timing);
        MaterialTextView tvDateChosen = findViewById(R.id.dateformat);
        AppCompatEditText tvRemarks = findViewById(R.id.remarks);
        totalPriceString = findViewById(R.id.totalprice);

        tvTitle.setText(title);
        if (Objects.equals(reviewCount, "0"))
            tvRating.setText("No reviews yet");
        else
            tvRating.setText(rating);
        tvLocation.setText(location);
        tvPrice.setText(priceString);
        tvReviewCount.setText(reviewCount);
        tvTiming.setText(timing);
        tvDateChosen.setText(dateChosen);

        double totalPrice = localPrice * minPax;
        String paxString = "people";
        if ( minPax == 1)
            paxString = "person";
        String currencyPrefix = Currency.getInstance(localCurrency).getSymbol();
        totalPriceString.setText(HtmlCompat.fromHtml(String.format("<u>%s%.2f × %d %s = <b>%s%.2f<b><u>", currencyPrefix, localPrice, minPax, paxString, currencyPrefix, totalPrice), HtmlCompat.FROM_HTML_MODE_LEGACY));



        Glide.with(this)
                .load(thumbnail.split("\\?")[0])
                .sizeMultiplier(0.5f)
                .into(ivThumbnail);


        AppCompatImageButton decrementButton = findViewById(R.id.decrement);
        AppCompatImageButton incrementButton = findViewById(R.id.increment);
        MaterialTextView counter = findViewById(R.id.counter);
        final int[] counterValue = {minPax};

        counter.setText(String.valueOf(minPax));
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterValue[0] < maxPax) {
                    counterValue[0]++;
                    counter.setText(String.valueOf(counterValue[0]));
                    double totalPrice = localPrice * counterValue[0];
                    String paxString = "people";
                    if ( counterValue[0] == 1)
                        paxString = "person";
                    String currencyPrefix = Currency.getInstance(localCurrency).getSymbol();
                    totalPriceString.setText(HtmlCompat.fromHtml(String.format("<u>%s%.2f × %d %s = <b>%s%.2f<b><u>", currencyPrefix, localPrice, counterValue[0], paxString, currencyPrefix, totalPrice), HtmlCompat.FROM_HTML_MODE_LEGACY));

                } else {
                    Toast.makeText(ConfirmBooking.this, "Max number of persons reached", Toast.LENGTH_SHORT).show();
                }
            }
        });
        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterValue[0] > minPax) {
                    counterValue[0]--;
                    counter.setText(String.valueOf(counterValue[0]));
                    double totalPrice = localPrice * counterValue[0];
                    String paxString = "people";
                    if ( counterValue[0] == 1)
                        paxString = "person";
                    String currencyPrefix = Currency.getInstance(localCurrency).getSymbol();
                    totalPriceString.setText(HtmlCompat.fromHtml(String.format("<u>%s%.2f × %d %s = <b>%s%.2f<b><u>", currencyPrefix, localPrice, counterValue[0], paxString, currencyPrefix, totalPrice), HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else {
                    Toast.makeText(ConfirmBooking.this, "Min number of persons reached", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button button = findViewById(R.id.confirmButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                remark = tvRemarks.getText().toString();
                pax = counterValue[0];
                createBooking();
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



    public void createBooking(){
        Log.d("BUTTONS", "User tapped the confirm button");
        //TourListing listing, String userId, TimeRange bookingDuration, Date dateBooked, Double bookingPrice, int pax, String remarks
        String json = String.format("{\"listingId\":\"%s\", \"bookingDuration\":{\"startTime\":%s, \"endTime\":%s}, \"dateBooked\":\"%s\", \"bookingPrice\":%s, \"pax\":%s, \"remarks\":\"%s\"}",listingId, timeSlot.getStartTime(), timeSlot.getEndTime(), date.toInstant(), Double.valueOf(price), pax, remark);
         RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        new AuthService(this).getResponse("/booking/create", true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {
                makeToast(message);
                ConfirmBooking.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onResponse(ResponseModel json) {
                if (json.success){
                    Log.d("JSON", "onResponse: success");
                    makeToast(json.data.getAsString());
                    ConfirmBooking.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                }
                else{
                    makeToast(json.data.getAsString());
                }
            }

            public void makeToast(String msg) {

                if (ConfirmBooking.this == null) {
                    Log.d("ERROR", "ACTIVITY CONTEXT IS NULL, UNABLE TO MAKE TOAST");
                    return;
                }
                ConfirmBooking.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ConfirmBooking.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}