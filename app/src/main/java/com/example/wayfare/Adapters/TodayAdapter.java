package com.example.wayfare.Adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Activity.WayfarerActivity;
import com.example.wayfare.Fragment.ProfileFragment;
import com.example.wayfare.Models.BookingModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.Utils.Helper;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class TodayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<BookingModel> bookings;
    private RecyclerViewInterface recyclerViewInterface;

    public TodayAdapter(List<BookingModel> bookings, RecyclerViewInterface recyclerViewInterface) {
        this.bookings = bookings;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.host_row, parent, false);

        return new BookingViewHolder(itemView, recyclerViewInterface);
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
        viewHolder.tourTime.setText(TimeDisplay(booking));
        booking.getBookingDuration().getStartTime();
        if (booking.getRemarks().isBlank()){
            viewHolder.remarksRow.setVisibility(View.GONE);
        } else{
            viewHolder.tourRemarks.setText(booking.getRemarks());
        }

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
        public TextView tourRemarks;
        public ImageView remarksImage;
        public TableRow remarksRow;
        // ... Similarly add references for all ImageViews and TextViews

        public BookingViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            tourBooker = (TextView) itemView.findViewById(R.id.hostName);
            tourName = (TextView) itemView.findViewById(R.id.hostTourName);
            tourTime = (TextView) itemView.findViewById(R.id.hostTime);
            tourPax = (TextView) itemView.findViewById(R.id.hostPax);
            tourRemarks = (TextView) itemView.findViewById(R.id.hostRemarks);
            remarksImage = (ImageView) itemView.findViewById(R.id.remarksImage);
            remarksRow = (TableRow) itemView.findViewById(R.id.remarksRow);
            // ... Similarly initialize all view references

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
    public String TimeDisplay(BookingModel booking){
        String date = booking.getDateBooked().substring(0,10);
        String startTime = Helper.convert24to12(booking.getBookingDuration().getStartTime());
        String endTime = Helper.convert24to12(booking.getBookingDuration().getEndTime());
        String formattedString = String.format("%s %s - %s", date, startTime, endTime);
        return formattedString;
    }
}