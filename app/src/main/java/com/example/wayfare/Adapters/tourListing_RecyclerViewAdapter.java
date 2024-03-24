package com.example.wayfare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;

import java.util.ArrayList;

public class tourListing_RecyclerViewAdapter extends RecyclerView.Adapter<tourListing_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<TourListModel> tourListModels;
    public tourListing_RecyclerViewAdapter(Context context, ArrayList<TourListModel> tourListingModels){
        this.context = context;
        this.tourListModels = tourListingModels;
    }
    @NonNull
    @Override
    public tourListing_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating layout and giving look to each view
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_tour_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tourListing_RecyclerViewAdapter.MyViewHolder holder, int position) {
        // assigning values to each of the views when they come back onto the screen, based on position of recycler view
        holder.tvTitle.setText(tourListModels.get(position).getTourListingTitle());
        holder.imageView.setImageResource(tourListModels.get(position).getTourListingImage());
    }

    @Override
    public int getItemCount() {
        // just wants to know the number of items to be displayed
        return tourListModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // grabbing views from layout file
        // almost like onCreate method
        ImageView imageView;
        TextView tvTitle;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView); // storing each of the views in XML file to variables in java code
            tvTitle = itemView.findViewById(R.id.title);
        }
    }
}
