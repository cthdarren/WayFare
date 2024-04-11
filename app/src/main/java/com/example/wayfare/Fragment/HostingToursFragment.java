package com.example.wayfare.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Adapters.YourListingsAdapter;
import com.example.wayfare.Adapters.tourListing_RecyclerViewAdapter;
import com.example.wayfare.Fragment.CreateListing.CreateListingFragment;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.ViewModel.UserViewModel;
import com.example.wayfare.tourListing_RecyclerViewInterface;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;

public class HostingToursFragment extends Fragment  implements tourListing_RecyclerViewInterface {
    RecyclerView yourListingRecycler;
    UserViewModel userViewModel;
    ProgressBar progressBar, globalProgressBar;
    LinearLayout nolistingsmessage;
    Button createListingMessageBtn;
    ArrayList<TourListModel> tourListModels = new ArrayList<>();

    public HostingToursFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        View view = inflater.inflate(R.layout.hosting_fragment_tours, container, false);
        ImageView createListingBtn = view.findViewById(R.id.createListingButton);
        progressBar = view.findViewById(R.id.yourListingsProgBar);
        globalProgressBar = getActivity().findViewById(R.id.progressBar);
        nolistingsmessage = view.findViewById(R.id.nolistingsmessage);
        createListingMessageBtn = view.findViewById(R.id.createListingmessagebutton);

        yourListingRecycler = view.findViewById(R.id.yourListingsRecycler);
        yourListingRecycler.setAdapter(new YourListingsAdapter(getContext(),tourListModels, this));
        yourListingRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        setupTourListModels();

        createListingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.goToFragmentSlideInRight(getParentFragmentManager(), R.id.container, new CreateListingFragment());
            }
        });

        createListingMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.goToFragmentSlideInRight(getParentFragmentManager(), R.id.container, new CreateListingFragment());
            }
        });

        return view;
    }
    public void setupTourListModels(){
        new AuthService(getContext()).getResponse("/api/v1/user/listing/" + userViewModel.getUserProfileData().getUsername(), false, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {
                makeToast(message);
            }

            @Override
            public void onResponse(ResponseModel json) {
                if (json.success){
                    JsonArray listingArray = json.data.getAsJsonArray();
                    for (JsonElement je: listingArray){
                        TourListModel toAdd = new Gson().fromJson(je, TourListModel.class);
                        tourListModels.add(toAdd);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            yourListingRecycler.getAdapter().notifyItemRangeChanged(0, tourListModels.size());
                            if (tourListModels.size() == 0) {
                                nolistingsmessage.setVisibility(View.VISIBLE);
                                yourListingRecycler.setVisibility(View.GONE);
                            }
                            else {
                                nolistingsmessage.setVisibility(View.GONE);
                                yourListingRecycler.setVisibility(View.VISIBLE);
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }
    public void makeToast(String msg) {

        if (getActivity() == null) {
            Log.d("ERROR", "ACTIVITY CONTEXT IS NULL, UNABLE TO MAKE TOAST");
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        globalProgressBar.setVisibility(View.VISIBLE);
        Bundle args = new Bundle();
        args.putString("listingId", tourListModels.get(position).getId());
        Helper.goToFragmentSlideInRightArgs(args, getParentFragmentManager(), R.id.container, new EditListingFragment());
    }
}