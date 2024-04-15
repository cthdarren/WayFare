package com.example.wayfare.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ctc.wstx.shaded.msv_core.reader.trex.ZeroOrMoreState;
import com.example.wayfare.Adapters.tourListing_RecyclerViewAdapter;
import com.example.wayfare.Comparators.TourListHotComparator;
import com.example.wayfare.Comparators.TourListNewComparator;
import com.example.wayfare.Comparators.TourListTrendingComparator;
import com.example.wayfare.Models.BookingModel;
import com.example.wayfare.Models.BookmarkModel;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.tourListing_RecyclerViewInterface;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.http.GET;


public class AfterSearchToursFragment extends Fragment implements tourListing_RecyclerViewInterface {
    private RecyclerView recyclerView;
    TextView searchParams;
    ProgressBar progBar;
    MaterialCardView searchBar;
    Double latitude, longitude;
    Integer numberPax, kmdistance;
    String region, date;
    TabLayout tabLayout;
    Long startDateLong, endDateLong;
    ArrayList<TourListModel> tourListModels = new ArrayList<>();
    //tourListing_RecyclerViewAdapter adapter = new tourListing_RecyclerViewAdapter(getContext(), tourListModels, this);
    // holding all models to send to adapter later on
    public AfterSearchToursFragment(){}

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
        tabLayout = view.findViewById(R.id.tabLayout);
        progBar = getActivity().findViewById(R.id.progressBar);
        progBar.setVisibility(View.VISIBLE);
        // Wait for the setup to complete
        if (args != null){
            region = args.getString("region");
            latitude = args.getDouble("latitude", -1);
            longitude = args.getDouble("longitude", -1);
            kmdistance = args.getInt("kmdistance", -1);
            numberPax = args.getInt("numPax", -1);
            startDateLong = args.getLong("startDate", -1);
            endDateLong = args.getLong("endDate", -1);

            if (region == null)
                region = "Anywhere";
            else
                region = region.substring(0,27) + "...";

            if (startDateLong != -1 & endDateLong != -1){
                if (startDateLong.equals(endDateLong)){
                    date = LocalDateTime.ofInstant(Instant.ofEpochMilli(startDateLong), ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("d MMM"));
                }
                else
                    date = LocalDateTime.ofInstant(Instant.ofEpochMilli(startDateLong), ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("d MMM")) + " - " + LocalDateTime.ofInstant(Instant.ofEpochMilli(endDateLong), ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("d MMM"));;
            }
            else
                date = "Anytime";

            String paxString = numberPax + " people";
            if (numberPax == -1){
                paxString = "Anybody";
            }
            else if (numberPax == 1)
                paxString = numberPax + " person";

            searchParams.setText(String.format("%s | %s | %s", region, date, paxString));
        }
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.goToFullScreenFragmentFromTop(getParentFragmentManager(), new SearchMenuFragment());
            }
        });

        new AuthService(getContext()).getResponse("/getbookmarks", true, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {
                setupBookmarks(new ArrayList<>());
            }

            @Override
            public void onResponse(ResponseModel json) {
                Type listType = new TypeToken<ArrayList<BookmarkModel>>(){}.getType();
                List<BookmarkModel> bookmarks = new Gson().fromJson(json.data, listType);
                setupBookmarks(bookmarks);
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        // Hot Tab
                        tourListModels.sort(new TourListHotComparator());
                        recyclerView.getAdapter().notifyItemRangeChanged(0, tourListModels.size());
                        break;
                    case 1:
                        // Trending Tab
                        tourListModels.sort(new TourListTrendingComparator());
                        recyclerView.getAdapter().notifyItemRangeChanged(0, tourListModels.size());
                        break;
                    case 2:
                        // New Tab
                        tourListModels.sort(new TourListNewComparator());
                        recyclerView.getAdapter().notifyItemRangeChanged(0, tourListModels.size());
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                System.out.println(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Do nothing
            }
        });

        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setupTourListings(){
        if (longitude == -1)
            longitude = null;
        if (latitude== -1)
            latitude = null;
        if (kmdistance == -1)
            kmdistance = null;
        if (numberPax == -1)
            numberPax = null;
        if (startDateLong == -1)
            startDateLong = null;
        if (endDateLong == -1)
            endDateLong = null;

        new AuthService(getContext()).getListingResponseParams(longitude, latitude,kmdistance,numberPax, startDateLong, endDateLong, new AuthService.ResponseListener() {
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
                        listingModel.setDateCreatedInstant(Instant.parse(listingModel.getDateCreated()));
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

    public void setupBookmarks(List<BookmarkModel> bookmarkModels){
        List<String> bookmarkedListingIds = new ArrayList<>();
        for (BookmarkModel bookmark: bookmarkModels){
            bookmarkedListingIds.add(bookmark.listing.getId());
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(new tourListing_RecyclerViewAdapter(getContext(), tourListModels, bookmarkedListingIds, AfterSearchToursFragment.this));
                setupTourListings();
            }
        });

    }    @Override
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