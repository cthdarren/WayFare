package com.example.wayfare.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wayfare.Models.ListingItemModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.TertiaryRecyclerViewInterface;

import java.util.List;

public class ProfileJourneysAdapter extends RecyclerView.Adapter<ProfileJourneysAdapter.ViewHolder> {


    private final List<String> journeyThumnails;
    private final Context context;

    private TertiaryRecyclerViewInterface recyclerViewInterface;

    public ProfileJourneysAdapter(Context context, List<String> journeyThumnails, TertiaryRecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.journeyThumnails = journeyThumnails;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_profile_journeys, parent, false);
        ViewHolder holder = new ViewHolder(view, recyclerViewInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (journeyThumnails.get(position) != null){
        Glide.with(context)
                .load(journeyThumnails.get(position).split("\\?")[0])
                .sizeMultiplier(0.5f)
                .centerCrop()
                .into(holder.journeyThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return journeyThumnails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView journeyThumbnail;


        public ViewHolder(@NonNull View itemView, TertiaryRecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            journeyThumbnail= itemView.findViewById(R.id.journey_thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getBindingAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onTertiaryItemClick(pos);
                        }
                    }
                }
            });
        }
    }

}

