package com.example.wayfare.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wayfare.Adapters.tourListing_RecyclerViewAdapter;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class TourListingFull extends Fragment {
    private RecyclerView recyclerView;
    ArrayList<TourListModel> tourListModels = new ArrayList<>();
    public TourListingFull(){};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tour_listing_full, container, false);

        Bundle args = getArguments();
        if (args != null) {
            MaterialTextView tvTitle = view.findViewById(R.id.materialTextView);
            MaterialTextView tvLocation = view.findViewById(R.id.location);
            MaterialTextView tvPrice = view.findViewById(R.id.materialTextView4);
            MaterialTextView tvRating = view.findViewById(R.id.materialTextView2);
            ImageView tvImage = view.findViewById(R.id.imageView2);

            tvTitle.setText(args.getString("title"));
            tvRating.setText(args.getString("rating"));
            tvLocation.setText(args.getString("location"));
            tvPrice.setText(args.getString("price"));
            Glide.with(requireContext())
                    .load(args.getString("thumbnail")) // Load the first URL from the array
                    .into(tvImage); // Set the image to the ImageView
        }


//        recyclerView = view.findViewById(R.id.myRecyclerView2);
//        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        recyclerView.setAdapter(new tourListing_RecyclerViewAdapter(getContext(), tourListModels));
        return view;
    }
}