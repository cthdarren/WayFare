package com.example.wayfare;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ToursFragment extends Fragment {
    private RecyclerView recyclerView;
    public ToursFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tours, container, false);
        recyclerView = view.findViewById(R.id.myRecyclerView);
        //setUpTourListModels();
        ArrayList<TourListModel> items = new ArrayList<TourListModel>();

        items.add(new TourListModel("Chinatown tour", R.drawable.guide1));
        items.add(new TourListModel("Little India tour", R.drawable.guide2));
        items.add(new TourListModel("Kampong Glam tour", R.drawable.guide3));
        items.add(new TourListModel("Lau Pa Sat tour", R.drawable.guide4));
        items.add(new TourListModel("SUTD tour", R.drawable.guide5));


        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new tourListing_RecyclerViewAdapter(view.getContext(), items));
        return view;
    }

    ArrayList<TourListModel> tourListModels = new ArrayList<>();
    // holding all models to send to adapter later on

    int[] tourListingImages = {R.drawable.ic_launcher_background};


    private void setUpTourListModels(){
        // not using for first implementation of feature
        String[] tourListingTitles = getResources().getStringArray(R.array.tours_fragment_list_titles);
        for (int i = 0; i < tourListingTitles.length; i++){
            tourListModels.add(new TourListModel(tourListingTitles[i], tourListingImages[i]));
        }
    }
}