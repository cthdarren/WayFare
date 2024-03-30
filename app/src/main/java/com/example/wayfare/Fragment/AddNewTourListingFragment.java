package com.example.wayfare.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wayfare.R;

public class AddNewTourListingFragment extends Fragment {

    EditText tourName, tourDescription,tourLocation, startTime, endTime, tourPrice, minPax, maxPax;
    Button tourSubmitBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_tour_listing, container, false);
        tourName = view.findViewById(R.id.newTourName);
        tourDescription = view.findViewById(R.id.tourDescription);
        tourLocation = view.findViewById(R.id.tourLocation);
        startTime = view.findViewById(R.id.startTime);
        endTime = view.findViewById(R.id.endTime);
        tourPrice = view.findViewById(R.id.tourPrice);
        minPax = view.findViewById(R.id.tourMinPax);
        maxPax = view.findViewById(R.id.tourMaxPax);
        tourSubmitBtn = view.findViewById(R.id.newTourSubmitBtn);
        //
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}