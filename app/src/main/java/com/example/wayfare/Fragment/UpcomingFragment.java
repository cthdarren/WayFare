package com.example.wayfare.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wayfare.Adapters.PastBookingAdapter;
import com.example.wayfare.Adapters.UpcomingBookingAdapter;
import com.example.wayfare.AlternateRecyclerViewInterface;
import com.example.wayfare.Models.BookingItemModel;
import com.example.wayfare.Models.BookingModel;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.UpcomingPastBookingResponse;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class UpcomingFragment extends Fragment implements RecyclerViewInterface, AlternateRecyclerViewInterface {

    Button bookmarksBtn;
    Button goToToursButton;
    LinearLayout noBookingsMessage;
    RecyclerView upcomingRecycler, pastRecycler;
    ProgressBar progBar;
    BottomNavigationView navBar;
    TextView pastBookingsHeader;
    List<BookingItemModel> upcomingBookings ;
    List<BookingItemModel> pastBookings ;

    public UpcomingFragment() {
    }

    public void setUpUpcomingModels(List<BookingModel> upcomingBookingModels) {
        for (BookingModel booking : upcomingBookingModels) {
            String thumbnailUrl;
            if (booking.getListing().getThumbnailUrls().length == 0)
                thumbnailUrl = "";
            else
                thumbnailUrl = booking.getListing().getThumbnailUrls()[0];
            BookingItemModel toAdd = new BookingItemModel(booking.getId(), booking.getListing().getId(), thumbnailUrl, booking.getListing().getTitle(), booking.getListing().getRegion(), booking.getBookingDuration().getStartTime(), booking.getDateBooked(), booking.getUser().getPictureUrl(), booking.getUser().getUsername(), booking.getListing().getId(), booking.getReviewed());
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
            BookingItemModel toAdd = new BookingItemModel(booking.getId(), booking.getListing().getId(), thumbnailUrl, booking.getListing().getTitle(), booking.getListing().getRegion(), booking.getBookingDuration().getStartTime(), booking.getDateBooked(), booking.getUser().getPictureUrl(), booking.getUser().getUsername(), booking.getListing().getId(), booking.getReviewed());
            pastBookings.add(toAdd);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        upcomingBookings = new ArrayList<>();
        pastBookings = new ArrayList<>();

        navBar = getActivity().findViewById(R.id.bottomNavigationView);
        progBar = getActivity().findViewById(R.id.progressBar);
        progBar.setVisibility(View.VISIBLE);

        bookmarksBtn = view.findViewById(R.id.bookmarksButton);
        noBookingsMessage = view.findViewById(R.id.noBookingsMessage);
        goToToursButton = view.findViewById(R.id.goToToursButton);
        pastBookingsHeader = view.findViewById(R.id.pastBookingsHeader);
        pastBookingsHeader.setVisibility(View.GONE);
//        upcomingBookings.add(new BookingItemModel("thumbnail", "title", 5, "today", "picurl", "test"));

        bookmarksBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Helper.goToFragmentSlideInRight(getParentFragmentManager(), R.id.container, new BookmarksFragment());
            }
        });

        goToToursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navBar.setSelectedItemId(R.id.tours);
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
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
                            if (pastBookings.size() > 0){
                                pastBookingsHeader.setVisibility(View.VISIBLE);
                            }

                            if (upcomingBookings.size() > 0){
                                upcomingRecycler.setVisibility(View.VISIBLE);
                                noBookingsMessage.setVisibility(View.GONE);

                                upcomingRecycler.getAdapter().notifyItemRangeInserted(0, upcomingBookings.size());

                                SnapHelper snapHelper = new LinearSnapHelper();
                                snapHelper.attachToRecyclerView(upcomingRecycler);
                            }
                            if (pastBookings.size() > 0){
                                //TODO same thing show error on no data
//                                upcomingRecycler.setVisibility(View.VISIBLE);
//                                noBookingsMessage.setVisibility(View.GONE);
                                pastRecycler.getAdapter().notifyItemRangeInserted(0, pastBookings.size());
                            }

                            progBar.setVisibility(View.GONE);
                        }
                    });
                }
                else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Bundle args = new Bundle();
        args.putString("listingId", pastBookings.get(position).listingUrl);
        args.putString("wayfarer", pastBookings.get(position).wayfarerUsername);
        args.putString("bookingId", pastBookings.get(position).id);
        Helper.goToFragmentSlideInRightArgs(args, getParentFragmentManager(), R.id.container, new CreateReviewFragment());
    }

    @Override
    public void onAlternateItemClick(int position) {
        Bundle args = new Bundle();
        args.putString("bookingId", upcomingBookings.get(position).id);
        Helper.goToFragmentSlideInRightArgs(args,getParentFragmentManager(), R.id.container, new ViewBookingFragment());
    }
}