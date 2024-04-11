package com.example.wayfare.Adapters;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wayfare.AlternateRecyclerViewInterface;
import com.example.wayfare.Models.ReviewItemModel;
import com.example.wayfare.Models.SettingItemModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {


    private final List<ReviewItemModel> reviewItemModels;
    private final Context context;

    private AlternateRecyclerViewInterface recyclerViewInterface;

    public ReviewAdapter(Context context, List<ReviewItemModel> reviewItemModels) {
        this.context = context;
        this.reviewItemModels = reviewItemModels;
    }

    public ReviewAdapter(Context context, List<ReviewItemModel> reviewItemModels, AlternateRecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.reviewItemModels = reviewItemModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_review_item, parent, false);
        ViewHolder holder = new ViewHolder(view, recyclerViewInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.reviewTitle.setText(reviewItemModels.get(position).title);
        holder.reviewContent.setText(reviewItemModels.get(position).reviewContent);
        holder.reviewUsername.setText(reviewItemModels.get(position).firstName);
        holder.reviewDate.setText(reviewItemModels.get(position).timeSinceCreation);

        Glide.with(context)
                .load(reviewItemModels.get(position).picUrl.split("\\?")[0])
                .sizeMultiplier(0.5f)
                .centerCrop()
                .into(holder.review_user_pic);
    }

    @Override
    public int getItemCount() {
        return reviewItemModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView reviewTitle;
        private TextView reviewContent;
        private TextView reviewUsername;
        private TextView reviewDate;
        private ImageView review_user_pic;


        public ViewHolder(@NonNull View itemView, AlternateRecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            reviewTitle = itemView.findViewById(R.id.review_title);
            reviewContent = itemView.findViewById(R.id.review_content);
            reviewUsername = itemView.findViewById(R.id.review_username);
            reviewDate = itemView.findViewById(R.id.review_date);
            review_user_pic = itemView.findViewById(R.id.review_user_pic);

            review_user_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getBindingAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onAlternateItemClick(pos);
                        }
                    }
                }
            });

            reviewUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getBindingAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onAlternateItemClick(pos);
                        }
                    }
                }
            });
        }
    }

}

