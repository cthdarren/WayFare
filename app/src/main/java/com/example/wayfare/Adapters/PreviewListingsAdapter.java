package com.example.wayfare.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.example.wayfare.tourListing_RecyclerViewInterface;

import java.util.ArrayList;

public class PreviewListingsAdapter extends RecyclerView.Adapter<PreviewListingsAdapter.PreviewListingsViewHolder>{
    ArrayList<TourListModel> tourListModels;
    Context context;
    private int selectedPosition = RecyclerView.NO_POSITION;
    public PreviewListingsAdapter(Context context, ArrayList<TourListModel> tourListingModels){
        this.context = context;
        this.tourListModels = tourListingModels;
    }
    @NonNull
    @Override
    public PreviewListingsAdapter.PreviewListingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_listing_item, parent, false);
        PreviewListingsViewHolder viewHolder = new PreviewListingsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewListingsAdapter.PreviewListingsViewHolder holder, int position) {
        Glide.with(context)
                .load(tourListModels.get(position).getThumbnailUrls()[0].split("\\?")[0]) // Load the first URL from the array
                .into(holder.listingImage);
        holder.listingTitle.setText(tourListModels.get(position).getTitle());
        holder.listingLoc.setText(tourListModels.get(position).getRegion());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousSelectedPosition = selectedPosition;
                if (previousSelectedPosition == position) {
                    selectedPosition = RecyclerView.NO_POSITION;
                }else{
                    selectedPosition = position;
                }

                // Update previous selected item if any
                if (previousSelectedPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousSelectedPosition);
                }
                if (selectedPosition != RecyclerView.NO_POSITION) {
                    // Update newly selected item
                    notifyItemChanged(selectedPosition);
                }
            }
        });
        // Set background tint based on selected position
        if (selectedPosition == position) {
            holder.cardListPreview.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.md_theme_inversePrimary)));
        } else {
            holder.cardListPreview.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.md_theme_background)));
        }
    }

    @Override
    public int getItemCount() {
        return tourListModels.size();
    }
    public int getSelectedPosition(){
        return selectedPosition;
    }
    public class PreviewListingsViewHolder extends RecyclerView.ViewHolder{
        ImageView listingImage;
        TextView listingTitle;
        TextView listingLoc;
        CardView cardListPreview;

        public PreviewListingsViewHolder(@NonNull View itemView) {
            super(itemView);
            listingImage = itemView.findViewById(R.id.previewListImg);
            listingTitle = itemView.findViewById(R.id.listingName);
            listingLoc = itemView.findViewById(R.id.listingLoc);
            cardListPreview = itemView.findViewById(R.id.cardListPreview);

        }
    }
}
