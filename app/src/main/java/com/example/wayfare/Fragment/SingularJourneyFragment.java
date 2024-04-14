package com.example.wayfare.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.OptIn;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.PlayerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.wayfare.Adapters.ShortsAdapter;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.ShortsObject;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.Models.UserModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.ViewModel.UserViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SingularJourneyFragment extends Fragment {

    List<ShortsObject> singularJourneyList = new ArrayList<>();
    UserViewModel userViewModel;
    UserModel userData;
    String userName;

    private PlayerView videoView;
    private ExoPlayer exoPlayer;
    private TextView shortsDescription, shortsTitle, listingTitle,tvComment, tvFavorites;
    private CardView listingCard;
    private ImageView imvAvatar, imvPause, imvMore, imvAppear, imvVolume, imvShare;
    private ProgressBar videoProgressBar;
    private MediaItem mediaItem;
    boolean isPaused = false;
    MotionLayout parentLayout;


    public SingularJourneyFragment(){}
    @OptIn(markerClass = UnstableApi.class) @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String journeyId = getArguments().getString("journeyId");
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        userData = userViewModel.getUserProfileData();

        View view = inflater.inflate(R.layout.item_explore, container, false);

        parentLayout = view.findViewById(R.id.exploreLayout);
        videoView = view.findViewById(R.id.videoView);
        shortsDescription = view.findViewById(R.id.shortsDescription);
        shortsTitle = view.findViewById(R.id.shortsTitle);
        tvComment = view.findViewById(R.id.tvComment);
        tvFavorites = view.findViewById(R.id.tvFavorites);
        videoProgressBar = view.findViewById(R.id.progressBar);
        videoProgressBar.setVisibility(View.GONE);
        imvVolume = view.findViewById(R.id.imvVolume);
        imvAppear = view.findViewById(R.id.imv_appear);
        listingTitle = view.findViewById(R.id.listingTitle);
        listingCard = view.findViewById(R.id.listingCard);
        videoView.setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING);
        videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        new AuthService(getContext()).getResponse("/api/v1/short/" + journeyId, false, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {
                makeToast(message);
                getParentFragmentManager().popBackStack();
            }

            @Override
            public void onResponse(ResponseModel json) {
                if (json.success){
                    ShortsObject singleJourney = new Gson().fromJson(json.data, ShortsObject.class);
                    singularJourneyList.add(singleJourney);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setShortsData(singleJourney);
                            exoPlayer.play();
                        }
                    });
                }
                else{
                    makeToast(json.data.getAsString());
                    getParentFragmentManager().popBackStack();
                }
            }
        });

        return view;
    }

    public void makeToast(String msg) {

        if (getActivity() == null) {
            Log.d("ERROR", "ACTIVITY CONTEXT IS NULL, UNABLE TO MAKE TOAST");
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    void setShortsData(ShortsObject shortsData){
        Uri shortsUri = Uri.parse(shortsData.getShortsUrl());
        shortsDescription.setText(shortsData.getDescription());
        shortsTitle.setText(shortsData.getUserName());
        if (userName!=null){
                tvFavorites.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite, 0, 0);
        }
        if (shortsData.getListing()!=null){
            listingCard.setVisibility(View.VISIBLE);
            listingTitle.setText(shortsData.getListing().getTitle());
        }

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
        imvAppear.setVisibility(View.GONE);
    }
}