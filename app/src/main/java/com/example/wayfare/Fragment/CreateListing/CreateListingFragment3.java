package com.example.wayfare.Fragment.CreateListing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.wayfare.R;
import com.example.wayfare.Utils.Helper;

public class CreateListingFragment3 extends Fragment {

    Button continue_button;

    public CreateListingFragment3() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing_create3, container, false);
        continue_button = view.findViewById(R.id.continue_button);

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO change to createlisting fragment3
                Helper.goToFragmentSlideInRight(getParentFragmentManager(), R.id.container, new CreateListingFragment4());
            }
        });
        return view;
    }

}