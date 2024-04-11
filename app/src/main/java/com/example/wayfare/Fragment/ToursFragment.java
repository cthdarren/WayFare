package com.example.wayfare.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wayfare.Adapters.tourListing_RecyclerViewAdapter;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.TimeSlotItemModel;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.tourListing_RecyclerViewInterface;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;


public class ToursFragment extends Fragment implements tourListing_RecyclerViewInterface {
    private RecyclerView recyclerView;
    TextView searchParams;
    ProgressBar progBar;
    MaterialCardView searchBar;
    Double latitude, longitude;
    int numberPax, kmdistance;
    String region, date;
    ArrayList<TourListModel> tourListModels = new ArrayList<>();
    //tourListing_RecyclerViewAdapter adapter = new tourListing_RecyclerViewAdapter(getContext(), tourListModels, this);
    // holding all models to send to adapter later on
    public ToursFragment(){}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tours, container, false);
        recyclerView = view.findViewById(R.id.myRecyclerView);
        searchBar = view.findViewById(R.id.clickSearchBar);
        searchParams = view.findViewById(R.id.searchParams);
        progBar = getActivity().findViewById(R.id.progressBar);
        progBar.setVisibility(View.VISIBLE);
        // Wait for the setup to complete
        if (args != null){
            region = args.getString("region", "Anywhere");
            latitude = args.getDouble("latitude", 0);
            longitude = args.getDouble("longitude", 0);
            kmdistance = args.getInt("kmdistance", 100);
            numberPax = args.getInt("numPax", 1);
            date = args.getString("date", "Any day");
            String paxString = " people";
            if (numberPax == 1)
                paxString = " person";
            searchParams.setText(String.format("%s | %s | %s", region, date, numberPax + paxString));
        }
        else{
            latitude = 0.0;
            longitude = 0.0;
            kmdistance = 20050;
            numberPax = 1;
        }
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
        new AuthService(getContext()).getResponse(String.format("/api/v1/listing/search?latitude=%f&longitude=%f&kmdistance=%d&numberPax=%d",latitude, longitude, kmdistance, numberPax), false, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
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
        data.putStringArray("thumbnailUrls", tourListModels.get(position).getThumbnailUrls());
        data.putString("description", tourListModels.get(position).getDescription());
        data.putString("reviewCount", String.valueOf(tourListModels.get(position).getReviewCount()));
        data.putString("listingId", tourListModels.get(position).getId());
        data.putInt("minPax", tourListModels.get(position).getMinPax());
        data.putInt("maxPax", tourListModels.get(position).getMaxPax());
        data.putString("userId", tourListModels.get(position).getUserId());
        data.putString("category", tourListModels.get(position).getCategory());
        data.putParcelableArrayList("timeRangeList", (ArrayList<? extends Parcelable>) tourListModels.get(position).getTimeRangeList());

        TourListingFull tourListingFullFragment = new TourListingFull();
        tourListingFullFragment.setArguments(data);

        Helper.goToFragmentSlideInRightArgs(data, getParentFragmentManager(), R.id.container, tourListingFullFragment);
    }

}
