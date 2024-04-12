package com.example.wayfare.Adapters;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.annotation.OptIn;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.Player;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.SimpleExoPlayer;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.PlayerView;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Fragment.TourListingFull;
import com.example.wayfare.Models.ShortsObject;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class ShortsAdapter extends RecyclerView.Adapter<ShortsAdapter.ShortsViewHolder> {
    List<ShortsObject> shortsDataList;
    private int currentPosition;
    private FragmentManager fragmentManager;
    private Fragment exploreFragment;
    int numberOfClick = 0;
    float volume;
    boolean isPlaying = false;
    private Context context;
    private List<ShortsViewHolder> shortsViewHolderList;
    public ShortsAdapter(List<ShortsObject> shortsDataList, Context context, FragmentManager fragmentManager, Fragment exploreFragment) {
        this.shortsDataList = shortsDataList;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.exploreFragment = exploreFragment;
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
            // ViewHolder is bound to the wrong position, rebind it to the correct position
        if(position >= shortsViewHolderList.size()) {
                holder.setShortsData(shortsDataList.get(position));
                shortsViewHolderList.add(position, holder);
            }
        Log.d("ViewHolderPosition", "bindcalled: " + position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public void updateCurrentPosition(int pos) {
        currentPosition = pos;
    }
    public void pauseVideo(int position) {
        if(position < shortsViewHolderList.size()) {
            shortsViewHolderList.get(position).pauseVideoNoImg();
            isPlaying = false;
        }

    }
    public void playVideo(int position) {
        if(position < shortsViewHolderList.size()) {
            shortsViewHolderList.get(position).playVideo();
        }
    }
    public void stopVideo(int position){
        if(position < shortsViewHolderList.size()) {
            shortsViewHolderList.get(position).stopVideo();
        }
    }
    public void stopAllVideo() {
        for(ShortsAdapter.ShortsViewHolder holder:shortsViewHolderList) {
            holder.stopVideo();
        }
    }

    public void onViewAttachedToWindow(ShortsViewHolder holder) {
//        holder.playVideo();
    }

    @Override
    public void onViewDetachedFromWindow(ShortsViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.d("ShortsAdapter", "onViewDetachedFromWindow called for position: " );
//            holder.pauseVideo();
//        isPlaying = false;
    }
    private boolean isFragmentAttached() {
        return exploreFragment != null && exploreFragment.isAdded() && !exploreFragment.isDetached() && !exploreFragment.isRemoving();
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
        private TextView shortsDescription, shortsTitle, listingTitle;
        private CardView listingCard;
        private ImageView imvAvatar, imvPause, imvMore, imvAppear, imvVolume, imvShare;
        private ProgressBar videoProgressBar;
        private MediaItem mediaItem;
        boolean isPaused = false;

        @OptIn(markerClass = UnstableApi.class) public ShortsViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            shortsDescription = itemView.findViewById(R.id.shortsDescription);
            shortsTitle = itemView.findViewById(R.id.shortsTitle);
            videoProgressBar = itemView.findViewById(R.id.progressBar);
            videoProgressBar.setVisibility(View.GONE);
            imvVolume = itemView.findViewById(R.id.imvVolume);
            imvAppear = itemView.findViewById(R.id.imv_appear);
            listingTitle = itemView.findViewById(R.id.listingTitle);
            listingCard = itemView.findViewById(R.id.listingCard);
            videoView.setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING);
            videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            videoView.setOnClickListener(this);
            imvVolume.setOnClickListener(this);
            listingTitle.setOnClickListener(this);
        }
        public void playVideo() {
            disappearImage();
            if (exoPlayer!=null) {
                Log.d("ViewHolderPosition", "playbackstate" + exoPlayer.getPlaybackState());
                if (!exoPlayer.isPlaying()) {
                    exoPlayer.play();
                }
                if (exoPlayer.getPlaybackState() == Player.STATE_READY || exoPlayer.getPlaybackState() == Player.STATE_IDLE){
                    exoPlayer.setPlayWhenReady(true);
                }
                if(exoPlayer.getPlaybackState() == Player.STATE_IDLE) {
                }
            }else{
                Log.d("ViewHolderPosition", "exo is null"+getCurrentPosition());
                videoProgressBar.setVisibility(View.VISIBLE);
                setShortsData(shortsDataList.get(getCurrentPosition()));
                exoPlayer.setPlayWhenReady(true);
                Log.d("ViewHolderPosition", "restarting" + getCurrentPosition());
            }
            isPaused = false;
        }
        public void pauseVideo() {
            if(exoPlayer!=null) {
                if (exoPlayer.getPlaybackState() == Player.STATE_READY) {
                    exoPlayer.setPlayWhenReady(false);
                    appearImage(R.drawable.ic_baseline_play_arrow_24);
                }
            }
        }
        public void pauseVideoNoImg() {
            if(exoPlayer!=null) {
                if (exoPlayer.getPlaybackState() == Player.STATE_READY) {
                    exoPlayer.setPlayWhenReady(false);
                }
            }
        }
        public void stopVideo() {
            isPaused = true;
            if (exoPlayer != null) {
                if (exoPlayer.getPlaybackState() == Player.STATE_READY) {
                    exoPlayer.setPlayWhenReady(false);
                    exoPlayer.stop();
                    exoPlayer.release();
                    exoPlayer = null;
                }
            }
        }
        public void appearImage(int src) {
            imvAppear.setImageResource(src);
            imvAppear.setVisibility(View.VISIBLE);
            imvAppear.animate()
                    .scaleX(1.5f)  // scale up to 150%
                    .scaleY(1.5f)  // scale up to 150%
                    .setDuration(200)  // duration of the animation in milliseconds
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            // Animation ended, scale back to original size
                            imvAppear.animate()
                                    .scaleX(1.0f)  // scale down to original size
                                    .scaleY(1.0f)  // scale down to original size
                                    .setDuration(200)  // duration of the animation in milliseconds
                                    .start();
                        }
                    })
                    .start();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imvAppear.setVisibility(View.GONE);
                }
            },  500);
        }
        public void disappearImage(){
            imvAppear.setVisibility(View.GONE);
        }
        @SuppressLint("ClickableViewAccessibility")
        void setShortsData(ShortsObject shortsData){
            Uri shortsUri = Uri.parse(shortsData.getShortsUrl());
            shortsDescription.setText(shortsData.getDescription());
            shortsTitle.setText(shortsData.getUserName());
            if (shortsData.getListing()!=null){
                listingCard.setVisibility(View.VISIBLE);
                listingTitle.setText(shortsData.getListing().getTitle());
            }
//             Create a media item representing the video
//             Create a MediaItem builder
            MediaItem.Builder mediaItemBuilder = new MediaItem.Builder();
            mediaItemBuilder.setMimeType(MimeTypes.VIDEO_MP4);
            mediaItemBuilder.setUri(shortsUri);
//            if (shortsData.getShortsUrl().endsWith(".mp4")) {
//                // Set MIME type to mp4 if extension is present
//                mediaItemBuilder.setMimeType(MimeTypes.VIDEO_MP4);
//            } else {
//                // Set MIME type to DASH for URIs without .mpd extension
//                mediaItemBuilder.setMimeType(MimeTypes.APPLICATION_MPD);
//            }
//
           MediaItem mediaItem = mediaItemBuilder.build();
//            MediaItem mediaItem = MediaItem.fromUri(shortsUri);
            if (exoPlayer != null){
                exoPlayer.release();
                exoPlayer = null;
            }
            exoPlayer = new ExoPlayer.Builder(videoView.getContext()).build();
            videoView.setPlayer(exoPlayer);
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.setRepeatMode(exoPlayer.REPEAT_MODE_ONE);
            exoPlayer.prepare();
            pauseVideo();
            isPaused = true;
//
//            // Hide progress bar when playback starts
            exoPlayer.addListener(new ExoPlayer.Listener() {
                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    if (isPlaying) {

                        videoProgressBar.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onPlaybackStateChanged(int playbackState) {
                    if (playbackState == ExoPlayer.STATE_READY){
                        Log.d("ViewHolderPosition", String.format("Curr:%s Layout:%s",getCurrentPosition(),getBindingAdapterPosition()));
                        if (!isFragmentAttached() && exoPlayer != null) {
                            // Loading is complete, release resources
                            exoPlayer.stop();
                            exoPlayer.release();
                            exoPlayer = null;
                        }
                    }
                }
            });

        }

        public void onClick(View view) {
            if (view.getId() == videoView.getId()) {
                if (exoPlayer != null) {
                    numberOfClick = numberOfClick + 1;
                    float currentVolume = exoPlayer.getVolume();
                    boolean isMuted = (currentVolume == 0);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (numberOfClick == 1) {
                                if (exoPlayer.isPlaying()) {
                                    pauseVideo();
                                    isPlaying = false;
                                    //appearImage(R.drawable.ic_baseline_play_arrow_24);
                                } else {
                                    playVideo();
                                    isPlaying = true;
                                    imvAppear.setVisibility(View.INVISIBLE);
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
            }
            if (view.getId() == imvVolume.getId()) {
                if(exoPlayer!=null) {
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
            if(view.getId() == listingTitle.getId()){
                if(exoPlayer!=null) {
                    if (exoPlayer.getPlaybackState() == Player.STATE_READY) {
                        exoPlayer.setPlayWhenReady(false);
                    }
                }
                int pos = getCurrentPosition();
                TourListModel currListing = shortsDataList.get(pos).getListing();
                Bundle data = new Bundle();

                data.putString("title", currListing.getTitle());
                data.putString("location", currListing.getRegion());
                data.putString("rating", String.valueOf(currListing.getRating()));
                data.putString("price", String.valueOf(currListing.getPrice()));
                data.putString("thumbnail", currListing.getThumbnailUrls()[0]);
                data.putString("description", currListing.getDescription());
                data.putString("reviewCount", String.valueOf(currListing.getReviewCount()));
                data.putString("listingId", currListing.getId());
                data.putInt("minPax", currListing.getMinPax());
                data.putInt("maxPax", currListing.getMinPax());
                data.putString("userId", currListing.getUserId());
                data.putString("category", currListing.getCategory());
                data.putParcelableArrayList("timeRangeList", (ArrayList<? extends Parcelable>) currListing.getTimeRangeList());

                TourListingFull tourListingFullFragment = new TourListingFull();
                tourListingFullFragment.setArguments(data);

                Helper.goToFragmentSlideInRightArgs(data, fragmentManager, R.id.container, tourListingFullFragment);
            }
        }
    }
}