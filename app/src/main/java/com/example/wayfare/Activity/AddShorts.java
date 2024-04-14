package com.example.wayfare.Activity;

import android.Manifest;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;

import android.provider.MediaStore;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.example.wayfare.R;

import androidx.activity.OnBackPressedCallback;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.FocusMeteringResult;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.video.*;
import androidx.camera.core.FocusMeteringAction;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AddShorts extends AppCompatActivity implements View.OnClickListener {
    private static final int STORAGE_PERMISSION_CODE = 1;
    private ActivityResultLauncher<Intent> videoPickerLauncher;
    private final MutableLiveData<String> captureLiveStatus = new MutableLiveData<>();
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    private VideoCapture<Recorder> videoCapture;
    private Recording currentRecording;
    private VideoRecordEvent recordingState;
    private PreviewView previewView;
    private TextView liveCountdown;
    ExecutorService service;
    Button btnUploadVideo;
    Button btnExit;
    Button btnPause;
    Button btnContinue;
    ImageView focusIcon;
    MediaRecorder mediaRecorder;
    Button btnStartRecord;
    ImageButton btnFlip, toggleFlash;
    Button btnStopRecord;
    String userName;
    boolean isRecording = false;
    boolean isPaused = false;
    int cameraFacing = CameraSelector.LENS_FACING_BACK;
    Animation animRotate;
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera(cameraFacing);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_shorts);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO},
                    STORAGE_PERMISSION_CODE);
            startCamera(cameraFacing);
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            startCamera(cameraFacing);
        }
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userName = bundle.getString("userName");
        //rectOverlay = findViewById(R.id.rect_overlay);
        mediaRecorder = new MediaRecorder();
        previewView = findViewById(R.id.previewView);
        btnUploadVideo = findViewById(R.id.btnUploadVideo);
        btnStartRecord = findViewById(R.id.button_record);
        btnExit = findViewById(R.id.button_close);
        btnFlip = findViewById(R.id.imb_flip_camera);
        btnPause = findViewById(R.id.button_pause);
        btnContinue = findViewById(R.id.button_continue);
        btnStopRecord = findViewById(R.id.button_stop);
        focusIcon = findViewById(R.id.img_focus);
        liveCountdown = findViewById(R.id.liveCountdown);
        toggleFlash = findViewById(R.id.toggleFlash);
        btnStartRecord.setOnClickListener(this);
        btnUploadVideo.setOnClickListener(this);
        btnFlip.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        btnStopRecord.setOnClickListener(this);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Create an Intent to launch the MainActivity
                Intent intent = new Intent(AddShorts.this, MainActivity.class);

                // Add any extra data if needed
                // intent.putExtra("key", value);

                // Start the MainActivity
                startActivity(intent);

                // Finish the current activity
                finish();

                // Apply custom transition animation
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_out_bottom);
            }
        };

        // Add the callback to the onBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);

        // Get Camera TextureView

        animRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        captureLiveStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String text) {
                liveCountdown.post(new Runnable() {
                    @Override
                    public void run() {
                        liveCountdown.setText(text);
                    }
                });
            }
        });

        // Set initial value for captureLiveStatus
        captureLiveStatus.setValue("");


        // Filter list
        // Initialize the video picker launcher
        videoPickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedVideoUri = result.getData().getData();
                        long videoDuration = getVideoDuration(selectedVideoUri);
                        if (videoDuration < 60000) {
//                            Toast.makeText(this, "Video selected: " + selectedVideoUri.toString(), Toast.LENGTH_SHORT).show();
                            startUploadingActivity(selectedVideoUri);
                        } else {
                            Toast.makeText(this, "Please select a video shorter than 60 seconds", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        service = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
            if (cameraFacing == CameraSelector.LENS_FACING_BACK) {
                cameraFacing = CameraSelector.LENS_FACING_FRONT;
            } else {
                cameraFacing = CameraSelector.LENS_FACING_BACK;
            }
            startCamera(cameraFacing);



        }
        if (view.getId() == btnUploadVideo.getId()) {
            checkStoragePermissionAndPickVideo();
            //            progressDialog = new ProgressDialog(MainActivity.this);
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION},
//                        STORAGE_PERMISSION_CODE);
//            }
//
//            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.setType("video/*");
            //startActivityForResult(intent, 5);
        }

        if (view.getId() == R.id.button_record) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                        STORAGE_PERMISSION_CODE);
            }
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            startRecording();

        }
        if (view.getId() == R.id.button_pause) {
            if (!isPaused) {
                btnPause.setVisibility(View.GONE);
                btnContinue.setVisibility(View.VISIBLE);
                isPaused = true;
                currentRecording.pause();
            }
        }
        if (view.getId() == R.id.button_continue) {
            if (isPaused) {
                btnPause.setVisibility(View.VISIBLE);
                btnContinue.setVisibility(View.GONE);
                isPaused = false;
                currentRecording.resume();
            }
        }
        if (view.getId() == R.id.button_stop) {
            stopRecording();

        }
        if (view.getId() == R.id.button_close) {
            Intent intent = new Intent(AddShorts.this, MainActivity.class);

            // Add any extra data if needed
            // intent.putExtra("key", value);

            // Start the MainActivity
            startActivity(intent);

            // Finish the current AddShorts Activity
            finish();

            // Apply custom transition animation
            overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_out_bottom);
        }
    }


    // Create an OnBackPressedCallback
    private void checkStoragePermissionAndPickVideo() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION},
                    STORAGE_PERMISSION_CODE);
            pickVideo();
        } else {
            pickVideo();
        }
    }
    private void pickVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        videoPickerLauncher.launch(intent);
    }
    void startUploadingActivity(Uri videoUri) {
        captureLiveStatus.setValue("");
//        String dashOutputPath = convertVideoToDASH(getRealPathFromURI(videoUri));
        // Conversion successful, proceed with new activity
        Intent i = new Intent(this,
                PreviewShortsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        bundle.putString("videoUri", videoUri.toString());
        i.putExtras(bundle);
        startActivity(i);
    }
    private String convertVideoToDASH(String inputVideoPath) {
        String outputFilePath = getOutputFilePathForDASH(); // Generate unique output path

        // FFmpeg command building (example, customize based on your needs)
        String[] command = new String[]{
                "-i", inputVideoPath, // Input file (from Uri)
                "-map", "v",  // Map the first video stream by default
                "-c:v", "libx264", // Specify video codec (H.264)
                "-preset", "fast", // Use ultrafast preset for faster encoding
                "-crf", "16", // Constant rate factor for quality (lower values mean better quality but larger file size)
                "-c:a", "aac", // Audio codec
                "-strict", "experimental",
                "-b:v", "6M",  // Set video bitrate (adjust based on your needs)
                "-maxrate:v", "5M",  // Set maximum bitrate (optional)
                "-bufsize:v", "5M",  // Set buffer size (optional)
                outputFilePath // Output DASH file path
        };

        int rc = FFmpeg.execute(command);

        if (rc == Config.RETURN_CODE_SUCCESS) {
            return outputFilePath; // Conversion successful, return output path
        } else {
            Log.e("FFmpeg", "FFmpeg command failed with return code: " + rc);
            return null; // Conversion failed, return null
        }
    }
    private String getOutputFilePathForDASH() {
        // Get internal storage directory
        File storageDir = getFilesDir();
        File outputFile = new File(storageDir, "cached_output.mpd");
        return outputFile.getAbsolutePath();
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor == null) {
            // Query failed, return null
            return null;
        }
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        String realPath = cursor.getString(columnIndex);
        cursor.close();
        return realPath;
    }
    private long getVideoDuration(Uri videoUri) {
        String[] filePathColumn = { MediaStore.Video.Media.DURATION };
        Cursor cursor = getContentResolver().query(videoUri, filePathColumn, null, null, null);
        int durationIndex = cursor.getColumnIndex(MediaStore.Video.Media.DURATION);
        cursor.moveToFirst();
        long duration = cursor.getLong(durationIndex);
        cursor.close();
        return duration;
    }

    public void startCamera(int cameraFacing) {
        ListenableFuture<ProcessCameraProvider> processCameraProvider = ProcessCameraProvider.getInstance(AddShorts.this);
        processCameraProvider.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = processCameraProvider.get();
                Preview preview = new Preview.Builder()
                        .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                        .build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                Recorder recorder = new Recorder.Builder()
                        .setQualitySelector(QualitySelector.from(Quality.HD))
                        .build();
                videoCapture = VideoCapture.withOutput(recorder);

                cameraProvider.unbindAll();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraFacing).build();
                // Perform flip animation

                Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture);
                toggleFlash.setOnClickListener(view -> toggleFlash(camera));
                previewView.setOnTouchListener((view, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        float x = event.getX();
                        float y = event.getY();
                        focusIcon.setVisibility(View.VISIBLE);
                        focusIcon.setX(x);
                        focusIcon.setY(y);
                        focusIcon.setColorFilter(Color.WHITE);
                        focusIcon.animate()
                                .scaleX(1.5f)  // scale up to 150%
                                .scaleY(1.5f)  // scale up to 150%
                                .setDuration(200)  // duration of the animation in milliseconds
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Animation ended, scale back to original size
                                        focusIcon.animate()
                                                .scaleX(1.0f)  // scale down to original size
                                                .scaleY(1.0f)  // scale down to original size
                                                .setDuration(200)  // duration of the animation in milliseconds
                                                .start();
                                    }
                                })
                                .start();
                        // Convert touch coordinates to camera's metering point
                        MeteringPointFactory factory = previewView.getMeteringPointFactory();
                        MeteringPoint point = factory.createPoint(x, y);

                        // Create action to focus camera
                        FocusMeteringAction action = new FocusMeteringAction.Builder(point,
                                FocusMeteringAction.FLAG_AF)
                                .build();

                        // Execute the focus action
                        ListenableFuture<FocusMeteringResult> future = camera.getCameraControl().startFocusAndMetering(action);
                        future.addListener(() -> {
                            try {
                                // Get the result of the focus action
                                FocusMeteringResult result = future.get();
                                if (result.isFocusSuccessful()) {
                                    // Focus was successful, handle accordingly
                                    focusIcon.setColorFilter(Color.GREEN);

                                } else {
                                    // Focus failed, handle accordingly
                                    focusIcon.setColorFilter(Color.RED);
                                }
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }, ContextCompat.getMainExecutor(AddShorts.this));
                        view.performClick();
                    }
                    return true;
                });

//                toggleFlash.setOnClickListener(view -> toggleFlash(camera));
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(AddShorts.this));
    }
    public void startRecording() {
        String name = "Wayfare-Shorts-" +
                new SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                        .format(System.currentTimeMillis()) + ".mp4";
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, name);
        MediaStoreOutputOptions mediaStoreOutput = new MediaStoreOutputOptions.Builder(
                getContentResolver(),
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                .setContentValues(contentValues)
                .build();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                    STORAGE_PERMISSION_CODE);
        }
        try {
            currentRecording = videoCapture.getOutput()
                    .prepareRecording(AddShorts.this, mediaStoreOutput)
                   // .withAudioEnabled()
                    .start(ContextCompat.getMainExecutor(AddShorts.this), videoRecordEvent -> {
                        RecordingStats stats = videoRecordEvent.getRecordingStats();
                        long time = TimeUnit.NANOSECONDS.toSeconds(stats.getRecordedDurationNanos());
                        String text = String.format(time + "s/60s");
                        if (time > 10) {
                            stopRecording();
                        } else {
                            captureLiveStatus.setValue(text);
                        }
                        captureLiveStatus.setValue(text);
                        if (!(videoRecordEvent instanceof VideoRecordEvent.Status)) {
                            recordingState = videoRecordEvent;
                        }
                        if (videoRecordEvent instanceof VideoRecordEvent.Start) {
                            btnStopRecord.setVisibility(View.VISIBLE);
                            btnPause.setVisibility(View.VISIBLE);
                            btnFlip.setVisibility(View.GONE);
                            btnUploadVideo.setVisibility(View.GONE);
                            btnStartRecord.setVisibility(View.GONE);
                            btnExit.setVisibility(View.GONE);
                            isRecording = true;
                        } else if (videoRecordEvent instanceof VideoRecordEvent.Finalize) {
                            if (!((VideoRecordEvent.Finalize) videoRecordEvent).hasError()) {
                                String msg = "Video capture succeeded: " + ((VideoRecordEvent.Finalize) videoRecordEvent).getOutputResults().getOutputUri();
                                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                                Uri capturedVideoUri = ((VideoRecordEvent.Finalize) videoRecordEvent).getOutputResults().getOutputUri();
                                startUploadingActivity(capturedVideoUri);
                            } else {
                                currentRecording.close();
                                currentRecording = null;
                                String msg = "Error: " + ((VideoRecordEvent.Finalize) videoRecordEvent).getError();
                                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }catch (Exception e) {
            Log.e("audio", "Error during recording preparation:", e);
        }
    }
    private void stopRecording(){
        if (currentRecording == null || recordingState instanceof VideoRecordEvent.Finalize) {
            return;  // Exit if no active recording or already finalized
        }
        // Stop the recording
        Recording recording = currentRecording;
        if (recording != null) {
            recording.stop();
            recording.close();
            currentRecording = null;
        }

        btnStartRecord.setVisibility(View.VISIBLE);
        isRecording = false;
        btnFlip.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.GONE);
        btnUploadVideo.setVisibility(View.VISIBLE);
        btnStopRecord.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);
        btnContinue.setVisibility(View.GONE);
        btnExit.setVisibility(View.VISIBLE);
    }
    private void toggleFlash(Camera camera) {
        if (camera.getCameraInfo().hasFlashUnit()) {
            if (camera.getCameraInfo().getTorchState().getValue() == 0) {
                camera.getCameraControl().enableTorch(true);
                toggleFlash.setImageResource(R.drawable.round_flash_on_24);
            } else {
                camera.getCameraControl().enableTorch(false);
                toggleFlash.setImageResource(R.drawable.round_flash_off_24);
            }
        } else {
            runOnUiThread(() -> Toast.makeText(AddShorts.this, "Flash is not available currently", Toast.LENGTH_SHORT).show());
        }
    }
    private void updateUI(VideoRecordEvent event) {
        RecordingStats stats = event.getRecordingStats();
        //long size = stats.getNumBytesRecorded() / 1000;
        long time = TimeUnit.NANOSECONDS.toSeconds(stats.getRecordedDurationNanos());
        String text = String.format(time + "s/60s");

        captureLiveStatus.setValue(text);

    }

    // Container for information about each video.
    private static class Video {
        private final Uri uri;
        private final String name;
        private final int duration;
        private final int size;

        public Video(Uri uri, String name, int duration, int size) {
            this.uri = uri;
            this.name = name;
            this.duration = duration;
            this.size = size;
        }
    }




}
