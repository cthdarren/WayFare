package com.example.wayfare.Fragment.CreateListing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.wayfare.R;
import com.example.wayfare.Utils.Helper;
import com.google.android.material.textfield.TextInputEditText;

public class CreateListingFragment5 extends Fragment {

    Button continue_button;

    TextInputEditText listingDesc, listingTitle;

    public CreateListingFragment5() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing_create5, container, false);
        continue_button = view.findViewById(R.id.continue_button);
        listingDesc = view.findViewById(R.id.listingDescription);
        listingTitle = view.findViewById(R.id.listingTitle);
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = getArguments();
                args.putString("description", listingDesc.getText().toString());
                args.putString("description", listingTitle.getText().toString());
                //TODO change to createlisting fragment3
                continue_button.setEnabled(false);
                Helper.goToFragmentSlideInRightArgs(args, getParentFragmentManager(), R.id.container, new CreateListingFragment6());
                continue_button.setEnabled(true);
            }
        });
        return view;
    }

}