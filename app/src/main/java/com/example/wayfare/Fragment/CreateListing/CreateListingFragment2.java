package com.example.wayfare.Fragment.CreateListing;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Adapters.CategoryAdapter;
import com.example.wayfare.Models.CategoryItemModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.Utils.Helper;

import java.util.Arrays;
import java.util.List;

public class CreateListingFragment2 extends Fragment {

    Button continue_button;

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

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO change to createlisting fragment3
                Helper.goToFragmentSlideInRight(getParentFragmentManager(), R.id.container, new CreateListingFragment2());
            }
        });
        return view;
    }

}