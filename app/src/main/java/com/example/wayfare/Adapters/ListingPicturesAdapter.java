package com.example.wayfare.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wayfare.Models.PictureUrlItemModel;
import com.example.wayfare.Models.TimeSlotItemModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;

import java.util.List;

public class ListingPicturesAdapter extends RecyclerView.Adapter<ListingPicturesAdapter.ViewHolder>{

    private final List<Uri> uriItemModels;
    private final Context context;

    private final RecyclerViewInterface recyclerViewInterface;

    public ListingPicturesAdapter(Context context, List<Uri> uriItemModels, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.uriItemModels = uriItemModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_listing_picture, parent,false);
        ViewHolder holder = new ViewHolder(view, recyclerViewInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.picture.setAdjustViewBounds(true);
        Glide.with(context)
                .load(uriItemModels.get(position))
                .override(300, 300)
                .into(holder.picture);
    }

    @Override
    public int getItemCount() {
        return uriItemModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView picture;
        Button removePic;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            picture = itemView.findViewById(R.id.picture);
            removePic = itemView.findViewById(R.id.removePic);

            removePic.setOnClickListener(new View.OnClickListener() {
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
