package com.example.wayfare.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.wayfare.Adapters.ReviewAdapter;
import com.example.wayfare.Models.ProfileModel;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.ReviewItemModel;
import com.example.wayfare.Models.UserModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.ViewModel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.CarouselStrategy;
import com.google.android.material.carousel.UncontainedCarouselStrategy;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment implements RecyclerViewInterface {

    UserViewModel userViewModel;
    ProgressBar progBar;
    BottomNavigationView navBar;
    ImageView profile_pic;
    TextView full_name;
    TextView ratings;
    TextView years_on_wayfare;
    TextView about_me;
    RecyclerView reviewRecycler;
    List<ReviewItemModel> reviewItemModels;
    public ProfileFragment(){}

    public void setupReviewModels(){
        reviewItemModels = Arrays.asList(
                new ReviewItemModel("Cool tour", "", "Jason", "Cool tour, managed to see many sights", "dafuqfuq", "1st November 2024"),
                new ReviewItemModel("Cool tour", "", "Jason", "Cool tour, managed to see many sights", "1st November 2024", "dafuqqq"),
                new ReviewItemModel("Cool tour", "", "Jason", "Cool tour, managed to see many sights", "1st November 2024", "1st November 2024"),
                new ReviewItemModel("Cool tour", "", "Jason", "Cool tour, managed to see many sights", "1st November 2024", "1st November 2024"),
                new ReviewItemModel("Cool tour", "", "Jason", "Cool tour, managed to see many sights", "1st November 2024", "1st November 2024")
        );
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        progBar = getActivity().findViewById(R.id.progressBar);
        progBar.setVisibility(View.VISIBLE);
        navBar = getActivity().findViewById(R.id.bottomNavigationView);
        profile_pic = view.findViewById(R.id.profile_picture);
        full_name = view.findViewById(R.id.full_name);
        ratings = view.findViewById(R.id.rating);
        years_on_wayfare = view.findViewById(R.id.years_on_wayfare);
        about_me = view.findViewById(R.id.about_me);
        reviewRecycler = view.findViewById(R.id.review_carousel);

        setupReviewModels();


        reviewRecycler.setAdapter(new ReviewAdapter(getContext(), reviewItemModels, this));

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(reviewRecycler);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        UserModel userData = userViewModel.getUserProfileData();

        new AuthService(getContext()).getResponse("/api/v1/profile/" + userViewModel.getUserProfileData().getUsername(), Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }); 
            }

            @Override
            public void onResponse(ResponseModel json) {
                if (json.success){
                    ProfileModel profileInfo = new Gson().fromJson(json.data, ProfileModel.class);

                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    Looper uiLooper = Looper.getMainLooper();
                    final Handler handler = new Handler(uiLooper);
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL(profileInfo.getPictureUrl());
                                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        profile_pic.setImageBitmap(image);
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            full_name.setText(profileInfo.getFirstName() + " " + profileInfo.getLastName());
                            ratings.setText(profileInfo.getAvgScore().toString() + "★");
                            years_on_wayfare.setText(String.valueOf(LocalDate.now().getYear() - LocalDate.parse(profileInfo.getDateCreated().substring(0,10)).getYear()));
                            about_me.setText(profileInfo.getAboutMe());
                        }
                    });
                }
            }
        });
        progBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(int position) {
        // TODO if you decide to make it onclickable it's here otherwise can take out
    }
}