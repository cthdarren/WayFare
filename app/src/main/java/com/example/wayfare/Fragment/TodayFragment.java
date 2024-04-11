package com.example.wayfare.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wayfare.Activity.WayfarerActivity;
import com.example.wayfare.Adapters.TodayAdapter;
import com.example.wayfare.Models.BookingModel;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.UserModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.ViewModel.UserViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class TodayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private UserViewModel userViewModel;
    private RecyclerView recyclerView;

    private ProgressBar progBar;

    private ArrayList<BookingModel> bookingModelsToday;
    private ArrayList<BookingModel> bookingModelsWeek;
    private ArrayList<BookingModel> bookingModelsMonth;

    private TodayAdapter todayAdapter;
    private TodayAdapter weekAdapter;
    private TodayAdapter monthAdapter;
    private LinearLayout noBookingMessage;

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
        noBookingMessage = view.findViewById(R.id.noBookingsMessage);
        progBar = getActivity().findViewById(R.id.progressBar);

        bookingModelsToday = new ArrayList<>();
        bookingModelsWeek = new ArrayList<>();
        bookingModelsMonth = new ArrayList<>();


        setUpBookingModels();

        todayAdapter = new TodayAdapter(bookingModelsToday);
        weekAdapter = new TodayAdapter(bookingModelsWeek);
        monthAdapter = new TodayAdapter(bookingModelsMonth);


        recyclerView = view.findViewById(R.id.hostRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));  // Assuming vertical list
        recyclerView.setAdapter(todayAdapter);


        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        UserModel userData = userViewModel.getUserProfileData();
        String userFirstName = userData.getFirstName();
        TextView user_welcome = view.findViewById(R.id.host_welcome);
        user_welcome.setText("Welcome back, " + userFirstName);

        RadioButton radioButton1 = view.findViewById(R.id.radioButton);
        RadioButton radioButton2 = view.findViewById(R.id.radioButton2);
        RadioButton radioButton3 = view.findViewById(R.id.radioButton3);


        setRadioButtonListener(radioButton1,todayAdapter);
        setRadioButtonListener(radioButton2,weekAdapter);
        setRadioButtonListener(radioButton3,monthAdapter);



    }

    public void setUpBookingModels() {
        new AuthService(getContext()).getResponse("/wayfarer/bookings", true, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {
                progBar.setVisibility(View.GONE);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(ResponseModel json) {
                if (json.success) {
                    JsonElement dataElement = json.data;
                    if (dataElement.isJsonObject()) {
                        JsonObject dataObject = dataElement.getAsJsonObject();
                        JsonArray dayArray = dataObject.getAsJsonArray("day");
                        JsonArray weekArray = dataObject.getAsJsonArray("week");
                        JsonArray monthArray = dataObject.getAsJsonArray("month");
                        setBookingLists(dayArray,bookingModelsToday);
                        setBookingLists(weekArray,bookingModelsWeek);
                        setBookingLists(monthArray,bookingModelsMonth);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.getAdapter().notifyDataSetChanged();
                            if (bookingModelsToday.size() == 0)
                                noBookingMessage.setVisibility(View.VISIBLE);
                            else
                                noBookingMessage.setVisibility(View.GONE);
                            progBar.setVisibility(View.GONE);
                        }
                    });
                }

            }
        });
    }

    public void setRadioButtonListener(RadioButton radioButton, TodayAdapter adapter){
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter == todayAdapter){
                    if (bookingModelsToday.size() == 0)
                        noBookingMessage.setVisibility(View.VISIBLE);
                    else
                        noBookingMessage.setVisibility(View.GONE);
                }
                else{
                    noBookingMessage.setVisibility(View.GONE);
                }
                recyclerView.setAdapter(adapter);
            }
        });
    }

    public void setBookingLists(JsonArray dataArray, ArrayList<BookingModel> bookingModelList){
        for (JsonElement listing : dataArray) {
            String eachString = listing.toString();
            BookingModel listingModel = new Gson().fromJson(eachString, BookingModel.class);
            bookingModelList.add(listingModel);
        }
    }



}