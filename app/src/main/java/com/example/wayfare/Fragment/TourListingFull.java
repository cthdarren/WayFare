package com.example.wayfare.Fragment;

import static android.view.View.GONE;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wayfare.Activity.ConfirmBooking;
import com.example.wayfare.Adapters.ListingPicturesAdapter;
import com.example.wayfare.Adapters.ReviewAdapter;
import com.example.wayfare.Adapters.ViewBookingAdapter;
import com.example.wayfare.Adapters.timingAdapter;
import com.example.wayfare.Adapters.tourListing_RecyclerViewAdapter;
import com.example.wayfare.Models.ProfileModel;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.Utils.AuthHelper;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.timingOnItemClickedInterface;
import com.example.wayfare.tourListing_RecyclerViewInterface;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.FullScreenCarouselStrategy;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class TourListingFull extends Fragment implements tourListing_RecyclerViewInterface{
    private RecyclerView recyclerView;
    public TourListingFull(){};
    ProfileModel profileInfo;
    String[] timingArray;
    timingAdapter newTimingAdapter;
    String dateChosen = null;
    MaterialButton button;
    ArrayList<Integer> timeList = new ArrayList<>();
    Date date;
    String listingId;
    TextView ratings;
    TextView reviewCount_user;
    TextView years_on_wayfare;
    ProgressBar progBar;
    TextView username;
    ImageView profile_pic;
    String profileId;
    LinearLayout guideInfo;
    RecyclerView listing_image_carousel;
    MaterialTextView tvPrice;
    Double localPrice;
    List<String> urlList = new ArrayList<>();
    ImageView categoryicon;
    TextView categoryname;
    Button report;
    public void setupThumbnails(List<String> thumbnailUrls){
        for (String url: thumbnailUrls){
            urlList.add(url);
        }
        listing_image_carousel.getAdapter().notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tour_listing_full, container, false);
        recyclerView = view.findViewById(R.id.recyclerView2);
        //ListView list = view.findViewById(R.id.list);

        MaterialTextView tvTitle = view.findViewById(R.id.materialTextView);
        MaterialTextView tvLocation = view.findViewById(R.id.location);
        tvPrice = view.findViewById(R.id.materialTextView4);
        MaterialTextView tvRating = view.findViewById(R.id.materialTextView2);
        listing_image_carousel= view.findViewById(R.id.listing_image_carousel);
        MaterialTextView tvDescription = view.findViewById(R.id.description);
        MaterialTextView tvReviewCount = view.findViewById(R.id.reviewCount);
        button = view.findViewById(R.id.bookButton);
        MaterialButton buttonDatePicker = view.findViewById(R.id.datePickerButton);
        MaterialCheckBox bookmarkCheckbox = view.findViewById(R.id.bookmarkCheckbox);
        reviewCount_user = view.findViewById(R.id.num_reviews);
        ratings = view.findViewById(R.id.rating);
        years_on_wayfare = view.findViewById(R.id.years_on_wayfare);
        categoryicon = view.findViewById(R.id.categoryicon);
        categoryname = view.findViewById(R.id.categoryname);

        username = view.findViewById(R.id.user_greeting);
        progBar = view.findViewById(R.id.settingsProgBar);
        progBar.setVisibility(View.VISIBLE);
        profile_pic = view.findViewById(R.id.user_profile_picture);
        guideInfo = view.findViewById(R.id.userCard);
        report = view.findViewById(R.id.report);

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "A report has been sent to our moderating team. Thank you.", Toast.LENGTH_SHORT).show();
            }
        });

        Bundle args = getArguments();
        if (args != null) {

            tvTitle.setText(args.getString("title"));
            if (args.getString("rating").equals("0.0")) {
                tvRating.setText("No reviews yet");
                tvReviewCount.setText(" •");
            } else {
                tvRating.setText(args.getString("rating"));
                String reviewCountFormat = "(" + args.getString("reviewCount") + ")" + " •";
                tvReviewCount.setText(reviewCountFormat);
            }

            categoryname.setText(Helper.enumToCategoryName(args.getString("category")));
            switch (categoryname.getText().toString()) {
                case "Art and Culture":
                    categoryicon.setImageResource(R.drawable.arts);
                    break;
                case "Entertainment":
                    categoryicon.setImageResource(R.drawable.entertainment);
                    break;
                case "Sports":
                    categoryicon.setImageResource(R.drawable.sports);
                    break;
                case "Food and Drink":
                    categoryicon.setImageResource(R.drawable.food);
                    break;
                case "Tours":
                    categoryicon.setImageResource(R.drawable.tours);
                    break;
                case "Sightseeing":
                    categoryicon.setImageResource(R.drawable.sightseeing);
                    break;
                case "Wellness":
                    categoryicon.setImageResource(R.drawable.wellness);
                    break;
                case "Nature and Outdoors":
                    categoryicon.setImageResource(R.drawable.nature);
                    break;
                default:
                    break;
            }
            tvLocation.setText(args.getString("location"));

            String localCurrency = new AuthHelper(getContext()).getSharedPrefsCurrencyName();
            String currencyPrefix = Currency.getInstance(localCurrency).getSymbol();
            localPrice = Helper.exchangeToLocal(Double.parseDouble(args.getString("price")), localCurrency);
            tvPrice.setText(Html.fromHtml(String.format("<u><b>%s %.2f</b> per person</u>", currencyPrefix, localPrice), Html.FROM_HTML_MODE_LEGACY));

            listing_image_carousel.setAdapter(new ViewBookingAdapter(getContext(), urlList));
            listing_image_carousel.setLayoutManager(new CarouselLayoutManager(new FullScreenCarouselStrategy()));
            SnapHelper snapHelper = new CarouselSnapHelper();
            snapHelper.attachToRecyclerView(listing_image_carousel);

            setupThumbnails(Arrays.asList(args.getStringArray("thumbnailUrls")));

            tvDescription.setText(args.getString("description"));

            ArrayList<TourListModel.TimeRange> timeRangeList = args.getParcelableArrayList("timeRangeList");
            timingArray = new String[timeRangeList.size()];
            for (int i = 0; i < timeRangeList.size(); i++){
                String startTime;
                String endTime;
                int startTimeInt = timeRangeList.get(i).getStartTime();
                int endTimeInt = timeRangeList.get(i).getEndTime();
                startTime = Helper.convert24to12(startTimeInt);
                endTime = Helper.convert24to12(endTimeInt);

                timingArray[i] = startTime + " - " + endTime;
                timeList.add(startTimeInt);
                timeList.add(endTimeInt);
            }
            listingId = args.getString("listingId");
            profileId = args.getString("userId");
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        newTimingAdapter = new timingAdapter(getContext(), timingArray, this);
        recyclerView.setAdapter(newTimingAdapter);
        //recyclerView.suppressLayout(true);

        MaterialToolbar toolbar = view.findViewById(R.id.materialToolbar);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });


        // constraintbuilder
        // open at curr month
        Calendar calendar = Calendar.getInstance();
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        calendar.setTimeInMillis(today);
        long startOfDay = calendar.getTimeInMillis();

        CalendarConstraints constraintsBuilder = new CalendarConstraints.Builder()
                .setStart(startOfDay)
                .setOpenAt(startOfDay)
                .setValidator(DateValidatorPointForward.now())
                .build();

        bookmarkCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    createBookmark();
                } else {
                    // do nothing for now
                }
            }
        });

        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // datepicker
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
//                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setCalendarConstraints(constraintsBuilder)
                        .build();
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        date = new Date(selection);
                        dateChosen = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(new Date(selection));
                        buttonDatePicker.setText(dateChosen);
                        newTimingAdapter.isButtonEnabled = true;

                        newTimingAdapter.dateChosen = Instant.ofEpochMilli(selection);
                        newTimingAdapter.notifyDataSetChanged();
                    }
                });
                datePicker.show(activity.getSupportFragmentManager(), "tag");
            }
        });

        guideInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle username = new Bundle();
                username.putString("username", profileInfo.getUsername());
                ProfileFragment pf = new ProfileFragment();
                pf.setArguments(username);
                Helper.goToFragmentSlideInRight(getParentFragmentManager(), R.id.container, pf);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new AuthService(getContext()).getResponse("/api/v1/profileid/" + profileId, true, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
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
                            Glide.with(TourListingFull.this)
                                    .load(profileInfo.getPictureUrl().split("\\?")[0])
                                    .centerCrop()
                                    .into(profile_pic);

                            username.setText(profileInfo.getFirstName());
                            int years = LocalDate.now().getYear() - LocalDate.parse(profileInfo.getDateCreated().substring(0, 10)).getYear();
                            if (years < 1) {
                                years_on_wayfare.setText("First year");
                                years_on_wayfare.setTextSize(16);
                            } else {
                                years_on_wayfare.setText(String.valueOf(years));
                            }
                            if (profileInfo.getReviewCount() == 0) {
                                ratings.setText("-");
                            } else {
                                ratings.setText(String.valueOf(profileInfo.getAvgScore()) + "★");
                                reviewCount_user.setText(profileInfo.getReviewCount().toString());
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        if (dateChosen != null) {
            Intent intent = new Intent(getActivity(), ConfirmBooking.class);
            intent.putExtra("title", getArguments().getString("title"));
            intent.putExtra("listingId", getArguments().getString("listingId"));
            intent.putExtra("rating", getArguments().getString("rating"));
            intent.putExtra("location", getArguments().getString("location"));
            intent.putExtra("priceString", tvPrice.getText().toString());
            intent.putExtra("price", getArguments().getString("price"));
            intent.putExtra("thumbnail", getArguments().getStringArray("thumbnailUrls")[0]);
            intent.putExtra("localprice", localPrice);
            intent.putExtra("description", getArguments().getString("description"));
            intent.putExtra("reviewCount", getArguments().getString("reviewCount"));
            intent.putExtra("minPax", getArguments().getInt("minPax"));
            intent.putExtra("maxPax", getArguments().getInt("maxPax"));

            int startingIndex = position * 2;
            if (!timeList.isEmpty()){
                intent.putExtra("startTime", timeList.get(startingIndex));
                intent.putExtra("endTime", timeList.get(startingIndex+1));
            }

            String timing = timingArray[position];
            intent.putExtra("timing", timing);

            intent.putExtra("dateChosen", dateChosen);
            intent.putExtra("date_key", date.getTime());
            startActivity(intent);
        } else {
            Log.d("Do nothing", String.valueOf(position));
        }
    }
    public void createBookmark(){
        String listingId = getArguments().getString("listingId");
        String json = String.format("{\"listingId\":\"%s\"}", listingId);
         RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        new AuthService(getContext()).getResponse("/bookmark", true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {
                makeToast(message);
            }

            @Override
            public void onResponse(ResponseModel json) {
                if (json.success){
                    Log.d("JSON", "onResponse: success");
                    makeToast(json.data.getAsString());
                }
                else{
                    makeToast(json.data.getAsString());
                }
            }

            public void makeToast(String msg) {

                if (getActivity() == null) {
                    Log.d("ERROR", "FRAGMENT CONTEXT IS NULL, UNABLE TO MAKE TOAST");
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}