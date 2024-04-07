package com.example.wayfare.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wayfare.Activity.MainActivity;
import com.example.wayfare.Adapters.TodayAdapter;
import com.example.wayfare.Models.BookingModel;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.Models.UserModel;
import com.example.wayfare.R;
import com.example.wayfare.ViewModel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private UserViewModel userViewModel;
    private RecyclerView recyclerView;

    private ArrayList<BookingModel> bookingModels = new ArrayList<>();

    public TodayFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);


        recyclerView = view.findViewById(R.id.hostRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));  // Assuming vertical list
        recyclerView.setAdapter(new TodayAdapter(bookingModels));

        return inflater.inflate(R.layout.fragment_today, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        UserModel userData = userViewModel.getUserProfileData();
        String userFirstName = userData.getFirstName();
        TextView user_welcome = view.findViewById(R.id.host_welcome);
        user_welcome.setText("Welcome back, " + userFirstName);

        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Handle the checked radio button here
                switch (checkedId) {
                    case 1:
                        // Handle selection of radio button 1
                        break;
                    case 2:
                        // Handle selection of radio button 2
                        break;
                    case 3:
                        // Handle selection of radio button 3
                        break;
                }
            }
        });


    }

    public void setUpBookingModels (Callback callback) throws IOException {

    }

}