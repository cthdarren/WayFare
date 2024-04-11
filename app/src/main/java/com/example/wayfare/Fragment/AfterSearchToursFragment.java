package com.example.wayfare.Fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Adapters.tourListing_RecyclerViewAdapter;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.tourListing_RecyclerViewInterface;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;


public class AfterSearchToursFragment extends Fragment implements tourListing_RecyclerViewInterface {
    private RecyclerView recyclerView;
    TextView searchParams;
    ProgressBar progBar;
    MaterialCardView searchBar;
    Double latitude, longitude;
    int numberPax, kmdistance;
    Long startDate, endDate;
    String region, date;
    ArrayList<TourListModel> tourListModels;
    //tourListing_RecyclerViewAdapter adapter = new tourListing_RecyclerViewAdapter(getContext(), tourListModels, this);
    // holding all models to send to adapter later on
    public AfterSearchToursFragment(){}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        tourListModels = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_tours, container, false);
        recyclerView = view.findViewById(R.id.myRecyclerView);
        searchParams = view.findViewById(R.id.searchParams);
        searchBar = view.findViewById(R.id.clickSearchBar);
        progBar = getActivity().findViewById(R.id.progressBar);
        progBar.setVisibility(View.VISIBLE);
        // Wait for the setup to complete
        if (args != null){
            region = args.getString("region", "Anywhere");
            if (Objects.equals(region, "Anywhere"))
                kmdistance = args.getInt("kmdistance", 500000);
            else
                kmdistance = args.getInt("kmdistance", 100);
            latitude = args.getDouble("latitude", 0);
            longitude = args.getDouble("longitude", 0);
            numberPax = args.getInt("numPax", 1);
            startDate = args.getLong("startDate", MaterialDatePicker.todayInUtcMilliseconds());
            endDate = args.getLong("endDate", MaterialDatePicker.todayInUtcMilliseconds());
            String startDateString = LocalDateTime.ofInstant(Instant.ofEpochMilli(startDate), ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("d MMM"));
            String endDateString = LocalDateTime.ofInstant(Instant.ofEpochMilli(endDate), ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("d MMM"));

            if (Objects.equals(startDate, endDate))
                date = startDateString;
            else
                date = startDateString + " - " + endDateString;

            String paxString = " people";
            if (numberPax == 1)
                paxString = " person";
            if (region.length() > 20)
                region = region.substring(0,20) + "...";
            searchParams.setText(String.format("%s  |  %s  |  %s", region, date, numberPax + paxString));
        }
        else{
            latitude = 0.0;
            longitude = 0.0;
            kmdistance = 50000;
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
                        recyclerView.getAdapter().notifyItemRangeInserted(0, tourListModels.size());
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
        data.putInt("minPax", tourListModels.get(position).getMinPax());
        data.putInt("maxPax", tourListModels.get(position).getMinPax());
        data.putString("userId", tourListModels.get(position).getUserId());
        data.putString("category", tourListModels.get(position).getCategory());
        data.putParcelableArrayList("timeRangeList", (ArrayList<? extends Parcelable>) tourListModels.get(position).getTimeRangeList());

        TourListingFull tourListingFullFragment = new TourListingFull();
        tourListingFullFragment.setArguments(data);

        Helper.goToFragmentSlideInRightArgs(data, getParentFragmentManager(), R.id.container, tourListingFullFragment);
    }

}
