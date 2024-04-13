package com.example.wayfare.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wayfare.Models.BookmarkModel;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthHelper;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.tourListing_RecyclerViewInterface;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class tourListing_RecyclerViewAdapter extends RecyclerView.Adapter<tourListing_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<TourListModel> tourListModels;
    List<String> bookmarkedListingIds;
    private final tourListing_RecyclerViewInterface tourListing_recyclerViewInterface;
    public tourListing_RecyclerViewAdapter(Context context, ArrayList<TourListModel> tourListingModels,List<String> bookmarkedListingIds, tourListing_RecyclerViewInterface tourListing_recyclerViewInterface){
        this.context = context;
        this.tourListModels = tourListingModels;
        this.bookmarkedListingIds = bookmarkedListingIds;
        this.tourListing_recyclerViewInterface = tourListing_recyclerViewInterface;
    }
    @NonNull
    @Override
    public tourListing_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating layout and giving look to each view
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_tour_new, parent, false);
        return new MyViewHolder(view, tourListing_recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull tourListing_RecyclerViewAdapter.MyViewHolder holder, int position) {
        // assigning values to each of the views when they come back onto the screen, based on position of recycler view
        boolean bookmarked = false;
        for (String listingId: bookmarkedListingIds){
            if (Objects.equals(listingId, tourListModels.get(position).getId())) {
                holder.bookmarkBtn.setChecked(true);
                bookmarked = true;
                break;
            }
        }

        holder.bookmarkBtn.setChecked(bookmarked);

        holder.tvTitle.setText(tourListModels.get(position).getTitle());
        Glide.with(context)
                .load(tourListModels.get(position).getThumbnailUrls()[0].split("\\?")[0]) // Load the first URL from the array
                .sizeMultiplier(0.8f)
                .into(holder.imageView); // Set the image to the ImageView
        String localCurrency = new AuthHelper(context).getSharedPrefsCurrencyName();
        Double localPrice = Helper.exchangeToLocal(tourListModels.get(position).getPrice(), localCurrency);
        String currencyPrefix = Currency.getInstance(localCurrency).getSymbol();
        holder.tvPrice.setText(Html.fromHtml(String.format("<u><b>%s %.2f</b> per person</u>", currencyPrefix, localPrice), Html.FROM_HTML_MODE_LEGACY));
        if (tourListModels.get(position).getReviewCount() == 0)
            holder.tvRating.setText("No reviews yet");
        else
            holder.tvRating.setText(String.format("%s (%d)", String.valueOf(tourListModels.get(position).getRating()), tourListModels.get(position).getReviewCount()));
        holder.tvLocation.setText(tourListModels.get(position).getRegion());

        holder.bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = String.format("{\"listingId\":\"%s\"}", tourListModels.get(position).getId());
                RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
                String apiUrl;
                if (holder.bookmarkBtn.isChecked())
                    apiUrl = "/bookmark";
                else
                    apiUrl = "/unbookmark";
                new AuthService(context).getResponse(apiUrl, true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
                    @Override
                    public void onError(String message) {
                    }

                    @Override
                    public void onResponse(ResponseModel json) {
                    }
                });
            }
        });
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
        TextView tvLocation;
        TextView tvPrice;
        TextView tvRating;
        CheckBox bookmarkBtn;
        public MyViewHolder(@NonNull View itemView, tourListing_RecyclerViewInterface tourListing_recyclerViewInterface) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView); // storing each of the views in XML file to variables in java code
            tvTitle = itemView.findViewById(R.id.title);
            tvLocation = itemView.findViewById(R.id.location);
            tvPrice = itemView.findViewById(R.id.price);
            tvRating = itemView.findViewById(R.id.rating);
            bookmarkBtn = itemView.findViewById(R.id.bookmarkBtn);


            // for changing to fragment
            itemView.setOnClickListener(new View.OnClickListener() {
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
