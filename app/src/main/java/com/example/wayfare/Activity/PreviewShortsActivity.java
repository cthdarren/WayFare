package com.example.wayfare.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.SimpleExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.wayfare.R;

public class PreviewShortsActivity extends AppCompatActivity implements View.OnClickListener {
    Uri videoUri;
    private PlayerView playerView;
    private ExoPlayer exoPlayer;
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_preview_shorts);
        btnBack = findViewById(R.id.button_back);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String videoPath= bundle.getString("videoUri");
        videoUri = Uri.parse(videoPath);
        playerView = findViewById(R.id.preview_player);
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);
        exoPlayer.setMediaItem(buildMediaItem(videoUri));
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.prepare();

    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_back) {
            finish();;
            //overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_out_bottom);

        }
    }
    private MediaItem buildMediaItem(Uri uri) {
        return new MediaItem.Builder()
                .setUri(uri)
                .setMimeType("video/*") // Set the mime type of the media item
                .build();
    }
    @Override
    protected void onStart() {
        super.onStart();
        exoPlayer.setPlayWhenReady(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        exoPlayer.setPlayWhenReady(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoPlayer.release();
    }

}