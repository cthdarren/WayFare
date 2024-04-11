package com.example.wayfare.Fragment;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.example.wayfare.Adapters.ViewBookingAdapter;
import com.example.wayfare.Models.BookingModel;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthHelper;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.FullScreenCarouselStrategy;
import com.google.android.material.carousel.HeroCarouselStrategy;
import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import okhttp3.RequestBody;

public class ViewBookingFragment extends Fragment implements OnMapReadyCallback {

    RecyclerView listing_image_carousel;
    BookingModel currentBooking;
    ImageView back_btn;
    TextView booking_title, booking_meeting_details, booking_remarks, booking_price_total, wayfarer_name, years_on_wayfare, with_string, booking_time;
    ImageView wayfarer_pic;
    Button cancel_booking;
    LinearLayout verified, wayfarer_card;
    SupportMapFragment mapFragment;
    List<String> pictureUrls = new ArrayList<>();

    public ViewBookingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_booking, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        back_btn = view.findViewById(R.id.back_btn);
        cancel_booking = view.findViewById(R.id.cancel_booking);
        booking_title = view.findViewById(R.id.bookingTitle);
        with_string = view.findViewById(R.id.with_string);
        booking_meeting_details = view.findViewById(R.id.booking_meeting_details);
        booking_time = view.findViewById(R.id.booking_time);
        booking_remarks = view.findViewById(R.id.booking_remarks);
        booking_price_total = view.findViewById(R.id.booking_price_total);
        wayfarer_name = view.findViewById(R.id.wayfarer_name);
        wayfarer_pic = view.findViewById(R.id.wayfarer_pic);
        years_on_wayfare = view.findViewById(R.id.years_on_wayfare);
        verified = view.findViewById(R.id.wayfarer_verified);
        wayfarer_card = view.findViewById(R.id.wayfarer_card);

        listing_image_carousel = view.findViewById(R.id.listing_image_carousel);
        listing_image_carousel.setLayoutManager(new CarouselLayoutManager(new FullScreenCarouselStrategy()));
        listing_image_carousel.setAdapter(new ViewBookingAdapter(getContext(), pictureUrls));

        SnapHelper snapHelper = new CarouselSnapHelper();
        snapHelper.attachToRecyclerView(listing_image_carousel);

        new AuthService(getContext()).getResponse("/booking/" + getArguments().getString("bookingId"), true, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {
                makeToast("Unable to get booking details");
                getParentFragmentManager().popBackStack();
            }

            @Override
            public void onResponse(ResponseModel json) {
                currentBooking = new Gson().fromJson(json.data, BookingModel.class);
                initializeMap();
                pictureUrls.addAll(Arrays.asList(currentBooking.getListing().getThumbnailUrls()));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listing_image_carousel.getAdapter().notifyDataSetChanged();

                        wayfarer_card.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle args = new Bundle();
                                args.putString("username", currentBooking.getUser().getUsername());
                                Helper.goToFragmentSlideInRightArgs(args, getParentFragmentManager(), R.id.container, new ProfileFragment());
                            }
                        });

                        cancel_booking.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                RequestBody body = RequestBody.create(null, new byte[]{});
                                new AuthService(getContext()).getResponse("/booking/delete/" + currentBooking.getId(), true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
                                    @Override
                                    public void onError(String message) {
                                        makeToast(message);
                                    }

                                    @Override
                                    public void onResponse(ResponseModel json) {
                                        makeToast("Booking successfully cancelled");
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                getParentFragmentManager().popBackStack();
                                                getParentFragmentManager().popBackStack();
                                                Helper.goToFragment(getParentFragmentManager(), R.id.flFragment, new UpcomingFragment());
                                            }
                                        });
                                    }
                                });
                            }
                        });

                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getContext(), Locale.ENGLISH);
                        TourListModel.Location location = currentBooking.getListing().getLocation();
                        try {
                            addresses = geocoder.getFromLocation(location.getY(), location.getX(), 1);
                        } catch (IOException e) {
                            throw new RuntimeException("ERROR DECODING COORDINATES: " + e.getMessage());
                        }
                        String address = addresses.get(0).getAddressLine(0);

                        Double indivPrice = currentBooking.getBookingPrice();
                        int totalPax = currentBooking.getPax();
                        String localCurrency = new AuthHelper(getContext()).getSharedPrefsCurrencyName();

                        String paxString = "people";
                        if (totalPax == 1)
                            paxString = "person";

                        int yearsOnWayfare = LocalDate.now().getYear() - LocalDate.parse(currentBooking.getUser().getDateCreated().substring(0, 10)).getYear();
                        String wayfareYearString;
                        if (yearsOnWayfare == 0)
                            wayfareYearString = "First year on WayFare";
                        else
                            wayfareYearString = String.valueOf(yearsOnWayfare) + " Years on WayFare";

                        Double localIndivPrice = Helper.exchangeToLocal(indivPrice, localCurrency);
                        Double localTotalPrice = Helper.exchangeToLocal(indivPrice * totalPax, localCurrency);

                        booking_title.setText(currentBooking.getListing().getTitle());
                        with_string.setText("with " + currentBooking.getUser().getUsername());
                        String timeOfBooking = Helper.convert24to12(currentBooking.getBookingDuration().getStartTime());

                        booking_meeting_details.setText(String.format("%s ,%s", address, currentBooking.getListing().getRegion()));
                        booking_time.setText(String.format("%s",timeOfBooking));
                        booking_remarks.setText(currentBooking.getRemarks());
                        String currencyPrefix = Currency.getInstance(localCurrency).getSymbol();
                        booking_price_total.setText(HtmlCompat.fromHtml(String.format("Total price: <u>%s%.2f × %d %s = <b>%s%.2f<b><u>", currencyPrefix, localIndivPrice, totalPax, paxString, localCurrency, localTotalPrice), HtmlCompat.FROM_HTML_MODE_LEGACY));

                        wayfarer_name.setText(currentBooking.getUser().getUsername());
                        years_on_wayfare.setText(wayfareYearString);
                        Glide.with(ViewBookingFragment.this)
                                .load(currentBooking.getUser().getPictureUrl().split("\\?")[0])
                                .sizeMultiplier(0.6f)
                                .centerCrop()
                                .into(wayfarer_pic);
                        if (currentBooking.getUser().isVerified())
                            verified.setVisibility(View.VISIBLE);
                        else
                            verified.setVisibility(View.GONE);
                    }

                });
            }

        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }

    public void initializeMap() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mapFragment.getMapAsync(ViewBookingFragment.this);
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
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng meeting = new LatLng(currentBooking.getListing().getLocation().getY(), currentBooking.getListing().getLocation().getX());
        googleMap.addMarker(new MarkerOptions()
                .position(meeting)
                .title("Meeting Points"));
        googleMap.getUiSettings().setAllGesturesEnabled(false);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(meeting, 15));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Uri gmapsuri = Uri.parse(String.format("https://www.google.com/maps/search/?api=1&query=%f,%f", latLng.latitude, latLng.longitude));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmapsuri);

                // Set the package to Google Maps
                mapIntent.setPackage("com.google.android.apps.maps");

                // Verify that the intent will resolve to an activity
                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Start Google Maps
                    startActivity(mapIntent);
                }
            }
        });
    }
}