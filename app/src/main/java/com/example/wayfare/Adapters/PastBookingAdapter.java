package com.example.wayfare.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wayfare.Models.BookingItemModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class PastBookingAdapter extends RecyclerView.Adapter<PastBookingAdapter.ViewHolder> {


    private final List<BookingItemModel> pastBookingItemModels;
    private final Context context;

    private RecyclerViewInterface recyclerViewInterface;

    public PastBookingAdapter(Context context, List<BookingItemModel> pastBookingItemModels, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.pastBookingItemModels = pastBookingItemModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_booking_item, parent, false);
        ViewHolder holder = new ViewHolder(view, recyclerViewInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.pastBookingWayfarer.setText("Hosted by " + pastBookingItemModels.get(position).wayfarerUsername);
        holder.pastBookingTitle.setText(pastBookingItemModels.get(position).title);
        holder.pastBookingLocation.setText(pastBookingItemModels.get(position).location);
        holder.pastBookingDate.setText(pastBookingItemModels.get(position).dateOfBooking);

        if (pastBookingItemModels.get(position).reviewed)
            holder.reviewButton.setVisibility(View.GONE);

        Glide.with(context)
                .load(pastBookingItemModels.get(position).thumbnailUrl.split("\\?")[0])
                .centerCrop()
                .sizeMultiplier(0.5f)
                .into(holder.pastBookingThumbnail);
    }

    @Override
    public int getItemCount() {
        return pastBookingItemModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView pastBookingThumbnail;
        private TextView pastBookingTitle, pastBookingWayfarer, pastBookingDate, pastBookingLocation;
        private MaterialCardView reviewButton;


        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            pastBookingThumbnail = itemView.findViewById(R.id.pastListingThumbnail);
            pastBookingWayfarer = itemView.findViewById(R.id.pastBookingWayfarer);
            pastBookingTitle = itemView.findViewById(R.id.pastBookingTitle);
            pastBookingLocation = itemView.findViewById(R.id.pastBookingLocation);
            pastBookingDate = itemView.findViewById(R.id.pastBookingDate);
            reviewButton = itemView.findViewById(R.id.reviewButton);

            reviewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getBindingAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }

}

