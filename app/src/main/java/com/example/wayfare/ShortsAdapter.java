package com.example.wayfare;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShortsAdapter extends RecyclerView.Adapter<ShortsAdapter.ShortsViewHolder> {
    List<Shortsdata> shortsDataList;
    public ShortsAdapter(List<Shortsdata> shortsDataList) {
        this.shortsDataList = shortsDataList;
    }

    @NonNull
    @Override
    public ShortsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore, parent, false);
        return new ShortsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortsViewHolder holder, int position) {
        holder.setShortsData(shortsDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return shortsDataList.size();
    }

    public class ShortsViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        TextView shortsUser, shortsTitle;
        ImageView shortsImage;

        public ShortsViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            shortsImage = itemView.findViewById(R.id.shortsImage);
            shortsUser = itemView.findViewById(R.id.shortsUsername);
            shortsTitle = itemView.findViewById(R.id.shortsTitle);
        }

        public void setShortsData(Shortsdata shortsData){
            shortsUser.setText(shortsData.getShortsUser());
            shortsTitle.setText(shortsData.getShortsTitle());
            videoView.setVideoPath(shortsData.getShortsPath());
            shortsImage.setImageResource(shortsData.getShortsImage());

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                    float screenRatio = videoView.getWidth() / (float) videoView.getHeight();

                    float scale = videoRatio / screenRatio;
                    if (scale >= 1f){
                        videoView.setScaleX(scale);
                    } else {
                        videoView.setScaleY(1f/scale);
                    }
                }});

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });

        }
    }
}
