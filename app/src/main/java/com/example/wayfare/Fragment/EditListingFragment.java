package com.example.wayfare.Fragment;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.example.wayfare.Adapters.ListingTimeSlotAdapter;
import com.example.wayfare.Adapters.ViewBookingAdapter;
import com.example.wayfare.Models.BookingModel;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.TimeSlotItemModel;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.Utils.AuthHelper;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.FullScreenCarouselStrategy;
import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import okhttp3.RequestBody;

public class EditListingFragment extends Fragment implements RecyclerViewInterface {

    String listingId, currencyName;
    RecyclerView time_slot_recycler;
    EditText edit_listing_title, edit_listing_description, edit_listing_price;
    TextView minPax, maxPax;
    ImageView minusMinPax, minusMaxPax, addMinPax, addMaxPax;
    LinearLayout category_card;
    ImageView back_btn;
    Button add_time_slot, delete_listing;

    List<TimeSlotItemModel> timeSlotList = new ArrayList<>();
    List<String> pictureUrls = new ArrayList<>();

    public EditListingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        currencyName = new AuthHelper(getContext()).getSharedPrefsCurrencyName();
        // Inflate the layout for this fragment
        listingId = getArguments().getString("listingId");
        View view = inflater.inflate(R.layout.fragment_edit_listing, container, false);
        back_btn = view.findViewById(R.id.back_btn);
        add_time_slot = view.findViewById(R.id.add_time_slot);
        delete_listing = view.findViewById(R.id.delete_listing);

        edit_listing_title = view.findViewById(R.id.edit_listing_title);
        edit_listing_description = view.findViewById(R.id.edit_listing_description);
        edit_listing_price = view.findViewById(R.id.edit_listing_price);
        category_card = view.findViewById(R.id.category_card);
        minPax = view.findViewById(R.id.minPax);
        maxPax = view.findViewById(R.id.maxPax);
        minusMinPax= view.findViewById(R.id.minusMinPax);
        minusMaxPax = view.findViewById(R.id.minusMaxPax);
        addMinPax = view.findViewById(R.id.addMinPax);
        addMaxPax = view.findViewById(R.id.addMaxPax);


        time_slot_recycler = view.findViewById(R.id.time_slot_recycler);

        time_slot_recycler.setAdapter(new ListingTimeSlotAdapter(getContext(), timeSlotList, this));
        time_slot_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });


        minusMinPax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curr = Integer.parseInt(minPax.getText().toString());
                if (curr > 1) {
                    String newVal = String.valueOf(curr - 1);
                    minPax.setText(newVal);
                }
            }
        });

        addMinPax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curr = Integer.parseInt(minPax.getText().toString());
                String newVal = String.valueOf(curr + 1);
                minPax.setText(newVal);
                //if min pax is same as maxPax, add one to maxPax also, because min cannot be > max
                if (curr == Integer.parseInt(maxPax.getText().toString()))
                    maxPax.setText(newVal);
            }
        });

        minusMaxPax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curr = Integer.parseInt(maxPax.getText().toString());
                if (curr > 1) {
                    String newVal = String.valueOf(curr - 1);
                    maxPax.setText(newVal);
                    // if max pax is same as min pax, reducing one would make maxPax < minPax, which doesn't make sense
                    if (curr == Integer.parseInt(minPax.getText().toString()))
                        minPax.setText(newVal);
                }
            }
        });

        addMaxPax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curr = Integer.parseInt(maxPax.getText().toString());
                String newVal = String.valueOf(curr + 1);
                maxPax.setText(newVal);
            }
        });

        new AuthService(getContext()).getResponse("/api/v1/listing/" + listingId, false, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {
                makeToast(message);
            }

            @Override
            public void onResponse(ResponseModel json) {
                TourListModel listing = new Gson().fromJson(json.data, TourListModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        edit_listing_title.setText(listing.getTitle());
                        edit_listing_description.setText(listing.getDescription());
                        double localPrice = Helper.exchangeToLocal(listing.getPrice(),currencyName);
                        edit_listing_price.setText(String.format("%.2f",localPrice));
                        for (TourListModel.TimeRange tr: listing.getTimeRangeList()){
                            timeSlotList.add(new TimeSlotItemModel(tr.startTime, tr.endTime));
                        }
                        time_slot_recycler.getAdapter().notifyDataSetChanged();
                    }
                });
            }
        });

        return view;
    }

    public void makeToast(String msg) {

        if (getActivity() == null) {
            Log.d("ERROR", "ACTIVITY CONTEXT IS NULL, UNABLE TO MAKE TOAST");
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }
}