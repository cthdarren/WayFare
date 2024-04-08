package com.example.wayfare.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Fragment.ProfileFragment;
import com.example.wayfare.Models.BookingItemModel;
import com.example.wayfare.Models.ListingItemModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.Utils.Helper;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpcomingBookingAdapter extends RecyclerView.Adapter<UpcomingBookingAdapter.ViewHolder> {


    private final List<BookingItemModel> upcomingBookingItemModels;
    private final Context context;

    private RecyclerViewInterface recyclerViewInterface;

    public UpcomingBookingAdapter(Context context, List<BookingItemModel> upcomingBookingItemModels, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.upcomingBookingItemModels = upcomingBookingItemModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_booking_item, parent, false);
        ViewHolder holder = new ViewHolder(view, recyclerViewInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.wayfarerUsername.setText(upcomingBookingItemModels.get(position).wayfarerUsername);
        holder.title.setText(upcomingBookingItemModels.get(position).title);
        holder.bookingLocation.setText(upcomingBookingItemModels.get(position).location);
        holder.timeToBooking.setText("In " + upcomingBookingItemModels.get(position).timeToBooking);
        holder.dateOfBooking.setText(upcomingBookingItemModels.get(position).dateOfBooking + ", " + upcomingBookingItemModels.get(position).timeOfBooking);
        holder.wayfarerUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle username = new Bundle();
                username.putString("username", upcomingBookingItemModels.get(position).wayfarerUsername);
                ProfileFragment pf = new ProfileFragment();
                pf.setArguments(username);
                AppCompatActivity containingActivity = (AppCompatActivity) context;
                Helper.goToFragmentSlideInRight(containingActivity.getSupportFragmentManager(), R.id.container, pf);
            }
        });

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Looper uiLooper = Looper.getMainLooper();
        final Handler handler = new Handler(uiLooper);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String picUrl = upcomingBookingItemModels.get(position).thumbnailUrl;
                    String wayfarerPictureUrl = upcomingBookingItemModels.get(position).wayfarerPicUrl;
                    Bitmap wayfarerImage, image;
                    if (wayfarerPictureUrl != null) {
                        URL wpUrl = new URL(wayfarerPictureUrl);
                        wayfarerImage = BitmapFactory.decodeStream(wpUrl.openConnection().getInputStream());
                    } else
                        wayfarerImage = null;
                    if (picUrl != null) {
                        URL url = new URL(picUrl);
                        image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } else
                        image = null;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.thumbnail.setImageBitmap(image);
                            if (wayfarerImage != null)
                                holder.wayfarerPicture.setImageBitmap(wayfarerImage);
                            else {
                                holder.wayfarerPicture.setBackgroundResource(R.drawable.default_avatar);
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return upcomingBookingItemModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnail, wayfarerPicture;
        private TextView title, wayfarerUsername, timeToBooking, dateOfBooking, bookingLocation;
        private LinearLayout wayfarerDetailsWrapper;


        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            wayfarerDetailsWrapper = itemView.findViewById(R.id.wayfarerDetailsWrapper);
            wayfarerPicture = itemView.findViewById(R.id.wayfarerPicture);
            wayfarerUsername = itemView.findViewById(R.id.wayfarerUsername);
            title = itemView.findViewById(R.id.title);
            timeToBooking = itemView.findViewById(R.id.timeToBooking);
            bookingLocation = itemView.findViewById(R.id.bookingLocation);
            dateOfBooking = itemView.findViewById(R.id.dateOfBooking);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (recyclerViewInterface != null) {
//                        int pos = getBindingAdapterPosition();
//                        if (pos != RecyclerView.NO_POSITION) {
//                            recyclerViewInterface.onItemClick(pos);
//                        }
//                    }
//                }
//            });
        }
    }

}

