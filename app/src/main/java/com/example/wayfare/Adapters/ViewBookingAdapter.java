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
import com.example.wayfare.Models.BookingModel;
import com.example.wayfare.Models.ReviewItemModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;

import java.util.List;

public class ViewBookingAdapter extends RecyclerView.Adapter<ViewBookingAdapter.ViewHolder> {


    private final List<String> pictureUrls;
    private final Context context;
    private RecyclerViewInterface recyclerViewInterface;


    public ViewBookingAdapter(Context context, List<String> pictureUrls) {
        this.context = context;
        this.pictureUrls = pictureUrls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view_booking, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context)
                .load(pictureUrls.get(position).split("\\?")[0])
                .sizeMultiplier(0.7f)
                .centerCrop()
                .into(holder.picture);
    }

    @Override
    public int getItemCount() {
        return pictureUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView picture;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            picture= itemView.findViewById(R.id.picture);
        }
    }

}

