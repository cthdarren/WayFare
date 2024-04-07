package com.example.wayfare.Fragment.CreateListing;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.wayfare.R;
import com.example.wayfare.Utils.Helper;

public class CreateListingFragment3 extends Fragment {

    Button continue_button;
    ImageView minusMin, addMin, minusMax, addMax;
    TextView minPax, maxPax;

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
        minPax = view.findViewById(R.id.minParticipants);
        maxPax = view.findViewById(R.id.maxParticipants);
        minusMin = view.findViewById(R.id.minusMinParticipants);
        minusMax = view.findViewById(R.id.minusMaxParticipants);
        addMin = view.findViewById(R.id.addMinParticipants);
        addMax = view.findViewById(R.id.addMaxParticipants);

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continue_button.setEnabled(false);
                Bundle args = getArguments();
                args.putString("minPax", minPax.getText().toString());
                args.putString("maxPax", maxPax.getText().toString());
                Helper.goToFragmentSlideInRightArgs(args, getParentFragmentManager(), R.id.container, new CreateListingFragment4());
                continue_button.setEnabled(true);
            }
        });

        minusMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curr = Integer.parseInt(minPax.getText().toString());
                if (curr > 1) {
                    String newVal = String.valueOf(curr - 1);
                    minPax.setText(newVal);
                }
            }
        });

        addMin.setOnClickListener(new View.OnClickListener() {
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

        minusMax.setOnClickListener(new View.OnClickListener() {
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

        addMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curr = Integer.parseInt(maxPax.getText().toString());
                String newVal = String.valueOf(curr + 1);
                maxPax.setText(newVal);
            }
        });

        return view;
    }

}