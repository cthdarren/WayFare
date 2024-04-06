package com.example.wayfare.Fragment.CreateListing;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Adapters.CategoryAdapter;
import com.example.wayfare.Fragment.SignUp2Fragment;
import com.example.wayfare.Models.CategoryItemModel;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;

import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CreateListingFragment1 extends Fragment implements RecyclerViewInterface {

    RecyclerView categoryRecycler;
    List<CategoryItemModel> categoryItemModels;
    Button continue_button;


    public void setupCategoryModels(Context context) {
        categoryItemModels = Arrays.asList(
                new CategoryItemModel("Arts and Culture", context.getDrawable(R.drawable.settings_icon)),
                new CategoryItemModel("Entertainment", context.getDrawable(R.drawable.settings_icon)),
                new CategoryItemModel("Food and Drink", context.getDrawable(R.drawable.settings_icon)),
                new CategoryItemModel("Sports", context.getDrawable(R.drawable.settings_icon)),
                new CategoryItemModel("Tours", context.getDrawable(R.drawable.settings_icon)),
                new CategoryItemModel("Sightseeing", context.getDrawable(R.drawable.settings_icon)),
                new CategoryItemModel("Wellness", context.getDrawable(R.drawable.settings_icon)),
                new CategoryItemModel("Nature and Outdoors", context.getDrawable(R.drawable.settings_icon))
        );
    }

    public CreateListingFragment1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing_create1, container, false);
        continue_button = view.findViewById(R.id.continue_button);
        categoryRecycler = view.findViewById(R.id.categoryRecycler);

        setupCategoryModels(getContext());
        categoryRecycler.setAdapter(new CategoryAdapter(getContext(), categoryItemModels, this));
        categoryRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.goToFragmentSlideInRight(getParentFragmentManager(), R.id.container, new CreateListingFragment2());
            }
        });
        return view;
    }

    @Override
    public void onItemClick(int position) {
        System.out.println(position);
    }
}