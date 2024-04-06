package com.example.wayfare.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.wayfare.Fragment.CreateListing.CreateListingFragment;
import com.example.wayfare.R;
import com.example.wayfare.Utils.Helper;

public class HostingToursFragment extends Fragment {

    public HostingToursFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.hosting_fragment_tours, container, false);
        ImageView createListingBtn = view.findViewById(R.id.createListingButton);
        createListingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.goToFragmentSlideInRight(getParentFragmentManager(), R.id.container, new CreateListingFragment());
            }
        });
        return view;
    }
}