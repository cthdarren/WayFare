package com.example.wayfare.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.media.MediaMetadataRetriever;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wayfare.R;

import java.io.IOException;

public class PostShortActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnBack;
    Uri videoUri;
    ImageView imgShort;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_short);
        btnBack = findViewById(R.id.btn_to_preview);
        imgShort = findViewById(R.id.imgShort);
        btnBack.setOnClickListener(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String videoPath= bundle.getString("videoUri");
        videoUri = Uri.parse(videoPath);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this,videoUri);
        Bitmap bitmap = retriever.getFrameAtTime();
        imgShort.setImageBitmap(bitmap);
        try {
            retriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_to_preview) {
            finish();
            //overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_out_bottom);
        }
    }
}