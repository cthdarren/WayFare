package com.example.wayfare.Activity;
import android.content.ContentResolver;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_btn) {
            finish();
            //overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_out_bottom);
        }
        if (view.getId() == R.id.btnUploadBlob2){
            loadingLayout.setVisibility(View.VISIBLE);
            AzureStorageManager.uploadBlob(this, videoUri, new Callback() {
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
                    String urlOfBlob = AzureStorageManager.getBaseUrl(url); //URL of uploaded file
                    Log.d("Upload", "File uploaded successfully. URL: " + url);
                    int selectedPosition = previewListingsAdapter.getSelectedPosition();
                    String apiUrl = "/shorts/create";
                    if (selectedPosition!=-1) {
                        String tourListingID = tourListModels.get(selectedPosition).getId();
                        apiUrl = apiUrl+"/"+tourListingID;
                    }
                    String descriptionText = descriptionEditText.getText().toString();
                    Date currentDate = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    String formattedDate = dateFormat.format(currentDate);
                    final OkHttpClient client = new OkHttpClient();
                    // TODO Complete JSON string
                    String json = String.format("{\"description\": \"%s\", \"shortsUrl\": \"%s\", \"datePosted\": \"%s\"}",
                            descriptionText,urlOfBlob,formattedDate);
                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
                    new AuthService(PostShortActivity.this).getResponse(apiUrl, true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
                        @Override
                        public void onError(String message) {
                            unsuccessfullScreen();
                        }
                        @Override
                        public void onResponse(ResponseModel json){
                            if (!response.isSuccessful()) {
                                unsuccessfullScreen();
                            }
                            successfullScreen();
                        }
                    });

                }
            });

        }
    }
    public String createServiceSASContainer(BlobContainerClient containerClient) {
        // Create a SAS token that's valid for 1 day, as an example
        OffsetDateTime expiryTime = OffsetDateTime.now().plusDays(1);

        // Assign read permissions to the SAS token
        BlobContainerSasPermission sasPermission = new BlobContainerSasPermission()
                .setReadPermission(true)
                .setWritePermission(true)  // Add write permission
                .setAddPermission(true);   // Add upload permission

        BlobServiceSasSignatureValues sasSignatureValues = new BlobServiceSasSignatureValues(expiryTime, sasPermission)
                .setStartTime(OffsetDateTime.now().minusMinutes(5));

        String sasToken = containerClient.generateSas(sasSignatureValues);
        return sasToken;
    }
    private String getFileNameFromUri(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DISPLAY_NAME};
        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                return cursor.getString(columnIndex);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
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

    public void uploadFile(Uri videoUri,String sasToken,String accountName,String containerName,Callback callback){
        String fileName = getFileNameFromUri(videoUri);
        OkHttpClient client = new OkHttpClient();
        String url = String.format("https://%s.blob.core.windows.net/%s/%s", accountName, containerName, fileName);
        final String toReturnUrl = url;
        if (sasToken != null && !sasToken.isEmpty()) {
            url += "?" + sasToken;
        }
        // Convert video content to byte array
        byte[] videoBytes;
        try {
            videoBytes = getBytesFromUri(videoUri);
        } catch (IOException e) {
            throw new RuntimeException("Error reading video content: " + e.getMessage(), e);
        }
        // Open an InputStream from the Uri
        RequestBody requestBody = RequestBody.create(MediaType.parse("video/mp4"), videoBytes);
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .addHeader("x-ms-blob-type", "BlockBlob")
                .build();
        // Execute the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
                e.printStackTrace();
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle success
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected response code: " + response);
                }
                // Handle successful upload
                System.out.println("File uploaded successfully.");// This is the URL of the uploaded file
                callback.onResponse(call, response);
            }
        });


    }
    private byte[] getBytesFromUri(Uri uri) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return outputStream.toByteArray();
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