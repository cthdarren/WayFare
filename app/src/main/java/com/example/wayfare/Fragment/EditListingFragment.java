package com.example.wayfare.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.FullScreenCarouselStrategy;
import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class EditListingFragment extends Fragment implements RecyclerViewInterface, OnMapReadyCallback {

    ProgressBar globalProgressBar;
    TourListModel currentListing;
    String listingId, currencyName, currencyPrefix;
    RecyclerView time_slot_recycler;
    EditText edit_listing_title, edit_listing_description, edit_listing_price;
    TextView minPax, maxPax, edit_listing_category;
    ImageView minusMinPax, minusMaxPax, addMinPax, addMaxPax;
    LinearLayout category_card;
    ImageView back_btn;
    Button add_time_slot, delete_listing, edit_listing_save;
    AutocompleteSupportFragment addressAutocomplete;
    LatLng latLngAddress;
    String placeName;
    String placeAddress;
    SupportMapFragment confirmMap;

    List<TimeSlotItemModel> timeSlotList = new ArrayList<>();
    List<String> pictureUrls = new ArrayList<>();

    public EditListingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        globalProgressBar = getActivity().findViewById(R.id.progressBar);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        currencyName = new AuthHelper(getContext()).getSharedPrefsCurrencyName();
        currencyPrefix = Currency.getInstance(currencyName).getSymbol();

        listingId = getArguments().getString("listingId");
        final String[] listItems = new String[]{"Art and Culture",
                "Entertainment",
                "Food and Drink",
                "Sports",
                "Tours",
                "Sightseeing",
                "Wellness",
                "Nature and Outdoors"};

        View view = inflater.inflate(R.layout.fragment_edit_listing, container, false);
        back_btn = view.findViewById(R.id.back_btn);
        add_time_slot = view.findViewById(R.id.add_time_slot);
        delete_listing = view.findViewById(R.id.delete_listing);
        edit_listing_save = view.findViewById(R.id.edit_listing_save);

        edit_listing_title = view.findViewById(R.id.edit_listing_title);
        edit_listing_description = view.findViewById(R.id.edit_listing_description);
        edit_listing_price = view.findViewById(R.id.edit_listing_price);
        edit_listing_category = view.findViewById(R.id.edit_listing_category);
        category_card = view.findViewById(R.id.category_card);
        minPax = view.findViewById(R.id.minPax);
        maxPax = view.findViewById(R.id.maxPax);
        minusMinPax = view.findViewById(R.id.minusMinPax);
        minusMaxPax = view.findViewById(R.id.minusMaxPax);
        addMinPax = view.findViewById(R.id.addMinPax);
        addMaxPax = view.findViewById(R.id.addMaxPax);

        time_slot_recycler = view.findViewById(R.id.time_slot_recycler);
        time_slot_recycler.setAdapter(new ListingTimeSlotAdapter(getContext(), timeSlotList, this));
        time_slot_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        edit_listing_price.setText(currencyPrefix);
        edit_listing_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith(currencyPrefix)){
                    edit_listing_price.setText(currencyPrefix);
                    Selection.setSelection(edit_listing_price.getText(), edit_listing_price.getText().length());
                }
                else {
                    if (s.toString().substring(currencyPrefix.length(), s.length()).length() == 0) {
                        edit_listing_save.setEnabled(false);
                    } else
                        edit_listing_save.setEnabled(true);
                }
            }
        });

        //=============================================== BASIC BUTTONS ==================================================
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


        //=============================================== BASIC BUTTONS ==================================================

        // ===================================== MAPS STUFF =============================================================
        addressAutocomplete = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.addressAutocomplete);
        confirmMap = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.confirmMap);

        addressAutocomplete.setLocationBias(RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596)
        ));
        //addressAutocomplete.setCountries("SG");
        addressAutocomplete.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME,
                Place.Field.ADDRESS, Place.Field.ID));
        addressAutocomplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Log.i("An error occurred (CreateListingFragment2)", status.toString());
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                latLngAddress = place.getLatLng();
                placeName = place.getName();
                placeAddress = place.getAddress();

                confirmMap.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngAddress, 15));
                    }
                });
            }
        });
        // ===================================== MAPS STUFF =============================================================


        new AuthService(getContext()).getResponse("/api/v1/listing/" + listingId, false, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {
                makeToast(message);
            }

            @Override
            public void onResponse(ResponseModel json) {
                currentListing= new Gson().fromJson(json.data, TourListModel.class);
                initializeMap();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        edit_listing_title.setText(currentListing.getTitle());
                        edit_listing_description.setText(currentListing.getDescription());
                        minPax.setText(String.valueOf(currentListing.getMinPax()));
                        maxPax.setText(String.valueOf(currentListing.getMaxPax()));
                        double localPrice = Helper.exchangeToLocal(currentListing.getPrice(), currencyName);
                        edit_listing_price.setText(String.format("%s%.2f", currencyPrefix, localPrice));

                        for (TourListModel.TimeRange tr : currentListing.getTimeRangeList()) {
                            timeSlotList.add(new TimeSlotItemModel(tr.startTime, tr.endTime));
                        }
                        time_slot_recycler.getAdapter().notifyDataSetChanged();


                        final int[] checkedItem = {0};
                        for (int i = 0; i< listItems.length; i++){
                            if (Objects.equals(currentListing.getCategory(), Helper.categoryNameToEnum(listItems[i]))) {
                                checkedItem[0] = i;
                                edit_listing_category.setText(listItems[i]);
                            }
                        }

                        category_card.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                                        .setTitle("Select category")
                                        .setSingleChoiceItems(listItems, checkedItem[0], (dialog, which) -> {
                                            checkedItem[0] = which;
                                            edit_listing_category.setText(listItems[which]);
                                            dialog.dismiss();
                                        })
                                        .setNegativeButton("Cancel", (dialog, which) -> {
                                        });
                                AlertDialog languagesDialog = builder.create();
                                languagesDialog.show();
                            }
                        });
                    }
                });
            }
        });

        edit_listing_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gsonparser = new Gson();
                Double usdPrice = Helper.exchangeToUsd(
                        Integer.parseInt(edit_listing_price.getText().toString().substring(currencyPrefix.length())),
                        new AuthHelper((getContext())).getSharedPrefsCurrencyName()
                );
                @SuppressLint("DefaultLocale") String json = String.format("""
                                {"title": "%s", "description": "%s", "thumbnailUrls": %s,"category": "%s", "location": {"x":%f, "y":%f}, "timeRangeList": %s, "price": "%.2f", "maxPax": %d, "minPax": %d}""",
                        edit_listing_title.getText().toString(), edit_listing_description.getText().toString(), gsonparser.toJson(currentListing.getThumbnailUrls()),
                        Helper.categoryNameToEnum(edit_listing_category.getText().toString()), latLngAddress.longitude, latLngAddress.latitude,
                        gsonparser.toJson(timeSlotList), usdPrice,
                        Integer.parseInt(maxPax.getText().toString()),
                        Integer.parseInt(minPax.getText().toString()));
                RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
                new AuthService(getContext()).getResponse("/wayfarer/listing/edit/" + currentListing.getId(), true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
                    @Override
                    public void onError(String message) {
                        makeToast(message);
                    }

                    @Override
                    public void onResponse(ResponseModel json) {
                        makeToast("Successfully updated listing");
                        getParentFragmentManager().popBackStack();
                        getParentFragmentManager().popBackStack();
                        Helper.goToFragment(getParentFragmentManager(), R.id.flFragment, new HostingToursFragment());
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

    public void initializeMap() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                confirmMap.getMapAsync(EditListingFragment.this);
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
        globalProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng meeting = new LatLng(currentListing.getLocation().getY(), currentListing.getLocation().getX());
        googleMap.addMarker(new MarkerOptions()
                .position(meeting)
                .title("Meeting Points"));
        googleMap.getUiSettings().setAllGesturesEnabled(false);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(meeting, 15));

        globalProgressBar.setVisibility(View.GONE);
    }
}