package com.example.wayfare.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.widget.TextView;

=======
>>>>>>> 4b5dfec (Added skeleton of Tour Listing Page and for Adding New Tour Listings)

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wayfare.R;

public class TourListingFragment extends Fragment {
<<<<<<< HEAD

    TextView listingTitle;
    TextView listingDescription;

=======
>>>>>>> 4b5dfec (Added skeleton of Tour Listing Page and for Adding New Tour Listings)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tours, container, false);
<<<<<<< HEAD

=======
>>>>>>> 4b5dfec (Added skeleton of Tour Listing Page and for Adding New Tour Listings)
        //
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
<<<<<<< HEAD
        listingDescription =(TextView) getView().findViewById(R.id.listingDescription);
        listingTitle = (TextView) getView().findViewById(R.id.listingTitle);
=======
>>>>>>> 4b5dfec (Added skeleton of Tour Listing Page and for Adding New Tour Listings)
    }
}
