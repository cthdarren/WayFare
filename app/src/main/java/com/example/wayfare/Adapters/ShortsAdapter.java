package com.example.wayfare.Adapters;
import static com.example.wayfare.Utils.AzureStorageManager.getBaseUrl;
import static com.example.wayfare.Utils.Helper.convertDateToShortDate;
import static com.example.wayfare.Utils.Helper.goToLogin;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.annotation.OptIn;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
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

import com.bumptech.glide.Glide;
import com.example.wayfare.Activity.PostShortActivity;
import com.example.wayfare.BuildConfig;
import com.example.wayfare.Fragment.ExploreFragment;
import com.example.wayfare.Fragment.TourListingFull;
import com.example.wayfare.Models.Comment;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.ShortsObject;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.Models.UserModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.ViewModel.UserViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShortsAdapter extends RecyclerView.Adapter<ShortsAdapter.ShortsViewHolder> {
    List<ShortsObject> shortsDataList;
    UserViewModel userViewModel;
    private int currentPosition;
    private FragmentManager fragmentManager;
//    private Fragment exploreFragment;
    private ExploreFragment exploreFragment;
    int numberOfClick = 0;
    float volume;
    boolean isPlaying = false;
    private Context context;
    private UserModel userData;
    private List<ShortsViewHolder> shortsViewHolderList;
    String userName;
    public ShortsAdapter(List<ShortsObject> shortsDataList, Context context, FragmentManager fragmentManager, ExploreFragment exploreFragment,UserModel userData) {
        this.shortsDataList = shortsDataList;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.exploreFragment = exploreFragment;
        currentPosition = 0;
        shortsViewHolderList = new ArrayList<>();
        this.userData = userData;
        if(userData!= null) {
            this.userName = userData.getUsername();
        }else{
            this.userName = null;
        }
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
        private MotionLayout motionLayout;
        private PlayerView videoView;
        private ExoPlayer exoPlayer;
        private TextView shortsDescription, shortsTitle, listingTitle,tvComment, tvFavorites,total_comments,shorts_date_posted;
        private CardView listingCard;
        private ImageView imvShortsAvatar, imvPause, imvMore, imvAppear, imvVolume, imvShare;
        private ProgressBar videoProgressBar;
        private MediaItem mediaItem;
        private ImageButton imvCloseComment;
        private ImageButton send_comment_btn;
        private EditText comment_text;
        boolean isPaused = false;
        CommentsAdapter commentsAdapter;
        RecyclerView recycleViewComments;

        @OptIn(markerClass = UnstableApi.class) public ShortsViewHolder(@NonNull View itemView) {
            super(itemView);
            comment_text = itemView.findViewById(R.id.comment_text);
            if (userData==null) {
                // Disable the EditText
                comment_text.setEnabled(false);
            } else {
                // Enable the EditText
                comment_text.setEnabled(true);
            }
            shorts_date_posted = itemView.findViewById(R.id.shorts_date_posted);
            send_comment_btn = itemView.findViewById(R.id.send_comment_btn);
            recycleViewComments = itemView.findViewById(R.id.comment_recyclerview);
            motionLayout = itemView.findViewById(R.id.exploreLayout);
            total_comments = itemView.findViewById(R.id.total_comments);
            videoView = itemView.findViewById(R.id.videoView);
            shortsDescription = itemView.findViewById(R.id.shortsDescription);
            shortsTitle = itemView.findViewById(R.id.shortsTitle);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvFavorites = itemView.findViewById(R.id.tvFavorites);
            videoProgressBar = itemView.findViewById(R.id.progressBar);
            videoProgressBar.setVisibility(View.GONE);
            imvVolume = itemView.findViewById(R.id.imvVolume);
            imvAppear = itemView.findViewById(R.id.imv_appear);
            imvShortsAvatar = itemView.findViewById(R.id.imvShortsAvatar);
            imvCloseComment = itemView.findViewById(R.id.exit_comment_section_btn);
            listingTitle = itemView.findViewById(R.id.listingTitle);
            listingCard = itemView.findViewById(R.id.listingCard);
            videoView.setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING);
            videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            videoView.setOnClickListener(this);
            imvVolume.setOnClickListener(this);
            listingTitle.setOnClickListener(this);
            tvFavorites.setOnClickListener(this);
            tvComment.setOnClickListener(this);
            imvCloseComment.setOnClickListener(this);
            send_comment_btn.setOnClickListener(this);
            comment_text.setOnClickListener(this);
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
            if(shortsData.getPosterPictureUrl()!=null){
                Glide.with(context)
                        .load(getBaseUrl(shortsData.getPosterPictureUrl())) // Load the first URL from the array
                        .into(imvShortsAvatar);
            }
            Date datePosted = shortsData.getDatePosted();

            String shortDate = convertDateToShortDate(datePosted);
            shorts_date_posted.setText(shortDate);
            Uri shortsUri = Uri.parse(shortsData.getShortsUrl());
            shortsDescription.setText(shortsData.getDescription());
            shortsTitle.setText(shortsData.getUserName());
            tvFavorites.setText(String.valueOf(shortsData.getLikes().size()));
            int num_comments = shortsData.getComments().size();
            tvComment.setText(String.valueOf(num_comments));
            if(num_comments==0)
                total_comments.setText("Be the first comment!");
            else{
                total_comments.setText(String.valueOf(num_comments)+" comments");
            }

            if (userName!=null){
                if(shortsData.getLikes().contains(userName)){
                    tvFavorites.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_fill_favorite, 0, 0);
                }else{
                    tvFavorites.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite, 0, 0);
                }
            }
            commentsAdapter = new CommentsAdapter(context,shortsData.getComments(),shortsData.getUserName());
            recycleViewComments.setAdapter(commentsAdapter);
            recycleViewComments.setLayoutManager(new LinearLayoutManager(context));
            if (shortsData.getListing()!=null){
                listingCard.setVisibility(View.VISIBLE);
                listingTitle.setText(shortsData.getListing().getTitle());
            }
//             Create a media item representing the video
//             Create a MediaItem builder
            MediaItem.Builder mediaItemBuilder = new MediaItem.Builder();
            mediaItemBuilder.setMimeType(MimeTypes.VIDEO_MP4);
            mediaItemBuilder.setUri(shortsUri);
            if (shortsData.getShortsUrl().endsWith(".mp4")) {
                // Set MIME type to mp4 if extension is present
                mediaItemBuilder.setMimeType(MimeTypes.VIDEO_MP4);
            } else {
                // Set MIME type to DASH for URIs without .mpd extension
                mediaItemBuilder.setMimeType(MimeTypes.APPLICATION_MPD);
            }
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
        private void setFillLiked(boolean isLiked) {
            if (userName != null) {
                if (isLiked) {
                    tvFavorites.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_fill_favorite, 0, 0);
                    shortsDataList.get(getCurrentPosition()).addLike(userName);
                    int totalLikes = shortsDataList.get(getCurrentPosition()).getLikes().size();
                    tvFavorites.setText(String.valueOf(totalLikes));
                    String apiUrl = "/shorts/liked/" + shortsDataList.get(getCurrentPosition()).getId();
                    RequestBody body = RequestBody.create("", MediaType.parse("application/json"));
                    new AuthService(context).getResponse(apiUrl, true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
                        @Override
                        public void onError(String message) {
//                        unsuccessfullScreen();
                        }

                        @Override
                        public void onResponse(ResponseModel json) {
                            if (json.success) {
                            }
                        }
                    });
                } else {
                    tvFavorites.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite, 0, 0);
                    shortsDataList.get(getCurrentPosition()).removeLike(userName);
                    int totalLikes = shortsDataList.get(getCurrentPosition()).getLikes().size();
                    tvFavorites.setText(String.valueOf(totalLikes));
                    String apiUrl = "/shorts/unliked/" + shortsDataList.get(getCurrentPosition()).getId();
                    RequestBody body = RequestBody.create("", MediaType.parse("application/json"));
                    new AuthService(context).getResponse(apiUrl, true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
                        @Override
                        public void onError(String message) {
//                        unsuccessfullScreen();
                        }

                        @Override
                        public void onResponse(ResponseModel json) {
                            if (json.success) {
                            }
                        }
                    });
                }
            }else{Toast.makeText(context, "Please sign in", Toast.LENGTH_SHORT).show();}
        }

        public void onClick(View view) {
            if (view.getId() == videoView.getId()) {
                if (motionLayout.getProgress() == 1.0f) {
                    // If motionLayout is in the end state, transition it to start
                    InputMethodManager imm = (InputMethodManager) exploreFragment.requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(comment_text.getWindowToken(), 0);
                    motionLayout.transitionToStart();
                    exploreFragment.enableShortsViewPagerScroll();
                } else {
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
                                } else if (numberOfClick == 2) {
                                    appearImage(R.drawable.ic_fill_favorite);
                                    setFillLiked(true);
                                }
                                numberOfClick = 0;
                            }
                        }, 250);
                    }
                }
            }
            if (view.getId() == tvFavorites.getId()){
                if(shortsDataList.get(getCurrentPosition()).getLikes().contains(userName)){
                    setFillLiked(false);}
                else{
                    setFillLiked(true);
                }
            }
            if(view.getId() == comment_text.getId()){
                final Handler handler = new Handler();
                comment_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                comment_text.requestFocus();
                                InputMethodManager imm = (InputMethodManager) exploreFragment.requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(comment_text, InputMethodManager.SHOW_IMPLICIT);
                                imm.restartInput(comment_text);
                            }
                        }, 100);
                    }
                });
            }
            if (view.getId() == send_comment_btn.getId()) {
                InputMethodManager imm = (InputMethodManager) exploreFragment.requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(comment_text.getWindowToken(), 0);
                String commentText = comment_text.getText().toString();
                if (userData == null) {
                    goToLogin(fragmentManager);
                } else {
                    if (commentText.strip().isEmpty()) {
                        Toast.makeText(context, "Empty Comment!", Toast.LENGTH_SHORT).show();
                    } else {
                        String apiUrl = "/shorts/comment";
                        String journeyId = shortsDataList.get(getCurrentPosition()).getId();
                        String json = String.format("{\"journeyId\": \"%s\", \"comment\": \"%s\"}",
                                journeyId, commentText);
                        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
                        new AuthService(context).getResponse(apiUrl, true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
                            @Override
                            public void onError(String message) {
//                        unsuccessfullScreen();
                            }

                            @Override
                            public void onResponse(ResponseModel json) {
                                if (json.success) {

                                    exploreFragment.requireActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            comment_text.setText("");
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                            Date currentDate = new Date();
                                            String formattedDate = dateFormat.format(currentDate);
                                            Comment commentToAdd = new Comment("", journeyId, userData.getId(), commentText, formattedDate, userData);
                                            shortsDataList.get(getCurrentPosition()).addComment(commentToAdd);
                                            Collections.sort(shortsDataList.get(getCurrentPosition()).getComments(), new Comparator<Comment>() {
                                                @Override
                                                public int compare(Comment o1, Comment o2) {
                                                    // Compare dates in descending order
                                                    return o2.getDateCreated().compareTo(o1.getDateCreated());
                                                }
                                            });
                                            commentsAdapter.notifyDataSetChanged();
                                            int num_comments = shortsDataList.get(getCurrentPosition()).getComments().size();
                                            tvComment.setText(String.valueOf(num_comments));
                                            if (num_comments == 0)
                                                total_comments.setText("Be the first comment!");
                                            else {
                                                total_comments.setText(String.valueOf(num_comments) + " comments");
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
            if (view.getId() == R.id.tvComment) {
                // Trigger transition when tvComment is clicked
                motionLayout.transitionToEnd();
                exploreFragment.disableShortsViewPagerScroll();
            }
            if (view.getId() == R.id.exit_comment_section_btn) {
                // Exit transition when exit_comment_section_btn is clicked
                InputMethodManager imm = (InputMethodManager) exploreFragment.requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(comment_text.getWindowToken(), 0);
                motionLayout.transitionToStart();
                exploreFragment.enableShortsViewPagerScroll();
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
                data.putStringArray("thumbnailUrls", currListing.getThumbnailUrls());
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