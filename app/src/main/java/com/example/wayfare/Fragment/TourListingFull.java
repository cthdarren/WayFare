package com.example.wayfare.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wayfare.Activity.ConfirmBooking;
import com.example.wayfare.Adapters.timingAdapter;
import com.example.wayfare.Adapters.tourListing_RecyclerViewAdapter;
import com.example.wayfare.Models.BookmarkItemModel;
import com.example.wayfare.Models.BookmarkModel;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.timingOnItemClickedInterface;
import com.example.wayfare.tourListing_RecyclerViewInterface;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class TourListingFull extends Fragment implements tourListing_RecyclerViewInterface{
    private RecyclerView recyclerView;
    public TourListingFull(){};
    String[] timingArray;
    timingAdapter newTimingAdapter;
    String dateChosen = null;
    MaterialButton button;
    ArrayList<Integer> timeList = new ArrayList<>();
    Date date;
    String listingId;
    MaterialCheckBox bookmarkCheckbox;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tour_listing_full, container, false);
        recyclerView = view.findViewById(R.id.recyclerView2);
        //ListView list = view.findViewById(R.id.list);

        MaterialTextView tvTitle = view.findViewById(R.id.materialTextView);
        MaterialTextView tvLocation = view.findViewById(R.id.location);
        MaterialTextView tvPrice = view.findViewById(R.id.materialTextView4);
        MaterialTextView tvRating = view.findViewById(R.id.materialTextView2);
        ImageView tvImage = view.findViewById(R.id.imageView2);
        MaterialTextView tvDescription = view.findViewById(R.id.description);
        MaterialTextView tvReviewCount = view.findViewById(R.id.reviewCount);
        button = view.findViewById(R.id.bookButton);
        MaterialButton buttonDatePicker = view.findViewById(R.id.datePickerButton);
        bookmarkCheckbox = view.findViewById(R.id.bookmarkCheckbox);

        Bundle args = getArguments();
        if (args != null) {

            tvTitle.setText(args.getString("title"));
            tvRating.setText(args.getString("rating"));
            tvLocation.setText(args.getString("location"));

            String priceFormat = "$" + args.getString("price") + " / person";
            tvPrice.setText(priceFormat);

            Glide.with(requireContext())
                    .load(args.getString("thumbnail")) // Load the first URL from the array
                    .into(tvImage); // Set the image to the ImageView
            tvDescription.setText(args.getString("description"));

            String reviewCountFormat = "(" + args.getString("reviewCount") + ")" + " •";
            tvReviewCount.setText(reviewCountFormat);

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
                    updateBookmark("/bookmark");
                } else {
                    updateBookmark("/unbookmark");
                }
            }
        });

        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // datepicker
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
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
                        newTimingAdapter.notifyDataSetChanged();
                    }
                });
                datePicker.show(activity.getSupportFragmentManager(), "tag");
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkBookmarked();
    }

    @Override
    public void onItemClick(int position) {
        if (dateChosen != null) {
            Intent intent = new Intent(getActivity(), ConfirmBooking.class);
            intent.putExtra("title", getArguments().getString("title"));
            intent.putExtra("listingId", getArguments().getString("listingId"));
            intent.putExtra("rating", getArguments().getString("rating"));
            intent.putExtra("location", getArguments().getString("location"));
            intent.putExtra("price", getArguments().getString("price"));
            intent.putExtra("thumbnail", getArguments().getString("thumbnail"));
            intent.putExtra("description", getArguments().getString("description"));
            intent.putExtra("reviewCount", getArguments().getString("reviewCount"));
            intent.putExtra("minPax", getArguments().getInt("minPax"));
            intent.putExtra("maxPax", getArguments().getInt("maxPax"));

            int startingIndex = position * 2;
            if (!timeList.isEmpty()){
                timeList.subList(startingIndex, startingIndex+1);

                intent.putExtra("startTime", timeList.get(0));
                intent.putExtra("endTime", timeList.get(1));
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
    public void updateBookmark(String apiurl){
        String listingId = getArguments().getString("listingId");
        String json = String.format("{\"listingId\":\"%s\"}", listingId);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        new AuthService(getContext()).getResponse(apiurl, true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {
                makeToast(message);
            }

            @Override
            public void onResponse(ResponseModel json) {
                if (json.success){
                    Log.d("JSON", "onResponse: success");

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

    private void checkBookmarked() {
        new AuthService(getContext()).getResponse("/getbookmarks", true, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
            @Override
            public void onResponse(ResponseModel json) {
                if (json.success){
                    Type listType = new TypeToken<ArrayList<BookmarkModel>>(){}.getType();
                    List<BookmarkModel> bookmarks = new Gson().fromJson(json.data.toString(), listType);
                    String currentListingId = getArguments().getString("listingId");
                    boolean isBookmarked = bookmarks.stream().anyMatch(bookmark -> currentListingId.equals(bookmark.listing.getId()));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bookmarkCheckbox.setChecked(isBookmarked);
                        }
                    });

                }
            }

            @Override
            public void onError(String message) {
                // Do nothing (update later?)
                Log.d("BOOKMARK", "error fetching bookmarks");
            }
        });
    }
}