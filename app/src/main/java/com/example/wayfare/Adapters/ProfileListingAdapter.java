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
import com.example.wayfare.Models.ListingItemModel;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ProfileListingAdapter extends RecyclerView.Adapter<ProfileListingAdapter.ViewHolder> {


    private final List<ListingItemModel> listingItemModels;
    private final Context context;

    private RecyclerViewInterface recyclerViewInterface;

    public ProfileListingAdapter(Context context, List<ListingItemModel> listingItemModels, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.listingItemModels = listingItemModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_listing_item, parent, false);
        ViewHolder holder = new ViewHolder(view, recyclerViewInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.listingTitle.setText(listingItemModels.get(position).title);
        holder.listingRegion.setText(listingItemModels.get(position).region);
        int ratingCount = listingItemModels.get(position).ratingCount;
        if (ratingCount > 0)
            holder.listingRating.setText(String.valueOf(listingItemModels.get(position).rating) + "★" + " (" + listingItemModels.get(position).ratingCount + ")");
        else{
            holder.listingRating.setText("No reviews yet");
            holder.listingRating.setTextSize(14);
        }

        Glide.with(context)
                .load(listingItemModels.get(position).thumbnailUrl.split("\\?")[0])
                .sizeMultiplier(0.5f)
                .centerCrop()
                .into(holder.listingThumbnail);
    }

    @Override
    public int getItemCount() {
        return listingItemModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView listingThumbnail;
        private TextView listingTitle;
        private TextView listingRating;
        private TextView listingRegion;


        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            listingRating = itemView.findViewById(R.id.listing_rating);
            listingThumbnail = itemView.findViewById(R.id.listing_thumbnail);
            listingTitle = itemView.findViewById(R.id.listing_title);
            listingRegion = itemView.findViewById(R.id.listing_region);

            itemView.setOnClickListener(new View.OnClickListener() {
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

