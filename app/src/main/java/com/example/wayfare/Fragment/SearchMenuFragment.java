package com.example.wayfare.Fragment;

import static android.graphics.Insets.add;

import static com.google.android.material.datepicker.MaterialDatePicker.thisMonthInUtcMilliseconds;
import static com.google.android.material.datepicker.MaterialDatePicker.todayInUtcMilliseconds;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.example.wayfare.R;
import com.example.wayfare.Utils.Helper;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SearchMenuFragment extends Fragment {
    LatLng latLngAddress;
    Button searchButton;
    TextView numPax;
    String startDateString, endDateString;
    Button dateRangeSelect;
    Long startDate, endDate;
    ImageView minusPax, addPax;
    int numPaxInt = 1;
    String region = null;
    AutocompleteSupportFragment addressAutocomplete;
    public SearchMenuFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_menu, container, false);
        dateRangeSelect = view.findViewById(R.id.dateRangeSelect);
        searchButton = view.findViewById(R.id.searchBtn);
        numPax = view.findViewById(R.id.numPax);
        minusPax = view.findViewById(R.id.minusPax);
        addPax = view.findViewById(R.id.addPax);

        addressAutocomplete = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.addressAutocomplete);
        addressAutocomplete.setLocationBias(RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596)
        ));
        addressAutocomplete.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.ID
        ));
        addressAutocomplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Log.i("An error occurred (CreateListingFragment2)", status.toString());
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                latLngAddress = place.getLatLng();
                region = place.getName();
            }});
        startDate = todayInUtcMilliseconds();
        endDate = todayInUtcMilliseconds();
        startDateString = LocalDateTime.ofInstant(Instant.ofEpochMilli(startDate), ZoneOffset.UTC).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
        endDateString = LocalDateTime.ofInstant(Instant.ofEpochMilli(endDate), ZoneOffset.UTC).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
        dateRangeSelect.setText(startDateString + " - " + endDateString);

        MaterialDatePicker dateRangePicker =
                MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Select dates")
                        .setSelection(
                                new Pair<>(
                                        MaterialDatePicker.todayInUtcMilliseconds(),
                                        MaterialDatePicker.todayInUtcMilliseconds()
                                )
                        )
                        .build();

        dateRangeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateRangePicker.show(getParentFragmentManager(), "deadpicker");
            }
        });
        dateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long,Long> selection) {
                startDate = selection.first;
                endDate = selection.second;
                startDateString = LocalDateTime.ofInstant(Instant.ofEpochMilli(startDate), ZoneOffset.UTC).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
                endDateString = LocalDateTime.ofInstant(Instant.ofEpochMilli(endDate), ZoneOffset.UTC).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
                dateRangeSelect.setText(startDateString + " - " + endDateString);
            }
        });

        addPax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numPaxInt ++;
                numPax.setText(String.valueOf(numPaxInt));
            }
        });

        minusPax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numPaxInt > 1) {
                    numPaxInt--;
                    numPax.setText(String.valueOf(numPaxInt));
                }
            }
        });

        numPax.setText(String.valueOf(numPaxInt));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
                Bundle args = new Bundle();
                if (latLngAddress != null) {
                    args.putDouble("latitude", latLngAddress.latitude);
                    args.putDouble("longitude", latLngAddress.longitude);
                }
                args.putLong("startDate", startDate);
                args.putLong("endDate", endDate);
                args.putInt("numPax", numPaxInt);
                if (region != null)
                    args.putString("region", region);
                Helper.goToFragmentSlideInRightArgs(args, getParentFragmentManager(), R.id.container, new AfterSearchToursFragment());
            }
        });


        return view;
    }
}