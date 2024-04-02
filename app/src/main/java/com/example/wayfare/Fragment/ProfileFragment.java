package com.example.wayfare.Fragment;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.wayfare.Adapters.ProfileListingAdapter;
import com.example.wayfare.Adapters.ReviewAdapter;
import com.example.wayfare.Models.ListingItemModel;
import com.example.wayfare.Models.ProfileModel;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.ReviewItemModel;
import com.example.wayfare.Models.ReviewModel;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.Models.UserModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.ViewModel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment implements RecyclerViewInterface {

    String profileUsername;
    UserViewModel userViewModel;
    ImageView backButton;
    ProgressBar progBar;
    BottomNavigationView navBar;
    ImageView profile_pic;
    TextView full_name;
    TextView ratings;
    TextView reviewCount;
    TextView years_on_wayfare;
    TextView about_me;
    TextView review_title;
    RecyclerView reviewRecycler;
    RecyclerView listingRecycler;
    Button show_all_reviews_button;
    LinearLayout review_segment;
    LinearLayout listings_wrapper;
    TextView listings_wrapper_header;
    TextView confirmed_info_header;
    ImageView verification_truege;
    TextView languagesSpoken;
    List<ReviewItemModel> reviewItemModels = new ArrayList<>();
    List<ListingItemModel> listingItemModels = new ArrayList<>();


    public ProfileFragment() {
    }

    public void setupReviewModels(List<ReviewModel> reviewList) {
        for (ReviewModel review : reviewList) {
            ReviewItemModel toAdd = new ReviewItemModel(review.getTitle(), review.getUser().getPictureUrl(), review.getUser().getFirstName(), review.getReviewContent(), review.getDateCreated(), review.getDateModified());
            reviewItemModels.add(toAdd);
        }
    }

    public void setUpListingModels(List<TourListModel> listingList) {
        for (TourListModel tour : listingList) {
            String thumbnailUrl;
            if (tour.getThumbnailUrls().length == 0)
                thumbnailUrl = "";
            else
                thumbnailUrl = tour.getThumbnailUrls()[0];
            ListingItemModel toAdd = new ListingItemModel(thumbnailUrl, tour.getTitle(), tour.getRating(), tour.getReviewCount(), tour.getRegion());
            listingItemModels.add(toAdd);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profileUsername = getArguments().getString("username");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        backButton = view.findViewById(R.id.profile_back);
        progBar = getActivity().findViewById(R.id.progressBar);
        progBar.setVisibility(View.VISIBLE);
        navBar = getActivity().findViewById(R.id.bottomNavigationView);
        profile_pic = view.findViewById(R.id.profile_picture);
        full_name = view.findViewById(R.id.full_name);
        languagesSpoken = view.findViewById(R.id.languages_spoken);
        review_segment = view.findViewById(R.id.review_segment);
        review_title = view.findViewById(R.id.review_title);
        reviewCount = view.findViewById(R.id.num_reviews);
        ratings = view.findViewById(R.id.rating);
        years_on_wayfare = view.findViewById(R.id.years_on_wayfare);
        about_me = view.findViewById(R.id.about_me);
        show_all_reviews_button = view.findViewById(R.id.show_all_review_button);
        listings_wrapper = view.findViewById(R.id.listings_wrapper);
        listings_wrapper_header = view.findViewById(R.id.listing_wrapper_header);
        confirmed_info_header = view.findViewById(R.id.confirmed_info_header);
        verification_truege = view.findViewById(R.id.verification_truege);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        UserModel userData = userViewModel.getUserProfileData();

        if (userData.isVerified())
            verification_truege.setImageResource(R.drawable.done);

        if (Objects.equals(userData.getRole(), "ROLE_USER"))
            listings_wrapper.setVisibility(GONE);

        listingRecycler = view.findViewById(R.id.listing_carousel);
        listingRecycler.setAdapter(new ProfileListingAdapter(getContext(), listingItemModels, this));
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(reviewRecycler);
        if (profileUsername != null) {

            new AuthService(getContext()).getResponse("/api/v1/profile/" + profileUsername, false, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
                @Override
                public void onError(String message) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            getParentFragmentManager().popBackStack();
                            progBar.setVisibility(GONE);
                        }
                    });
                }

                @Override
                public void onResponse(ResponseModel json) {
                    if (json.success) {
                        ProfileModel profileInfo = new Gson().fromJson(json.data, ProfileModel.class);

                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        Looper uiLooper = Looper.getMainLooper();
                        final Handler handler = new Handler(uiLooper);
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Bitmap image;
                                    if (profileInfo.getPictureUrl() == null | Objects.equals(profileInfo.getPictureUrl(), ""))
                                        image = null;
                                    else {
                                        URL url = new URL(profileInfo.getPictureUrl());
                                        image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                    }
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (image != null)
                                                profile_pic.setImageBitmap(image);
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), "Unexpected Error", Toast.LENGTH_SHORT).show();
                                            getParentFragmentManager().popBackStack();
                                            progBar.setVisibility(GONE);
                                        }
                                    });
                                }
                            }
                        });

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                full_name.setText(profileInfo.getFirstName() + " " + profileInfo.getLastName());
                                reviewCount.setText(profileInfo.getReviewCount().toString());
                                languagesSpoken.setText(String.join(", ", profileInfo.getLanguagesSpoken()));
                                review_title.setText(profileInfo.getFirstName() + "'s reviews");
                                listings_wrapper_header.setText(profileInfo.getFirstName() + "'s listings");
                                confirmed_info_header.setText(profileInfo.getFirstName() + "'s confirmed information");
                                int numratings = profileInfo.getReviewCount();
                                if (numratings == 0) {
                                    ratings.setText("No ratings yet");
                                    ratings.setTextSize(16);
                                } else {
                                    ratings.setText(String.valueOf(numratings));
                                }
                                int years = LocalDate.now().getYear() - LocalDate.parse(profileInfo.getDateCreated().substring(0, 10)).getYear();
                                if (years < 1) {
                                    years_on_wayfare.setText("First year");
                                    years_on_wayfare.setTextSize(16);
                                } else {
                                    years_on_wayfare.setText(String.valueOf(years));
                                }
                                about_me.setText(profileInfo.getAboutMe());
                                show_all_reviews_button.setText(String.format("Show all %d reviews", profileInfo.getReviewCount()));
                                if (profileInfo.getReviewCount() == 0) {
                                    review_segment.setVisibility(GONE);
                                    ratings.setText("-");
                                } else {
                                    ratings.setText(String.valueOf(profileInfo.getAvgScore()) + "★");
                                }

                                setupReviewModels(profileInfo.getReviews());
                                setUpListingModels(profileInfo.getTours());

                                reviewRecycler = view.findViewById(R.id.review_carousel);
                                reviewRecycler.setAdapter(new ReviewAdapter(getContext(), reviewItemModels));

                                SnapHelper snapHelper = new LinearSnapHelper();
                                snapHelper.attachToRecyclerView(reviewRecycler);

                                listingRecycler.getAdapter().notifyDataSetChanged();

                                progBar.setVisibility(GONE);
                            }
                        });
                    }
                }
            });
        }
        else{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "No such profile", Toast.LENGTH_SHORT).show();
                    progBar.setVisibility(GONE);
                }
            });
            getParentFragmentManager().popBackStack();
        }
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