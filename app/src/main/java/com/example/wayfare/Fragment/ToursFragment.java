package com.example.wayfare.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.wayfare.Adapters.tourListing_RecyclerViewAdapter;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.tourListing_RecyclerViewInterface;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;


public class ToursFragment extends Fragment implements tourListing_RecyclerViewInterface {
    private RecyclerView recyclerView;
    ProgressBar progBar;
    MaterialCardView searchBar;
    ArrayList<TourListModel> tourListModels = new ArrayList<>();
    //tourListing_RecyclerViewAdapter adapter = new tourListing_RecyclerViewAdapter(getContext(), tourListModels, this);
    // holding all models to send to adapter later on
    public ToursFragment(){}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tours, container, false);
        recyclerView = view.findViewById(R.id.myRecyclerView);
        searchBar = view.findViewById(R.id.clickSearchBar);
        progBar = getActivity().findViewById(R.id.progressBar);
        progBar.setVisibility(View.VISIBLE);
        // Wait for the setup to complete

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.goToFullScreenFragmentFromTop(getParentFragmentManager(), new SearchMenuFragment());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new tourListing_RecyclerViewAdapter(getContext(), tourListModels, this));

        setupTourListings();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setupTourListings(){
        new AuthService(getContext()).getResponse("/api/v1/listing/search?latitude=1.24853&longitude=103.84483&kmdistance=30000&numberPax=2", false, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(ResponseModel json) {
                if (json.success) {
                    JsonArray listingArray = json.data.getAsJsonArray();
                    for (JsonElement listing : listingArray) {
                        String eachString = listing.toString();
                        TourListModel listingModel = new Gson().fromJson(eachString, TourListModel.class);
                        tourListModels.add(listingModel);
                    }

                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.getAdapter().notifyDataSetChanged();
                        progBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Bundle data = new Bundle();

        data.putString("title", tourListModels.get(position).getTitle());
        data.putString("location", tourListModels.get(position).getRegion());
        data.putString("rating", String.valueOf(tourListModels.get(position).getRating()));
        data.putString("price", String.valueOf(tourListModels.get(position).getPrice()));
        data.putString("thumbnail", tourListModels.get(position).getThumbnailUrls()[0]);
        data.putString("description", tourListModels.get(position).getDescription());
        data.putString("reviewCount", String.valueOf(tourListModels.get(position).getReviewCount()));
        data.putString("listingId", tourListModels.get(position).getId());


        List<TourListModel.TimeRange> timeRangeList = tourListModels.get(position).getTimeRangeList();
        String[] timingArray = new String[timeRangeList.size()];
        ArrayList<Integer> timeList = new ArrayList<>();
        for (int i = 0; i < timeRangeList.size(); i++){
            String startTime;
            String endTime;
            int startTimeInt = timeRangeList.get(i).getStartTime();
            int endTimeInt = timeRangeList.get(i).getEndTime();
            if (startTimeInt > 12) {
                startTimeInt = startTimeInt - 12;
                endTimeInt = endTimeInt - 12;
                startTime = startTimeInt + "PM";
                endTime = endTimeInt + "PM";
            } else {
                startTime = startTimeInt + "AM";
                endTime = endTimeInt + "AM";
            }
            timingArray[i] = startTime + " - " + endTime;
            timeList.add(startTimeInt);
            timeList.add(endTimeInt);
        }
        data.putStringArray("timingArray", timingArray);
        data.putIntegerArrayList("timeList", timeList);

        TourListingFull tourListingFullFragment = new TourListingFull();
        tourListingFullFragment.setArguments(data);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutFragment, tourListingFullFragment);
        fragmentTransaction.addToBackStack(null); // Optional: Add to back stack
        fragmentTransaction.commit();
    }

}
