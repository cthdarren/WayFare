package com.example.wayfare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.HostListingsRecyclerViewInterface;
import com.example.wayfare.Models.CategoryItemModel;
import com.example.wayfare.Models.TimeSlotItemModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.google.android.material.card.MaterialCardView;

import java.sql.Time;
import java.util.List;

public class ListingTimeSlotAdapter extends RecyclerView.Adapter<ListingTimeSlotAdapter.ViewHolder>{

    private final List<TimeSlotItemModel> timeSlotItemModels;
    private final Context context;

    private final RecyclerViewInterface recyclerViewInterface;

    public ListingTimeSlotAdapter(Context context, List<TimeSlotItemModel> timeSlotModels, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.timeSlotItemModels = timeSlotModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hosting_timeslot_list_item, parent,false);
        ViewHolder holder = new ViewHolder(view, recyclerViewInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String timeSlotString;
        int startTime = timeSlotItemModels.get(position).startTime;
        int endTime = timeSlotItemModels.get(position).endTime;
        String startTimeString;
        String endTimeString;
        String duration;
        duration = String.valueOf(endTime - startTime);
        if (startTime >= 12)
            startTimeString = String.valueOf(startTime-12) + "pm";
        else
            startTimeString = String.valueOf(startTime) + "am";
        if (endTime >= 12)
            endTimeString = String.valueOf(endTime-12) + "pm";
        else
            endTimeString = String.valueOf(endTime) + "am";

        holder.timeSlot.setText(String.format("%s - %s, (%s hours)",startTimeString, endTimeString, duration));
    }

    @Override
    public int getItemCount() {
        return timeSlotItemModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView timeSlot;
        private ImageView removeSlot;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            timeSlot = itemView.findViewById(R.id.timeSlotString);
            removeSlot = itemView.findViewById(R.id.removeSlotBtn);

            removeSlot.setOnClickListener(new View.OnClickListener() {
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
