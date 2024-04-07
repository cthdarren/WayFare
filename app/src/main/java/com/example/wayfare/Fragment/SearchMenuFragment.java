package com.example.wayfare.Fragment;

import static android.graphics.Insets.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.wayfare.R;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchMenuFragment extends Fragment {
    LatLng latLngAddress;
    String placeName;
    String placeAddress;
    Button selectAddress;
    AutocompleteSupportFragment addressAutocomplete;
    public SearchMenuFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_menu, container, false);
//        selectAddress = view.findViewById(R.id.selectAddress);
        addressAutocomplete = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.addressAutocomplete);
        addressAutocomplete.setLocationBias(RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596)
        ));
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

                Log.i("Place Selected", placeName + " " + placeAddress);
            }});

        // Retrieve a PlacesClient (previously initialized - see MainActivity)

        // Attach an Autocomplete intent to the Address 1 EditText field


        return view;
    }
}