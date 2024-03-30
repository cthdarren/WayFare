package com.example.wayfare.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wayfare.BuildConfig;
import com.example.wayfare.R;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddNewTourListingFragment extends Fragment {

    EditText tourName, tourDescription,tourLocation, startTime, endTime, tourPrice, minPax, maxPax,
            tourCategory, thumbnailUrls;
    Button tourSubmitBtn;
    List<String> thumbnailArray;
    List<String> timeRangeList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_tour_listing, container, false);
        tourName = view.findViewById(R.id.newTourName);
        tourDescription = view.findViewById(R.id.tourDescription);
        tourLocation = view.findViewById(R.id.tourLocation);
        startTime = view.findViewById(R.id.startTime);
        endTime = view.findViewById(R.id.endTime);
        tourPrice = view.findViewById(R.id.tourPrice);
        minPax = view.findViewById(R.id.tourMinPax);
        maxPax = view.findViewById(R.id.tourMaxPax);
        tourCategory = view.findViewById(R.id.tourCategory);
        tourSubmitBtn = view.findViewById(R.id.newTourSubmitBtn);
        thumbnailUrls = view.findViewById(R.id.thumbNailUrls);
        thumbnailArray = Arrays.asList(thumbnailUrls.getText().toString().split(","));
        timeRangeList = new ArrayList<>();
        timeRangeList.add(startTime.getText().toString());
        timeRangeList.add(endTime.getText().toString());
        tourSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    submitNewListing();
                } catch(IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        //
        return view;
    }

    public void submitNewListing() throws IOException {
        if (getActivity() != null) {
            final OkHttpClient client = new OkHttpClient();
            String json_request = String.format(("{\"title\":\"%s\", \"description\":\"%s\", " +
                    "\"thumbnailUrls\":\"%s\", \"category\":\"%s\", \"location\":\"%s\", " +
                    "\"timeRangeList\":\"%s\", \"price\":\"%s\", \"maxPax\":\"%s\", " +
                    "\"minPax\":\"%s\"}"),
                    tourName.getText().toString(), tourDescription.getText().toString(),
                    thumbnailArray.toString(), tourCategory.getText().toString(),
                    tourLocation.getText().toString(), timeRangeList.toString(),
                    tourPrice.getText().toString(), maxPax.getText().toString(),
                    minPax.getText().toString()
            );
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), json_request);
            Request request = new Request.Builder().url(BuildConfig.API_URL + "/wayfarer/listing/create")
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (e instanceof SocketTimeoutException) {
                        makeToast("Request Timed Out");
                        e.printStackTrace();
                    } else if (e instanceof SocketException) {
                        makeToast("Server Error");
                        Log.d("ERROR", "CHECK IF BACKEND SERVER IS RUNNING!");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String serverResponse = response.body().string();
                    System.out.println(serverResponse);
                    Log.i("Tag", "it worked>");
                }
            });
        }
    }

    public void makeToast(String msg) {

        if (getActivity() == null) {
            Log.d("ERROR", "ACTIVITY CONTEXT IS NULL, UNABLE TO MAKE TOAST");
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}