package com.example.wayfare.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wayfare.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddShorts extends AppCompatActivity implements View.OnClickListener {
    private static final int STORAGE_PERMISSION_CODE = 1;
    CameraManager manager;
    FrameLayout cameraFrameLayout;
    TextureView textureFront;
    String frontId, backId, defaultId;
    Size previewSize;
    Size videoSize;
    Button btnUploadVideo;
    Button btnExit;
    Button btnPause;
    Button btnContinue;

    MediaRecorder mediaRecorder;
    File videoFileHolder;
    Button btnStartRecord;
    ImageButton btnFlip;
    Button btnStopRecord;
    boolean isRecording = false;
    boolean isPaused = false;

    String videoFileName;
    String userId;
    File videoFolder;

    Animation animRotate;

    int totalRotation;
    static int sensorToDeviceToRotation(CameraCharacteristics characteristics, int deviceOrientation) {
        if (deviceOrientation == android.view.OrientationEventListener.ORIENTATION_UNKNOWN) return 0;
        int sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);

        // Round device orientation to a multiple of 90
        deviceOrientation = (deviceOrientation + 45) / 90 * 90;

        // Reverse device orientation for front-facing cameras
        boolean facingFront = characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT;
        if (facingFront) deviceOrientation = -deviceOrientation;

        // Calculate desired JPEG orientation relative to camera orientation to make
        // the image upright relative to the device orientation
        int jpegOrientation = (sensorOrientation + deviceOrientation + 360) % 360;

        return jpegOrientation;
    }
    CameraDevice mainCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_shorts);
        mediaRecorder = new MediaRecorder();

        btnUploadVideo = findViewById(R.id.btnUploadVideo);
        btnUploadVideo.setOnClickListener(this);
        btnStartRecord = findViewById(R.id.button_record);
        btnExit = findViewById(R.id.button_close);
        btnFlip = findViewById(R.id.imb_flip_camera);
        btnPause = findViewById(R.id.button_pause);
        btnContinue = findViewById(R.id.button_continue);
        btnStopRecord = findViewById(R.id.button_stop);
        // Get Camera TextureView
        cameraFrameLayout = findViewById(R.id.camera_frame);
        textureFront = findViewById(R.id.texture_view_front);

        animRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        // Filter list
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED) {

            }else {
                Toast.makeText(this,"Permission is denied, please allow permission.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imb_flip_camera) {
            view.startAnimation(animRotate);


        }
        if (view.getId() == btnUploadVideo.getId()) {
            //            progressDialog = new ProgressDialog(MainActivity.this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION},
                        STORAGE_PERMISSION_CODE);
            }

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("video/*");
            //startActivityForResult(intent, 5);
        }

        if (view.getId() == R.id.button_record) {
            btnStopRecord.setVisibility(View.VISIBLE);
            btnPause.setVisibility(View.VISIBLE);
            btnFlip.setVisibility(View.GONE);
            findViewById(R.id.tv_flip_camera).setVisibility(View.GONE);
            btnUploadVideo.setVisibility(View.GONE);
            btnStartRecord.setVisibility(View.GONE);
            btnExit.setVisibility(View.GONE);

            isRecording = true;

        }
        if (view.getId() == R.id.button_pause) {
            if (!isPaused) {
                btnPause.setVisibility(View.GONE);
                btnContinue.setVisibility(View.VISIBLE);
                isPaused = true;
            }
        }
        if (view.getId() == R.id.button_continue) {
            if (isPaused) {
                btnPause.setVisibility(View.VISIBLE);
                btnContinue.setVisibility(View.GONE);
                isPaused = false;
            }
        }
        if (view.getId() == R.id.button_stop) {
            if (isRecording) {

                btnStartRecord.setVisibility(View.VISIBLE);
                isRecording = false;
                btnFlip.setVisibility(View.VISIBLE);
                findViewById(R.id.tv_flip_camera).setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);
                btnUploadVideo.setVisibility(View.VISIBLE);
                btnStopRecord.setVisibility(View.GONE);
                btnPause.setVisibility(View.GONE);
                btnContinue.setVisibility(View.GONE);
                btnExit.setVisibility(View.VISIBLE);
//                connectCamera();
            }
        }
        if (view.getId() == R.id.button_close) {
            finish();
            overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_out_bottom);
        }
    }



}