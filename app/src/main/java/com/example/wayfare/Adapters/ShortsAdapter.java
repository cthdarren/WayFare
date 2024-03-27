package com.example.wayfare.Adapters;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.ImageView;

import androidx.media3.common.Player;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.SimpleExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Models.ShortsObject;
import com.example.wayfare.R;

import java.util.ArrayList;
import java.util.List;

public class ShortsAdapter extends RecyclerView.Adapter<ShortsAdapter.ShortsViewHolder> {
    List<ShortsObject> shortsDataList;
    private int currentPosition;
    int numberOfClick = 0;
    float volume;
    boolean isPlaying = false;
    private Context context;
    private List<ShortsViewHolder> shortsViewHolderList;
    public ShortsAdapter(List<ShortsObject> shortsDataList,Context context) {
        this.shortsDataList = shortsDataList;
        this.context = context;
        currentPosition = 0;
        shortsViewHolderList = new ArrayList<>();
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
        shortsViewHolderList.add(position, holder);
    }
    public void updateCurrentPosition(int pos) {
        currentPosition = pos;
    }
    public void pauseVideo(int position) {
        shortsViewHolderList.get(position).pauseVideo();
    }

    public void playVideo(int position) {
        shortsViewHolderList.get(position).playVideo();
    }
    public void stopAllVideo() {
        for(ShortsAdapter.ShortsViewHolder holder:shortsViewHolderList) {
            holder.stopVideo();
        }
    }

    public void onViewAttachedToWindow(ShortsViewHolder holder) {
        if(isPlaying == false) {
            holder.playVideo();
            isPlaying = true;
        }
    }

    @Override
    public void onViewDetachedFromWindow(ShortsViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.d("ShortsAdapter", "onViewDetachedFromWindow called for position: " );
        holder.pauseVideo();
        isPlaying = false;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
    @Override
    public int getItemCount() {
        return shortsDataList.size();
    }
    public int getViewsCount() {return shortsViewHolderList.size();}
    public class ShortsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private PlayerView videoView;
        private ExoPlayer exoPlayer;
        private TextView shortsDescription, shortsTitle;
        private ImageView imvAvatar, imvPause, imvMore, imvAppear, imvVolume, imvShare;
        private ProgressBar videoProgressBar;
        boolean isPaused = false;

        public ShortsViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            shortsDescription = itemView.findViewById(R.id.shortsDescription);
            shortsTitle = itemView.findViewById(R.id.shortsTitle);
            videoProgressBar = itemView.findViewById(R.id.progressBar);
            imvVolume = itemView.findViewById(R.id.imvVolume);
            imvAppear = itemView.findViewById(R.id.imv_appear);
            videoView.setOnClickListener(this);
            imvVolume.setOnClickListener(this);
        }
        public void playVideo() {
            disappearImage();
            if (!exoPlayer.isPlaying()) {
                exoPlayer.play();
            }
            if (exoPlayer.getPlaybackState() == Player.STATE_READY
                    || exoPlayer.getPlaybackState() == Player.STATE_IDLE) {
                exoPlayer.setPlayWhenReady(true);
            }
        }
        public void pauseVideo() {
            if (exoPlayer.getPlaybackState() == Player.STATE_READY) {
                exoPlayer.setPlayWhenReady(false);
                imvAppear.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                imvAppear.setVisibility(View.VISIBLE);
            }
        }
        public void stopVideo() {
            isPaused = true;
            if (exoPlayer.getPlaybackState() == Player.STATE_READY) {
                exoPlayer.setPlayWhenReady(false);
                exoPlayer.stop();
                exoPlayer.seekTo(0);
                exoPlayer.release();
            }
        }
        public void appearImage(int src) {
            imvAppear.setImageResource(src);
            imvAppear.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    imvAppear.setVisibility(View.GONE);
                }
            },  1000);
        }
        public void disappearImage(){
            imvAppear.setVisibility(View.GONE);
        }
        @SuppressLint("ClickableViewAccessibility")
        void setShortsData(ShortsObject shortsData){
            Uri shortsUri = Uri.parse(shortsData.getUrl());
            shortsDescription.setText(shortsData.getDescription());
            shortsTitle.setText(shortsData.getTitle());

            // Create a media item representing the video
            MediaItem mediaItem = MediaItem.fromUri(shortsUri);
            if (exoPlayer != null) exoPlayer.release();
            exoPlayer = new ExoPlayer.Builder(videoView.getContext()).build();
            videoView.setPlayer(exoPlayer);
            exoPlayer.addMediaItem(mediaItem);
            exoPlayer.setRepeatMode(exoPlayer.REPEAT_MODE_ONE);
            exoPlayer.prepare();
            pauseVideo();
            isPaused = true;

            // Hide progress bar when playback starts
            exoPlayer.addListener(new ExoPlayer.Listener() {
                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    if (isPlaying) {
                        videoProgressBar.setVisibility(View.GONE);
                    }
                }
            });

        }

        public void onClick(View view) {
            if (view.getId() == videoView.getId()) {
                numberOfClick = numberOfClick + 1;
                float currentVolume = exoPlayer.getVolume();
                boolean isMuted = (currentVolume == 0);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (numberOfClick == 1) {
                            if (isPlaying) {
                                pauseVideo();
                                isPlaying = false;
                                imvAppear.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                                imvAppear.setVisibility(View.VISIBLE);
                            } else {
                                playVideo();
                                isPlaying = true;
                                imvAppear.setVisibility(View.GONE);
                            }
                        }
//                        else if (numberOfClick == 2) {
//                            handleTymClick(view);
//                            imvAppear.setVisibility(View.GONE);
//                            appearImage(R.drawable.ic_fill_favorite);
//                        }
                        numberOfClick = 0;
                    }
                }, 250);
            }
            if (view.getId() == imvVolume.getId()) {
                float currentVolume = exoPlayer.getVolume();
                boolean isMuted = (currentVolume == 0);
                if (isMuted) {
                    exoPlayer.setVolume(volume);
                    imvVolume.setImageResource(R.drawable.ic_baseline_volume_up_24);
                    appearImage(R.drawable.ic_baseline_volume_up_24);
                } else {
                    volume = exoPlayer.getVolume();
                    exoPlayer.setVolume(0);
                    imvVolume.setImageResource(R.drawable.ic_baseline_volume_off_24);
                    appearImage(R.drawable.ic_baseline_volume_off_24);
                }
            }
        }
    }
}