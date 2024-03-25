package com.example.wayfare.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wayfare.Adapters.tourListing_RecyclerViewAdapter;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;


import java.util.ArrayList;

public class ToursFragment extends Fragment {
    private RecyclerView recyclerView;
    ArrayList<TourListModel> tourListModels = new ArrayList<>();
    // holding all models to send to adapter later on
    int[] tourListingImages = {R.drawable.guide1, R.drawable.guide2, R.drawable.guide3, R.drawable.guide4, R.drawable.guide5};
    public ToursFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tours, container, false);
        recyclerView = view.findViewById(R.id.myRecyclerView);

        //ArrayList<TourListModel> tourListModels = new ArrayList<TourListModel>();
        //setUpTourListModels();

//         use this to test custom models
        tourListModels.add(new TourListModel("Chinatown tour", R.drawable.guide1));
        tourListModels.add(new TourListModel("Little India tour", R.drawable.guide2));
        tourListModels.add(new TourListModel("Kampong Glam tour", R.drawable.guide3));
        tourListModels.add(new TourListModel("Lau Pa Sat tour", R.drawable.guide4));
        tourListModels.add(new TourListModel("SUTD tour", R.drawable.guide5));


        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new tourListing_RecyclerViewAdapter(view.getContext(), tourListModels));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //((SearchView) view.findViewById(R.id.searchView)).setUpWithSearchBar(R.id.search_bar);
    }

    private void setUpTourListModels(){
        String[] tourListingTitles = getResources().getStringArray(R.array.tours_fragment_list_titles);
        for (int i = 0; i < tourListingTitles.length; i++){
            // note that in this implementation, the length of the array must be the same as the number of images
            tourListModels.add(new TourListModel(tourListingTitles[i], tourListingImages[i]));
        }
    }
}