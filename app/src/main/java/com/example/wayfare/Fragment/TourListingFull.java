package com.example.wayfare.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wayfare.Activity.ConfirmBooking;
import com.example.wayfare.Adapters.timingAdapter;
import com.example.wayfare.Adapters.tourListing_RecyclerViewAdapter;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.example.wayfare.timingOnItemClickedInterface;
import com.example.wayfare.tourListing_RecyclerViewInterface;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textview.MaterialTextView;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TourListingFull extends Fragment implements tourListing_RecyclerViewInterface{
    private RecyclerView recyclerView;
    public TourListingFull(){};
    String[] timingArray;
    MaterialButton button;
    timingAdapter newTimingAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tour_listing_full, container, false);
        recyclerView = view.findViewById(R.id.recyclerView2);
        //ListView list = view.findViewById(R.id.list);

        MaterialTextView tvTitle = view.findViewById(R.id.materialTextView);
        MaterialTextView tvLocation = view.findViewById(R.id.location);
        MaterialTextView tvPrice = view.findViewById(R.id.materialTextView4);
        MaterialTextView tvRating = view.findViewById(R.id.materialTextView2);
        ImageView tvImage = view.findViewById(R.id.imageView2);
        MaterialTextView tvDescription = view.findViewById(R.id.description);
        MaterialTextView tvReviewCount = view.findViewById(R.id.reviewCount);
        button = view.findViewById(R.id.bookButton);
        MaterialButton buttonDatePicker = view.findViewById(R.id.datePicker);
        MaterialTextView chosenDate = view.findViewById(R.id.chosenDate);

        Bundle args = getArguments();
        if (args != null) {

            tvTitle.setText(args.getString("title"));
            tvRating.setText(args.getString("rating"));
            tvLocation.setText(args.getString("location"));

            String priceFormat = "$" + args.getString("price") + " / person";
            tvPrice.setText(priceFormat);

            Glide.with(requireContext())
                    .load(args.getString("thumbnail")) // Load the first URL from the array
                    .into(tvImage); // Set the image to the ImageView
            tvDescription.setText(args.getString("description"));

            String reviewCountFormat = "(" + args.getString("reviewCount") + ")" + " •";
            tvReviewCount.setText(reviewCountFormat);

            timingArray = args.getStringArray("timingArray");

        }
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        newTimingAdapter = new timingAdapter(getContext(), timingArray, this);
        recyclerView.setAdapter(newTimingAdapter);
        //recyclerView.suppressLayout(true);

        MaterialToolbar toolbar = view.findViewById(R.id.materialToolbar);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });


        // constraintbuilder
        // open at curr month
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
        long february = calendar.getTimeInMillis();

        CalendarConstraints constraintsBuilder = new CalendarConstraints.Builder().setOpenAt(february).build();

        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // datepicker
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
//                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setCalendarConstraints(constraintsBuilder)
                        .build();
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date(selection));
                        chosenDate.setText(MessageFormat.format("Selected Date: {0}", date));
                    }
                });
                datePicker.show(activity.getSupportFragmentManager(), "tag");
            }
        });
        return view;
    }

    @Override
    public void onItemClick(int position) {
        Log.d("Do nothing", String.valueOf(position));
        Intent intent = new Intent(getActivity(), ConfirmBooking.class);
        intent.putExtra("title", getArguments().getString("title"));
        intent.putExtra("rating", getArguments().getString("rating"));
        intent.putExtra("location", getArguments().getString("location"));
        intent.putExtra("price", getArguments().getString("price"));
        intent.putExtra("thumbnail", getArguments().getString("thumbnail"));
        intent.putExtra("description", getArguments().getString("description"));
        intent.putExtra("reviewCount", getArguments().getString("reviewCount"));

        String timing = timingArray[position];
        intent.putExtra("timing", timing);
        startActivity(intent);
    }
}