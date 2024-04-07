package com.example.wayfare.Fragment.CreateListing;

import static com.google.maps.android.Context.getApplicationContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.impl.CameraFactory;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Adapters.CategoryAdapter;
import com.example.wayfare.Models.CategoryItemModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.Utils.Helper;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.model.AutocompletePrediction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateListingFragment2 extends Fragment {

    Button continue_button;
    AutocompleteSupportFragment addressAutocomplete;
    LatLng latLngAddress;
    String placeName;
    String placeAddress;
    SupportMapFragment confirmMap;

    public CreateListingFragment2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_listing_create2, container, false);
        continue_button = view.findViewById(R.id.continue_button);
        addressAutocomplete = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.addressAutocomplete);
        confirmMap = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.confirmMap);

        addressAutocomplete.setLocationBias(RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596)
        ));
        addressAutocomplete.setCountries("SG");
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

                confirmMap.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngAddress, 20));
                    }
                });
            }
        });

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO change to createlisting fragment3
                continue_button.setEnabled(false);
                Bundle args = new Bundle();
                args.putAll(getArguments());

//                args.putString("locationLatLng", latLngAddress.toString());
//                args.putString("locationName", placeAddress);
                Helper.goToFragmentSlideInRightArgs(args, getParentFragmentManager(), R.id.container, new CreateListingFragment3());
                continue_button.setEnabled(true);
            }
        });
        return view;
    }

}