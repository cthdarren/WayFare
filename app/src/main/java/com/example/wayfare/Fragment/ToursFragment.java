package com.example.wayfare.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wayfare.Adapters.tourListing_RecyclerViewAdapter;
import com.example.wayfare.BuildConfig;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.example.wayfare.tourListing_RecyclerViewInterface;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ToursFragment extends Fragment implements tourListing_RecyclerViewInterface {
    private RecyclerView recyclerView;
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

        final CountDownLatch latch = new CountDownLatch(1);
        try {
            setUpTourListModels(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // Notify the latch that the setup is complete
                    latch.countDown();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Wait for the setup to complete
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new tourListing_RecyclerViewAdapter(getContext(), tourListModels, this));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    public void setUpTourListModels(Callback callback) throws IOException {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    final OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(BuildConfig.API_URL + "/api/v1/listing/search?latitude=1.24853&longitude=103.84483&kmdistance=30000&numberPax=2")
                            .get()
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            if (e instanceof SocketTimeoutException) {
                                e.printStackTrace();
                            } else if (e instanceof SocketException) {
                                Log.d("ERROR", "CHECK IF BACKEND SERVER IS RUNNING!");
                                e.printStackTrace();
                            }
                        }


                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String serverResponse = response.body().string();
                            System.out.println(serverResponse);
                            ResponseModel test = new Gson().fromJson(serverResponse, ResponseModel.class);
                            JsonArray peepo = test.data.getAsJsonArray();
                            for (JsonElement testi : peepo){
                                String eachString = testi.toString();
                                TourListModel testing = new Gson().fromJson(eachString, TourListModel.class);
                                tourListModels.add(testing);
                            }
                            Log.i("Tag", "it worked>");
                            callback.onResponse(call,response);
                        }
                    });
                }
            });
        }
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


        List<TourListModel.TimeRange> timeRangeList = tourListModels.get(position).getTimeRangeList();
        String[] timingArray = new String[timeRangeList.size()];
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
        }
        data.putStringArray("timingArray", timingArray);

        TourListingFull tourListingFullFragment = new TourListingFull();
        tourListingFullFragment.setArguments(data);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutFragment, tourListingFullFragment);
        fragmentTransaction.addToBackStack(null); // Optional: Add to back stack
        fragmentTransaction.commit();
    }

}