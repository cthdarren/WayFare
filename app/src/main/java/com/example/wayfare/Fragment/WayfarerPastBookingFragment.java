package com.example.wayfare.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wayfare.Adapters.PastBookingAdapter;
import com.example.wayfare.Adapters.WayfarerPastBoookingAdapter;
import com.example.wayfare.Models.BookingItemModel;
import com.example.wayfare.Models.BookingModel;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WayfarerPastBookingFragment extends Fragment implements RecyclerViewInterface {

    RecyclerView pastBookingsList;
    LinearLayout noBookingsMessage;
    List<BookingItemModel> pastBookingItemModels;



    public void setupPastBookingModels(List<BookingModel> bookingModelList){
        for (BookingModel booking : bookingModelList) {
            String thumbnailUrl;
            if (booking.getListing().getThumbnailUrls().length == 0)
                thumbnailUrl = "";
            else
                thumbnailUrl = booking.getListing().getThumbnailUrls()[0];
            BookingItemModel toAdd = new BookingItemModel(booking.getId(), booking.getListing().getId(), thumbnailUrl, booking.getListing().getTitle(), booking.getListing().getRegion(), booking.getBookingDuration().getStartTime(), booking.getDateBooked(), booking.getUser().getPictureUrl(), booking.getUser().getUsername(), booking.getListing().getId(), booking.getReviewed());
            pastBookingItemModels.add(toAdd);
        }
    }
    public WayfarerPastBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wayfarer_past_bookings, container, false);
        pastBookingItemModels = new ArrayList<>();
        pastBookingsList = view.findViewById(R.id.pastBookingList);
        noBookingsMessage = view.findViewById(R.id.noBookingsMessage);
        pastBookingsList.setAdapter(new WayfarerPastBoookingAdapter(getContext(), pastBookingItemModels, this));

        new AuthService(getContext()).getResponse("/wayfarer/pastbookings", true, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {
                makeToast(message);
            }

            @Override
            public void onResponse(ResponseModel json) {
                if (json.success) {
                    Type listType = new TypeToken<List<BookingModel>>() {}.getType();
                    List<BookingModel> bookingModelList = new Gson().fromJson(json.data, listType);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setupPastBookingModels(bookingModelList);
                            if (pastBookingItemModels.size() > 0) {
                                noBookingsMessage.setVisibility(View.GONE);
                            } else {
                                noBookingsMessage.setVisibility(View.VISIBLE);
                            }
                            pastBookingsList.getAdapter().notifyItemRangeInserted(0, bookingModelList.size());
                        }
                    });
                }
            }
        });
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    public void makeToast(String msg) {

        if (getActivity() == null) {
            Log.d("ERROR", "ACTIVITY CONTEXT IS NULL, UNABLE TO MAKE TOAST");
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Bundle args = new Bundle();
        args.putString("listingId", pastBookingItemModels.get(position).listingUrl);
        args.putString("wayfarer", pastBookingItemModels.get(position).wayfarerUsername);
        args.putString("bookingId", pastBookingItemModels.get(position).id);
        Helper.goToFragmentSlideInRightArgs(args, getParentFragmentManager(), R.id.container, new CreateReviewFragment());
    }
}