package com.example.wayfare.Fragment.CreateListing;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Adapters.ListingTimeSlotAdapter;
import com.example.wayfare.Models.TimeSlotItemModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.Utils.Helper;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.List;

public class CreateListingFragment4 extends Fragment implements RecyclerViewInterface {

    Button continue_button, addTimeSlotBtn;
    RecyclerView timeSlotRecycler;
    TextView changeStartTime, changeEndTime, startTimeString, endTimeString;
    int toAddStart, toAddEnd;
    List<TimeSlotItemModel> timeSlotItemModelList = new ArrayList<TimeSlotItemModel>();

    public CreateListingFragment4() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing_create4, container, false);
        continue_button = view.findViewById(R.id.continue_button);
        addTimeSlotBtn = view.findViewById(R.id.addTimeSlotButton);
        changeStartTime = view.findViewById(R.id.changeStartTime);
        changeEndTime = view.findViewById(R.id.changeEndTime);
        startTimeString = view.findViewById(R.id.startTimeString);
        endTimeString = view.findViewById(R.id.endTimeString);
        timeSlotRecycler = view.findViewById(R.id.timeSlotRecycler);
        timeSlotRecycler.setAdapter(new ListingTimeSlotAdapter(getContext(), timeSlotItemModelList, this));
        timeSlotRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        continue_button.setEnabled(false);

        MaterialTimePicker startTimePicker =
            new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(13)
                    .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                    .setTitleText("Select start time")
                    .build();

        MaterialTimePicker endTimePicker =
            new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(13)
                    .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                    .setTitleText("Select end time")
                    .build();

        startTimePicker.addOnPositiveButtonClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int hour = startTimePicker.getHour();
                        if (hour >= 12){
                            startTimeString.setText(String.format("%d pm", hour-12));
                        }
                        else
                            startTimeString.setText(String.format("%d am", hour));

                        toAddStart = hour;
                    }
                }
        );

        endTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = endTimePicker.getHour();
                if (hour >= 12){
                    endTimeString.setText(String.format("%d pm", hour-12));
                }
                else
                    endTimeString.setText(String.format("%d am", hour));

                toAddEnd = hour;
            }
        });

        changeStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimePicker.show(getParentFragmentManager(), "nosepicker");
            }
        });

        changeEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTimePicker.show(getParentFragmentManager(), "nosepicker");
            }
        });
        addTimeSlotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSlotItemModelList.add(new TimeSlotItemModel(toAddStart, toAddEnd));
                timeSlotRecycler.getAdapter().notifyItemInserted(timeSlotItemModelList.size()- 1);
                continue_button.setEnabled(true);
            }
        });
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = getArguments();
                args.putParcelableArrayList("timeslots", (ArrayList<? extends Parcelable>) timeSlotItemModelList);
                continue_button.setEnabled(false);
                Helper.goToFragmentSlideInRightArgs(args, getParentFragmentManager(), R.id.container, new CreateListingFragment5());
                continue_button.setEnabled(true);
            }
        });
        return view;
    }

    @Override
    public void onItemClick(int position) {
        timeSlotItemModelList.remove(position);
        timeSlotRecycler.getAdapter().notifyItemRemoved(position);
        if (timeSlotItemModelList.size() == 0)
            continue_button.setEnabled(false);
    }
}