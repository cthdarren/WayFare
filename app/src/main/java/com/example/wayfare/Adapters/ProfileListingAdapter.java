package com.example.wayfare.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Models.ReviewItemModel;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileListingAdapter extends RecyclerView.Adapter<ProfileListingAdapter.ViewHolder> {


    private final List<TourListModel> listingItemModels;
    private final Context context;

    private RecyclerViewInterface recyclerViewInterface;

    public ProfileListingAdapter(Context context, List<TourListModel> listingItemModels, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.listingItemModels = listingItemModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_listing_item, parent, false);
        ViewHolder holder = new ViewHolder(view, recyclerViewInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.listingTitle.setText(listingItemModels.get(position).getTitle());
        holder.listingRating.setText(String.valueOf(listingItemModels.get(position).getRating()) + "★" + "(" + listingItemModels.get(position).getReviewCount() + ")");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Looper uiLooper = Looper.getMainLooper();
        final Handler handler = new Handler(uiLooper);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String picUrl = listingItemModels.get(position).getThumbnailUrls()[0];
                    URL url = new URL(picUrl);
                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.listingThumbnail.setImageBitmap(image);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listingItemModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView listingThumbnail;
        private TextView listingTitle;
        private TextView listingRating;


        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            listingRating = itemView.findViewById(R.id.listing_rating);
            listingThumbnail = itemView.findViewById(R.id.listing_thumbnail);
            listingTitle = itemView.findViewById(R.id.listing_title);

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

}

