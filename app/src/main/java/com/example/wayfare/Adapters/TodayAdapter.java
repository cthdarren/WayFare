package com.example.wayfare.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Models.BookingModel;
import com.example.wayfare.R;

import java.util.List;

public class TodayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BookingModel> bookings;

    public TodayAdapter(List<BookingModel> bookings) {
        this.bookings = bookings;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.host_row, parent, false);
        return new BookingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BookingModel booking = bookings.get(position);
        BookingViewHolder viewHolder = (BookingViewHolder) holder;  // Cast to BookingViewHolder

        if (booking.getUser()==null){
            viewHolder.tourBooker.setText(booking.getUserId());
        }else
        {viewHolder.tourBooker.setText(booking.getUser().getUsername());} //returned user throws a null pointer exception
        viewHolder.tourName.setText(booking.getListing().getTitle());
        viewHolder.tourPax.setText(String.valueOf(booking.getPax()));
        viewHolder.tourTime.setText(booking.getDateBooked().substring(0,10));
    }

    @Override
    public int getItemCount() {
        if (bookings == null) {
            return 0;
        }
        return bookings.size();
    }

    // Inner class for ViewHolder
    private static class BookingViewHolder extends RecyclerView.ViewHolder {

        public TextView tourBooker;
        public TextView tourName;
        public TextView tourTime;
        public TextView tourPax;
        // ... Similarly add references for all ImageViews and TextViews

        public BookingViewHolder(View itemView) {
            super(itemView);
            tourBooker = (TextView) itemView.findViewById(R.id.hostName);
            tourName = (TextView) itemView.findViewById(R.id.hostTourName);
            tourTime = (TextView) itemView.findViewById(R.id.hostTime);
            tourPax = (TextView) itemView.findViewById(R.id.hostPax);
            // ... Similarly initialize all view references
        }
    }
}