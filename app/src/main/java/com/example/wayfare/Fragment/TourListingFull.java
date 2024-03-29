package com.example.wayfare.Fragment;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wayfare.Adapters.timingAdapter;
import com.example.wayfare.Adapters.tourListing_RecyclerViewAdapter;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.example.wayfare.tourListing_RecyclerViewInterface;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class TourListingFull extends Fragment implements tourListing_RecyclerViewInterface{
    private RecyclerView recyclerView;
    public TourListingFull(){};
    String[] timingArray;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tour_listing_full, container, false);
        recyclerView = view.findViewById(R.id.recyclerView2);

        Bundle args = getArguments();
        if (args != null) {
            MaterialTextView tvTitle = view.findViewById(R.id.materialTextView);
            MaterialTextView tvLocation = view.findViewById(R.id.location);
            MaterialTextView tvPrice = view.findViewById(R.id.materialTextView4);
            MaterialTextView tvRating = view.findViewById(R.id.materialTextView2);
            ImageView tvImage = view.findViewById(R.id.imageView2);
            MaterialTextView tvDescription = view.findViewById(R.id.description);
            MaterialTextView tvReviewCount = view.findViewById(R.id.reviewCount);

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

//            LinearLayout timingCardContainer = view.findViewById(R.id.timingCardContainer);
            timingArray = args.getStringArray("timingArray");
//            for (int i = 0; i < timingArray.length; i++) {
//                // Inflate the CardView layout
//                View cardView = inflater.inflate(R.layout.fragment_tour_listing_full, container, false);
//                MaterialTextView tvCardTiming = view.findViewById(R.id.timingCardText);
//                tvCardTiming.setText(timingArray[i]);
//                timingCardContainer.addView(cardView);
//            }

        }
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new timingAdapter(getContext(), timingArray, this));
        //recyclerView.suppressLayout(true);
        return view;
    }

    @Override
    public void onItemClick(int position) {
        Log.d("Do nothing", "Do nothing");
    }
}