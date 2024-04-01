package com.example.wayfare.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wayfare.Adapters.PastBookingAdapter;
import com.example.wayfare.Adapters.ReviewAdapter;
import com.example.wayfare.Adapters.UpcomingBookingAdapter;
import com.example.wayfare.Models.BookingItemModel;
import com.example.wayfare.Models.BookingModel;
import com.example.wayfare.Models.ListingItemModel;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.ReviewItemModel;
import com.example.wayfare.Models.ReviewModel;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.Models.UpcomingPastBookingResponse;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

public class UpcomingFragment extends Fragment implements RecyclerViewInterface {

    ImageView bookmarksBtn;
    TextView Test;
    RecyclerView upcomingRecycler, pastRecycler;

    List<BookingItemModel> upcomingBookings = new ArrayList<>();
    List<BookingItemModel> pastBookings = new ArrayList<>();

    public UpcomingFragment() {
    }

    public void setUpUpcomingModels(List<BookingModel> upcomingBookingModels) {
        for (BookingModel booking : upcomingBookingModels) {
            String thumbnailUrl;
            if (booking.getListing().getThumbnailUrls().length == 0)
                thumbnailUrl = "";
            else
                thumbnailUrl = booking.getListing().getThumbnailUrls()[0];
            BookingItemModel toAdd = new BookingItemModel(thumbnailUrl, booking.getListing().getTitle(), booking.getListing().getRegion(), booking.getBookingDuration().getStartTime(), booking.getDateBooked(), booking.getUser().getPictureUrl(), booking.getUser().getUsername());
            upcomingBookings.add(toAdd);
        }
    }

    public void setupPastBookingModels(List<BookingModel> pastBookingModels) {
        for (BookingModel booking : pastBookingModels) {
            String thumbnailUrl;
            if (booking.getListing().getThumbnailUrls().length == 0)
                thumbnailUrl = "";
            else
                thumbnailUrl = booking.getListing().getThumbnailUrls()[0];
            BookingItemModel toAdd = new BookingItemModel(thumbnailUrl, booking.getListing().getTitle(), booking.getListing().getRegion(), booking.getBookingDuration().getStartTime(), booking.getDateBooked(), booking.getUser().getPictureUrl(), booking.getUser().getUsername());
            pastBookings.add(toAdd);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        bookmarksBtn = view.findViewById(R.id.bookmarks);
//        upcomingBookings.add(new BookingItemModel("thumbnail", "title", 5, "today", "picurl", "test"));

        bookmarksBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO Navigate to bookmarked bookings
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        upcomingRecycler = view.findViewById(R.id.upcomingBookingsCarousel);
        upcomingRecycler.setAdapter(new UpcomingBookingAdapter(getContext(), upcomingBookings, this));
        pastRecycler = view.findViewById(R.id.pastBookingList);
        pastRecycler.setAdapter(new PastBookingAdapter(getContext(), pastBookings, this));

        new AuthService(getContext()).getResponse("/bookings", true, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(ResponseModel json) {
                if (json.success) {
                    UpcomingPastBookingResponse response = new Gson().fromJson(json.data, UpcomingPastBookingResponse.class);
                    List<BookingModel> bookingModelList = response.upcoming();
                    List<BookingModel> pastBookingModelList = response.past();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setUpUpcomingModels(bookingModelList);
                            setupPastBookingModels(pastBookingModelList);

                            upcomingRecycler.getAdapter().notifyDataSetChanged();
                            pastRecycler.getAdapter().notifyDataSetChanged();

                            SnapHelper snapHelper = new LinearSnapHelper();
                            snapHelper.attachToRecyclerView(upcomingRecycler);
                        }
                    });


                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }
}