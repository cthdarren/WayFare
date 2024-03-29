package com.example.wayfare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.R;
import com.example.wayfare.tourListing_RecyclerViewInterface;
import com.google.android.material.button.MaterialButton;

public class timingAdapter extends RecyclerView.Adapter<timingAdapter.MyViewHolder>{
    Context context;
    String[] tourTimings;
    private final tourListing_RecyclerViewInterface tourListing_recyclerViewInterface;

    public timingAdapter(Context context, String[] tourTimings, tourListing_RecyclerViewInterface tourListing_recyclerViewInterface) {
        this.context = context;
        this.tourTimings = tourTimings;
        this.tourListing_recyclerViewInterface = tourListing_recyclerViewInterface;
    }

    @NonNull
    @Override
    public timingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating layout and giving look to each view
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.timing_row, parent, false);
        return new timingAdapter.MyViewHolder(view, tourListing_recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull timingAdapter.MyViewHolder holder, int position) {
        holder.tvTiming.setText(tourTimings[position]);
    }

    @Override
    public int getItemCount() {
        return tourTimings.length;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // grabbing views from layout file
        // almost like onCreate method
        TextView tvTiming;
        MaterialButton tvBookButton;
        public MyViewHolder(@NonNull View itemView, tourListing_RecyclerViewInterface tourListing_recyclerViewInterface) {
            super(itemView);
            tvTiming = itemView.findViewById(R.id.timingCardText);
            tvBookButton = itemView.findViewById(R.id.bookButton);

            // for changing to activity
            tvBookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tourListing_recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            tourListing_recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
