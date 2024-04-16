package com.example.wayfare.Fragment;

import static com.example.wayfare.Utils.AzureStorageManager.getBaseUrl;
import static com.example.wayfare.Utils.Helper.convertStringToShortDate;
import static com.example.wayfare.Utils.Helper.goToLogin;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.arthenica.mobileffmpeg.Config;
import com.bumptech.glide.Glide;
import com.example.wayfare.Adapters.CommentsAdapter;
import com.example.wayfare.Adapters.ShortsAdapter;
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

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SingularJourneyFragment extends Fragment implements View.OnClickListener {
    int numberOfClick = 0;
    float volume;
    private boolean isFragmentPaused = false;
    private boolean isFragmentDestroyed = false;

    List<ShortsObject> singularJourneyList = new ArrayList<>();
    UserViewModel userViewModel;
    UserModel userData;
    String userName;

    private MotionLayout motionLayout;
    private PlayerView videoView;
    private ExoPlayer exoPlayer;
    private TextView shortsDescription, shortsTitle, listingTitle,tvComment, tvFavorites,total_comments,shorts_date_posted;
    private CardView listingCard,imvShortsAvatarCard;
    private ImageView imvShortsAvatar, imvPause, imvMore, imvAppear, imvVolume, imvBackSingle,closeDeleteJourneyButton;
    private ProgressBar videoProgressBar;
    private MediaItem mediaItem;
    private ImageButton imvCloseComment;
    private Button delete_journey;
    private ImageButton send_comment_btn;
    private EditText comment_text;
    private CardView to_delete_short_bar;
    boolean isPaused = false;
    CommentsAdapter commentsAdapter;
    RecyclerView recycleViewComments;
    ShortsObject singleJourney;


    public SingularJourneyFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        userData = userViewModel.getUserProfileData();

        View view = inflater.inflate(R.layout.item_explore, container, false);
        makeLayoutFullscreen(view);
        return view;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        String journeyId = getArguments().getString("journeyId");
        comment_text = view.findViewById(R.id.comment_text);
        if (userData==null) {
            // Disable the EditText
            comment_text.setEnabled(false);
        } else {
            // Enable the EditText
            comment_text.setEnabled(true);
        }
        motionLayout = view.findViewById(R.id.exploreLayout);
        shorts_date_posted = view.findViewById(R.id.shorts_date_posted);
        send_comment_btn = view.findViewById(R.id.send_comment_btn);
        recycleViewComments = view.findViewById(R.id.comment_recyclerview);
        motionLayout = view.findViewById(R.id.exploreLayout);
        total_comments = view.findViewById(R.id.total_comments);
        videoView = view.findViewById(R.id.videoView);
        shortsDescription = view.findViewById(R.id.shortsDescription);
        shortsTitle = view.findViewById(R.id.shortsTitle);
        tvComment = view.findViewById(R.id.tvComment);
        tvFavorites = view.findViewById(R.id.tvFavorites);
        videoProgressBar = view.findViewById(R.id.progressBar);
        imvVolume = view.findViewById(R.id.imvVolume);
        imvAppear = view.findViewById(R.id.imv_appear);
        imvMore = view.findViewById(R.id.imvMore);
        imvShortsAvatar = view.findViewById(R.id.imvShortsAvatar);
        imvShortsAvatarCard = view.findViewById(R.id.imvShortsAvatarCard);
        imvCloseComment = view.findViewById(R.id.exit_comment_section_btn);
        listingTitle = view.findViewById(R.id.listingTitle);
        listingCard = view.findViewById(R.id.listingCard);
        imvBackSingle = view.findViewById(R.id.imvBackSingle);
        to_delete_short_bar = view.findViewById(R.id.to_delete_short_bar);
        delete_journey = to_delete_short_bar.findViewById(R.id.delete_journey);
        closeDeleteJourneyButton = to_delete_short_bar.findViewById(R.id.closeDeleteJourneyButton);
        imvBackSingle.setVisibility(View.VISIBLE);
        videoView.setOnClickListener(this);
        imvVolume.setOnClickListener(this);
        listingTitle.setOnClickListener(this);
        tvFavorites.setOnClickListener(this);
        tvComment.setOnClickListener(this);
        imvCloseComment.setOnClickListener(this);
        send_comment_btn.setOnClickListener(this);
//            comment_text.setOnClickListener(this);
        imvShortsAvatar.setOnClickListener(this);
        imvShortsAvatarCard.setOnClickListener(this);
        imvBackSingle.setOnClickListener(this);
        imvMore.setOnClickListener(this);
        delete_journey.setOnClickListener(this);
        closeDeleteJourneyButton.setOnClickListener(this);


        new AuthService(getContext()).getResponse("/api/v1/short/" + journeyId, false, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {
                makeToast(message);
                getParentFragmentManager().popBackStack();
            }

            @Override
            public void onResponse(ResponseModel json) {
                if (json.success){
                    singleJourney = new Gson().fromJson(json.data, ShortsObject.class);
                    singularJourneyList.add(singleJourney);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setShortsData(singleJourney);
                        }
                    });
                }
                else{
                    makeToast(json.data.getAsString());
                    getParentFragmentManager().popBackStack();
                }
            }
        });

    }
    @Override
    public void onPause() {
        super.onPause();
        isFragmentPaused = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isFragmentPaused = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopVideo();
        isFragmentDestroyed = true;
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        stopVideo();
    }

    // Method to check if fragment is paused
    public boolean isFragmentPaused() {
        return isFragmentPaused;
    }

    // Method to check if fragment is destroyed
    public boolean isFragmentDestroyed() {
        return isFragmentDestroyed;
    }

    public void makeToast(String msg) {

        if (getActivity() == null) {
            Log.d("ERROR", "ACTIVITY CONTEXT IS NULL, UNABLE TO MAKE TOAST");
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void stopVideo() {
        if (exoPlayer != null) {
            if (exoPlayer.getPlaybackState() == Player.STATE_READY) {
                exoPlayer.setPlayWhenReady(false);
                exoPlayer.stop();
                exoPlayer.release();
                exoPlayer = null;
            }
        }
    }
    public void disappearImage(){
        imvAppear.setVisibility(View.GONE);
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
        }
    }
    public void pauseVideo() {
        if(exoPlayer!=null) {
            if (exoPlayer.getPlaybackState() == Player.STATE_READY) {
                exoPlayer.setPlayWhenReady(false);
                appearImage(R.drawable.ic_baseline_play_arrow_24);
            }
        }
    }

    void setShortsData(ShortsObject shortsData){
            if(shortsData.getPosterPictureUrl()!=null){
                Glide.with(getActivity())
                        .load(getBaseUrl(shortsData.getPosterPictureUrl()))// Load the first URL from the array
                        .override(30, 30) // Set the dimensions to 30dp by 30dp
                        .into(imvShortsAvatar);
            }
            String datePosted = shortsData.getDatePosted();
            String shortDate = convertStringToShortDate(datePosted);
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
//            recycleViewComments.setAdapter(null);
//            commentsAdapter = new CommentsAdapter(context,shortsData.getComments(),shortsData.getUserName(),fragmentManager);
//            recycleViewComments.setAdapter(commentsAdapter);
//            recycleViewComments.setLayoutManager(new LinearLayoutManager(context));
            if (shortsData.getListing()!=null){
                listingCard.setVisibility(View.VISIBLE);
                listingTitle.setText(shortsData.getListing().getTitle());
            }else{listingCard.setVisibility(View.GONE);}
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
            exoPlayer.setPlayWhenReady(true);
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
                    if (isFragmentPaused() || isFragmentDestroyed()) {
                        if(exoPlayer != null) {
                            // Loading is complete, release resources
                            exoPlayer.stop();
                            exoPlayer.release();
                            exoPlayer = null;
                        }
                    }
                }
            }
        });
    }
    private void setFillLiked(boolean isLiked) {
        if (userData != null) {
            if (isLiked) {
                tvFavorites.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_fill_favorite, 0, 0);
                singleJourney.addLike(userData.getUsername());
                int totalLikes = singleJourney.getLikes().size();
                tvFavorites.setText(String.valueOf(totalLikes));
                String apiUrl = "/shorts/liked/" + singleJourney.getId();
                RequestBody body = RequestBody.create("", MediaType.parse("application/json"));
                new AuthService(requireActivity()).getResponse(apiUrl, true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
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
                singleJourney.removeLike(userName);
                int totalLikes = singleJourney.getLikes().size();
                tvFavorites.setText(String.valueOf(totalLikes));
                String apiUrl = "/shorts/unliked/" + singleJourney.getId();
                RequestBody body = RequestBody.create("", MediaType.parse("application/json"));
                new AuthService(requireActivity()).getResponse(apiUrl, true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
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
        }else{Toast.makeText(requireActivity(), "Please sign in", Toast.LENGTH_SHORT).show();}
    }
    public void onClick(View view) {
        if (view.getId() == videoView.getId()) {
            if (motionLayout.getProgress() == 1.0f) {
                // If motionLayout is in the end state, transition it to start
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(comment_text.getWindowToken(), 0);
                motionLayout.transitionToStart();
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
                                    //appearImage(R.drawable.ic_baseline_play_arrow_24);
                                } else {
                                    playVideo();
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
        if(view.getId()==imvBackSingle.getId()){
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

            // Set custom animations for exit and pop exit
            transaction.setCustomAnimations(R.anim.slide_left_to_right,R.anim.slide_out_right_to_left);

            // Remove the current fragment
            transaction.remove(this).commit();
        }
        if(view.getId() == imvShortsAvatar.getId()){
            if(exoPlayer!=null) {
                if (exoPlayer.getPlaybackState() == Player.STATE_READY) {
                    exoPlayer.setPlayWhenReady(false);
                }
            }
            Bundle username = new Bundle();
            username.putString("username", singleJourney.getUserName());
            ProfileFragment pf = new ProfileFragment();
            pf.setArguments(username);
            Helper.goToFragmentSlideInRight(getParentFragmentManager(), R.id.container, pf);
        }
        if(view.getId() == imvMore.getId()){
              if(Objects.equals(userData.getUsername(),singleJourney.getUserName())) {
//            Animation slideUpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
                  to_delete_short_bar.setVisibility(View.VISIBLE);
              }else{
                  makeToast("Function not available");
              }
//            to_delete_short_bar.startAnimation(slideUpAnimation);
        }
        if(view.getId()==closeDeleteJourneyButton.getId()){
//            Animation slideDownAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_bottom);
            to_delete_short_bar.setVisibility(View.GONE);
//            to_delete_short_bar.startAnimation(slideDownAnimation);
        }
        if(view.getId()==delete_journey.getId()){
            String apiUrl = "/short/delete/" + singleJourney.getId();
            RequestBody body = RequestBody.create("", MediaType.parse("application/json"));
            new AuthService(requireActivity()).getResponse(apiUrl, true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
                @Override
                public void onError(String message) {
                        makeToast(message);

                }

                @Override
                public void onResponse(ResponseModel json) {
                    if (json.success) {
                        makeToast("Journey Deleted");
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                // Set custom animations for exit and pop exit
                                transaction.setCustomAnimations(R.anim.slide_left_to_right,R.anim.slide_out_right_to_left);
                                // Remove the current fragment
                                transaction.remove(SingularJourneyFragment.this).commit();

                            }
                        });

                    }
                }
            });
        }
        if (view.getId() == tvFavorites.getId()){
            if(singleJourney.getLikes().contains(userName)){
                setFillLiked(false);}
            else{
                setFillLiked(true);
            }
        }
//            if(view.getId() == comment_text.getId()){
//                comment_text.requestFocus();
//                InputMethodManager imm = (InputMethodManager) exploreFragment.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(comment_text, InputMethodManager.SHOW_IMPLICIT);
//            }
        if (view.getId() == send_comment_btn.getId()) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(comment_text.getWindowToken(), 0);
            String commentText = comment_text.getText().toString();
            if (userData == null) {
                goToLogin(getParentFragmentManager());
            } else {
                if (commentText.strip().isEmpty()) {
                    Toast.makeText(getContext(), "Empty Comment!", Toast.LENGTH_SHORT).show();
                } else {
                    String apiUrl = "/shorts/comment";
                    String journeyId = singleJourney.getId();
                    String json = String.format("{\"journeyId\": \"%s\", \"comment\": \"%s\"}",
                            journeyId, commentText);
                    RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
                    new AuthService(getContext()).getResponse(apiUrl, true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
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
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    Date currentDate = Date.from(Instant.now());
                    String formattedDate = dateFormat.format(currentDate);
                    Comment commentToAdd = new Comment("", journeyId, userData.getId(), commentText, formattedDate, userData);
                    singleJourney.addComment(commentToAdd);
                    int num_comments = singleJourney.getComments().size();
                    String totalCommentsText;
                    if (num_comments == 0) {
                        totalCommentsText = "Be the first comment!";
                    }
                    else {
                        totalCommentsText = String.format("%s comments",num_comments);
                    }
                    Collections.sort(singleJourney.getComments(), new Comparator<Comment>() {
                        @Override
                        public int compare(Comment o1, Comment o2) {
                            // Compare dates in descending order
                            return o2.getDateCreated().compareTo(o1.getDateCreated());
                        }
                    });
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            comment_text.setText("");
                            total_comments.setText(totalCommentsText);
                            commentsAdapter.notifyDataSetChanged();
                            tvComment.setText(String.valueOf(num_comments));

                        }
                    });
                }
            }
        }
        if (view.getId() == R.id.tvComment) {
            // Trigger transition when tvComment is clicked
            commentsAdapter = new CommentsAdapter(getContext(),singleJourney.getComments(),singleJourney.getUserName(),getParentFragmentManager(),null,this);
            recycleViewComments.setAdapter(commentsAdapter);
            recycleViewComments.setLayoutManager(new LinearLayoutManager(getContext()));
            motionLayout.transitionToEnd();
        }
        if (view.getId() == R.id.exit_comment_section_btn) {
            // Exit transition when exit_comment_section_btn is clicked
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(comment_text.getWindowToken(), 0);
            recycleViewComments.setAdapter(null);
            Glide.get(getContext()).clearMemory();
            motionLayout.transitionToStart();
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
            TourListModel currListing = singleJourney.getListing();
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

            Helper.goToFragmentSlideInRightArgs(data, getParentFragmentManager(), R.id.container, tourListingFullFragment);
        }
    }
    private void makeLayoutFullscreen(View view) {
        // Adjust layout parameters to match parent's dimensions
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        view.setLayoutParams(layoutParams);

        // Set flags to ensure that the layout covers system bars
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11 (API level 30) and above
            WindowInsetsController controller = view.getWindowInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.navigationBars());
                controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            // For Android versions below 11
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}