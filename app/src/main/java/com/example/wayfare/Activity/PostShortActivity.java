package com.example.wayfare.Activity;
import android.content.ContentResolver;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.example.wayfare.Activity.MainActivity;
import com.example.wayfare.Adapters.PreviewListingsAdapter;
import com.example.wayfare.Adapters.YourListingsAdapter;
import com.example.wayfare.Fragment.CreateListing.CreateListingFragmentSuccess;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.AzureStorageManager;
import com.example.wayfare.BuildConfig;
import com.example.wayfare.Models.ResponseModel;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.media.MediaMetadataRetriever;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azure.core.util.Context;
import com.azure.storage.blob.BlobAsyncClient;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlockBlobItem;
import com.azure.storage.blob.models.ParallelTransferOptions;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import com.azure.storage.blob.sas.BlobContainerSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.blob.specialized.BlobAsyncClientBase;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.example.wayfare.R;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.ViewModel.UserViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class PostShortActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    ImageView btnBack;
    Button btnUpload;
    Uri videoUri;
    ImageView imgShort;
    View loadingLayout;
    String returnUrl;
    EditText descriptionEditText;
    TextView listingExistsText;
    RecyclerView recycleListingsPreview;
    ArrayList<TourListModel> tourListModels;
    UserViewModel userViewModel;
    String userName;
    PreviewListingsAdapter previewListingsAdapter;
    String dashPath;
    String thumbnailPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_short2);
        btnBack = findViewById(R.id.back_btn);
        imgShort = findViewById(R.id.video_thumbnail);
        btnUpload = findViewById(R.id.btnUploadBlob2);
        loadingLayout = findViewById(R.id.loading_layout);
        descriptionEditText = findViewById(R.id.description_text);

        descriptionEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Check if the Enter key is pressed
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // Update the layout params to allow the EditText to expand
                    ViewGroup.LayoutParams layoutParams = descriptionEditText.getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    descriptionEditText.setLayoutParams(layoutParams);
                }
                return false; // Return false to allow normal event handling
            }
        });



        listingExistsText = findViewById(R.id.listingExists);
        btnBack.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        tourListModels = new ArrayList<>();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userName = bundle.getString("userName");
        String videoPath= bundle.getString("videoUri");
        videoUri = Uri.parse(videoPath);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this,videoUri);
        Bitmap bitmap = retriever.getFrameAtTime();
        imgShort.setImageBitmap(bitmap);
        thumbnailPath = saveBitmapAsImage(bitmap);
        try {
            retriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new AuthService(this).getResponse("/api/v1/user/listing/" + userName, false, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {
                makeToast(message);
            }

            @Override
            public void onResponse(ResponseModel json) {
                if (json.success){
                    JsonArray listingArray = json.data.getAsJsonArray();
                    for (JsonElement je: listingArray){
                        TourListModel toAdd = new Gson().fromJson(je, TourListModel.class);
                        tourListModels.add(toAdd);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            previewListingsAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
        previewListingsAdapter = new PreviewListingsAdapter(PostShortActivity.this,tourListModels);
        recycleListingsPreview = findViewById(R.id.recycleListingsPreview);
        recycleListingsPreview.setAdapter(previewListingsAdapter);
        recycleListingsPreview.setLayoutManager(new LinearLayoutManager(PostShortActivity.this));
        convertVideoToDASHAsync(getRealPathFromURI(videoUri));

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_btn) {
            finish();
            //overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_out_bottom);
        }
        if (view.getId() == R.id.btnUploadBlob2) {
            loadingLayout.setVisibility(View.VISIBLE);
            if (dashPath != null) {
                File file = new File(dashPath);
                Uri dashUri = Uri.fromFile(file);
                File thumbnailFile = new File(thumbnailPath);
                Uri thumbnailUri = Uri.fromFile(thumbnailFile);
                AzureStorageManager.uploadBlob(this, dashUri, false, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // Handle failure
                        e.printStackTrace();
                        unsuccessfullScreen();
                        // Perform any error handling logic, such as showing an error message to the user
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // Handle success
                        if (!response.isSuccessful()) {
                            unsuccessfullScreen();
                            throw new IOException("Unexpected response code: " + response);
                        }
                        // Handle successful upload
                        String url = response.request().url().toString();
                        String urlOfVidBlob = AzureStorageManager.getBaseUrl(url); //URL of uploaded file
                        Log.d("Upload", "File uploaded successfully. URL: " + url);
                        AzureStorageManager.uploadBlob(PostShortActivity.this, thumbnailUri, false, new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                if (!response.isSuccessful()) {
                                    unsuccessfullScreen();
                                    throw new IOException("Unexpected response code: " + response);
                                }
                                String url = response.request().url().toString();
                                String urlOfPiclob = AzureStorageManager.getBaseUrl(url); //URL of uploaded file
                                Log.d("Upload", "Pic uploaded successfully. URL: " + url);
                                int selectedPosition = previewListingsAdapter.getSelectedPosition();
                                String apiUrl = "/shorts/create";
                                if (selectedPosition != -1) {
                                    String tourListingID = tourListModels.get(selectedPosition).getId();
                                    apiUrl = apiUrl + "/" + tourListingID;
                                }
                                String descriptionText = descriptionEditText.getText().toString();
                                Date currentDate = new Date();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                String formattedDate = dateFormat.format(currentDate);
                                final OkHttpClient client = new OkHttpClient();
                                // TODO Complete JSON string
                                String json = String.format("{\"description\": \"%s\", \"shortsUrl\": \"%s\", \"datePosted\": \"%s\",\"thumbnailUrl\": \"%s\"}",
                                        descriptionText, urlOfVidBlob, formattedDate,urlOfPiclob);
                                RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
                                new AuthService(PostShortActivity.this).getResponse(apiUrl, true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
                                    @Override
                                    public void onError(String message) {
                                        unsuccessfullScreen();
                                    }

                                    @Override
                                    public void onResponse(ResponseModel json) {
                                        if (!response.isSuccessful()) {
                                            unsuccessfullScreen();
                                        }
                                        successfullScreen();
                                    }
                                });
                            }
                        });
                    }
                });
             }
            }
        }
    private void successfullScreen(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Start a new activity
                loadingLayout.setVisibility(View.GONE);
                Toast.makeText(PostShortActivity.this, "Upload Successful", Toast.LENGTH_LONG).show();
                Intent i = new Intent(PostShortActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }
    private void unsuccessfullScreen(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Start a new activity
                loadingLayout.setVisibility(View.GONE);
                Toast.makeText(PostShortActivity.this, "Upload Unsuccessful", Toast.LENGTH_LONG).show();
                Intent i = new Intent(PostShortActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }

    private String getOutputFilePathForDASH() {
        // Get internal storage directory
        File storageDir = getFilesDir();
        String name = "Wayfare-Shorts-" +
                new SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                        .format(System.currentTimeMillis()) + ".mp4";
        File outputFile = new File(storageDir, name);
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
    private void convertVideoToDASHAsync(final String inputVideoPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Generate unique output path for DASH file
                final String outputFilePath = getOutputFilePathForDASH();

                // FFmpeg command building
                String[] command = new String[]{
                        "-i", inputVideoPath, // Input file
                        "-c:v", "libx264", // Specify video codec (H.264)
                        "-preset", "veryfast", // Use ultrafast preset for faster encoding
                        "-crf", "23", // Constant rate factor for quality (lower values mean better quality but larger file size)
                        "-c:a", "aac", // Audio codec
                        "-b:v", "1M",  // Set video bitrate (adjust based on your needs)
                        "-movflags", "+faststart",
                        outputFilePath // Output DASH file path
                };

                // Execute FFmpeg command
                int rc = FFmpeg.execute(command);

                // Handle the result of the conversion
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (rc == Config.RETURN_CODE_SUCCESS) {
                            // Conversion successful, do something with the output file path
                            // For example, start playback with ExoPlayer
                            dashPath = outputFilePath;
                            if(loadingLayout.getVisibility()==View.VISIBLE){
                                btnUpload.performClick();
                            }
                        } else {
                            // Conversion failed, handle the error
                            // For example, show an error message to the user
                            Toast.makeText(getApplicationContext(), "Video conversion failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }
    private String saveBitmapAsImage(Bitmap bitmap) {
        // Get internal storage directory
        File storageDir = getFilesDir();

        // Generate a unique file name for the image
        String name = "Wayfare-Shorts-" +
                new SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                        .format(System.currentTimeMillis()) + ".jpg";

        // Create a File object with the specified file name in the internal storage directory
        File outputFile = new File(storageDir, name);

        try {
            // Open a FileOutputStream for the file path
            FileOutputStream fos = new FileOutputStream(outputFile);
            // Compress the Bitmap into the FileOutputStream as a JPEG image with 100% quality
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            // Close the FileOutputStream
            fos.close();
            // Image is saved successfully, return the absolute path of the created image file
            return outputFile.getAbsolutePath();
        } catch (IOException e) {
            // Error occurred while saving the image
            e.printStackTrace();
            Log.e("Bitmap", "Error saving bitmap as image: " + e.getMessage());
            return null; // Return null to indicate failure
        }
    }
    public void makeToast(String msg) {

        if (this == null) {
            Log.d("ERROR", "ACTIVITY CONTEXT IS NULL, UNABLE TO MAKE TOAST");
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}