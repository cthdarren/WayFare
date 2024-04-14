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

import com.bumptech.glide.Glide;
import com.example.wayfare.Adapters.ProfileJourneysAdapter;
import com.example.wayfare.Adapters.ProfileListingAdapter;
import com.example.wayfare.Adapters.ReviewAdapter;
import com.example.wayfare.AlternateRecyclerViewInterface;
import com.example.wayfare.Models.ListingItemModel;
import com.example.wayfare.Models.ProfileModel;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.ReviewItemModel;
import com.example.wayfare.Models.ReviewModel;
import com.example.wayfare.Models.ShortsObject;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.Models.UserModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.TertiaryRecyclerViewInterface;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.ViewModel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class ProfileFragment extends Fragment implements RecyclerViewInterface, AlternateRecyclerViewInterface, TertiaryRecyclerViewInterface {

    String profileUsername;
    UserViewModel userViewModel;
    ImageView backButton;
    ProgressBar progBar;
    BottomNavigationView navBar, hostingBar;
    ImageView profile_pic;
    TextView full_name;
    TextView ratings;
    TextView reviewCount;
    TextView years_on_wayfare;
    TextView about_me;
    TextView review_title;
    RecyclerView reviewRecycler;
    RecyclerView listingRecycler;
    RecyclerView journeyRecycler;
    Button show_all_reviews_button, edit_profile_button;
    LinearLayout review_segment, ratingBox, reviewBox, ratingDivider, reviewCountDivider;
    LinearLayout listings_wrapper, journeys_wrapper;
    TextView listings_wrapper_header;
    TextView confirmed_info_header;
    ImageView verification_truege;
    TextView languagesSpoken;
    List<ReviewItemModel> reviewItemModels = new ArrayList<>();
    List<ListingItemModel> listingItemModels = new ArrayList<>();
    List<String> journeyThumbnailUrls;
    List<ShortsObject> journeyList;
    ProfileModel profileInfo;


    public ProfileFragment() {
    }

    public void setupJourneyThumnails(List<ShortsObject> journeys) {
        for (ShortsObject journey: journeys) {
            journeyThumbnailUrls.add(journey.getThumbnailUrl());
        }

    }
    public void setupReviewModels(List<ReviewModel> reviewList) {
        for (ReviewModel review : reviewList) {
            ReviewItemModel toAdd = new ReviewItemModel(review.getTitle(),review.getUser().getUsername(), review.getUser().getPictureUrl(), review.getUser().getFirstName(), review.getReviewContent(), review.getDateCreated(), review.getDateModified());
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
        journeyThumbnailUrls = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        backButton = view.findViewById(R.id.profile_back);
        hostingBar = getActivity().findViewById(R.id.bottomHostingNav);
        journeyRecycler = view.findViewById(R.id.journeys_carousel);

        profile_pic = view.findViewById(R.id.profile_picture);
        full_name = view.findViewById(R.id.full_name);
        languagesSpoken = view.findViewById(R.id.languages_spoken);
        review_segment = view.findViewById(R.id.review_segment);
        ratingBox = view.findViewById(R.id.ratingBox);
        reviewBox = view.findViewById(R.id.reviewBox);
        ratingDivider = view.findViewById(R.id.ratingDivider);
        reviewCountDivider = view.findViewById(R.id.reviewCountDivider);
        review_title = view.findViewById(R.id.review_title);
        reviewCount = view.findViewById(R.id.num_reviews);
        ratings = view.findViewById(R.id.rating);
        years_on_wayfare = view.findViewById(R.id.years_on_wayfare);
        about_me = view.findViewById(R.id.about_me);
        show_all_reviews_button = view.findViewById(R.id.show_all_review_button);
        listings_wrapper = view.findViewById(R.id.listings_wrapper);
        journeys_wrapper = view.findViewById(R.id.journeys_wrapper);
        listings_wrapper_header = view.findViewById(R.id.listing_wrapper_header);
        confirmed_info_header = view.findViewById(R.id.confirmed_info_header);
        verification_truege = view.findViewById(R.id.verification_truege);
        edit_profile_button = view.findViewById(R.id.edit_profile_btn);

        journeyRecycler.setAdapter(new ProfileJourneysAdapter(getContext(), journeyThumbnailUrls, this));

        progBar = view.findViewById(R.id.profileProgBar);
        progBar.setVisibility(View.VISIBLE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        edit_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.goToFragmentSlideInRight(getParentFragmentManager(), R.id.container, new EditProfileFragment());
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        UserModel userData = userViewModel.getUserProfileData();
        if (userData != null) {
            if (Objects.equals(profileUsername, userData.getUsername()))
                edit_profile_button.setVisibility(View.VISIBLE);
            else
                edit_profile_button.setVisibility(GONE);
        }
        else
            edit_profile_button.setVisibility(GONE);


        listingRecycler = view.findViewById(R.id.listing_carousel);
        listingRecycler.setAdapter(new ProfileListingAdapter(getContext(), listingItemModels, this));
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(reviewRecycler);
        if (profileUsername != null) {
            CountDownLatch latch = new CountDownLatch(2);
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
                        profileInfo = new Gson().fromJson(json.data, ProfileModel.class);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(ProfileFragment.this)
                                        .load(profileInfo.getPictureUrl().split("\\?")[0])
                                        .centerCrop()
                                        .into(profile_pic);

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


                                if (profileInfo.isVerified())
                                    verification_truege.setImageResource(R.drawable.done);

                                if (Objects.equals(profileInfo.getRole(), "ROLE_USER"))
                                    listings_wrapper.setVisibility(GONE);

                                if (profileInfo.getAboutMe().isEmpty()) {
                                    about_me.setVisibility(GONE);
                                }
                                about_me.setText(profileInfo.getAboutMe());

                                setupReviewModels(profileInfo.getReviews());
                                setUpListingModels(profileInfo.getTours());

                                show_all_reviews_button.setText(String.format("Show all %d reviews", reviewItemModels.size()));

                                if (reviewItemModels.isEmpty()){
                                    review_segment.setVisibility(GONE);
                                    ratingBox.setVisibility(GONE);
                                    ratingDivider.setVisibility(GONE);
                                    reviewBox.setVisibility(GONE);
                                    reviewCountDivider.setVisibility(GONE);
                                }
                                // means he is wayfarer but has not reviews because he has only reviews from other wayfarers
                                else if (profileInfo.getReviewCount() == 0) {
                                    review_title.setText("Reviews from WayFarers");
                                    ratingBox.setVisibility(GONE);
                                    ratingDivider.setVisibility(GONE);
                                    reviewCount.setText(String.valueOf(reviewItemModels.size()));

                                } else {
                                    ratings.setText(String.format("%.2f★", profileInfo.getAvgScore()));
                                }
                                reviewRecycler = view.findViewById(R.id.review_carousel);
                                reviewRecycler.setAdapter(new ReviewAdapter(getContext(), reviewItemModels, ProfileFragment.this));

                                SnapHelper snapHelper = new LinearSnapHelper();
                                snapHelper.attachToRecyclerView(reviewRecycler);

                                listingRecycler.getAdapter().notifyItemRangeInserted(0, listingItemModels.size());

                                latch.countDown();
                                if (latch.getCount() == 0)
                                    progBar.setVisibility(GONE);
                            }
                        });
                    }
                }
            });

        new AuthService(getContext()).getResponse("/api/v1/profileshorts/" + profileUsername, false, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Failed to get Journeys" + message, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(ResponseModel json) {
                Type listType = new TypeToken<List<ShortsObject>>(){}.getType();
                journeyList = new Gson().fromJson(json.data, listType);
                setupJourneyThumnails(journeyList);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        journeyRecycler.getAdapter().notifyItemRangeChanged(0, journeyList.size());
                        latch.countDown();
                        if (latch.getCount() == 0)
                            progBar.setVisibility(GONE);
                    }
                });
            }
        });
        } else {
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
    }

    //For listings clicked
    @Override
    public void onItemClick(int position) {
        Bundle args = new Bundle();
        Bundle data = new Bundle();
        data.putString("title", profileInfo.getTours().get(position).getTitle());
        data.putString("location", profileInfo.getTours().get(position).getRegion());
        data.putString("rating", String.valueOf(profileInfo.getTours().get(position).getRating()));
        data.putString("price", String.valueOf(profileInfo.getTours().get(position).getPrice()));
        data.putStringArray("thumbnailUrls", profileInfo.getTours().get(position).getThumbnailUrls());
        data.putString("description", profileInfo.getTours().get(position).getDescription());
        data.putString("reviewCount", String.valueOf(profileInfo.getTours().get(position).getReviewCount()));
        data.putString("listingId", profileInfo.getTours().get(position).getId());
        data.putInt("minPax", profileInfo.getTours().get(position).getMinPax());
        data.putInt("maxPax", profileInfo.getTours().get(position).getMaxPax());
        data.putString("userId", profileInfo.getTours().get(position).getUserId());
        data.putString("category", profileInfo.getTours().get(position).getCategory());
        data.putParcelableArrayList("timeRangeList", new ArrayList<>(profileInfo.getTours().get(position).getTimeRangeList()));

        TourListingFull tourListingFullFragment = new TourListingFull();
        tourListingFullFragment.setArguments(data);

        Helper.goToFragmentSlideInRightArgs(data, getParentFragmentManager(), R.id.container, tourListingFullFragment);
    }

    //For reviews clicked
    @Override
    public void onAlternateItemClick(int position) {
        Bundle args = new Bundle();
        args.putString("username", reviewItemModels.get(position).username);
        Helper.goToFragmentSlideInRightArgs(args, getParentFragmentManager(), R.id.container, new ProfileFragment());
    }

    @Override
    public void onTertiaryItemClick(int position) {
        Bundle args = new Bundle();
        args.putString("journeyId", journeyList.get(position).getId());
        Helper.goToFragmentSlideInRightArgs(args, getParentFragmentManager(), R.id.container, new SingularJourneyFragment());
    }
}