package com.example.wayfare.Utils;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.wayfare.Activity.MainActivity;
import com.example.wayfare.Activity.PostShortActivity;
import com.example.wayfare.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import id.zelory.compressor.Compressor;
import okhttp3.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AzureStorageManager {

    public static void uploadBlob(Context context, Uri fileUri,Callback callback){
        String containerName = "test";
        OkHttpClient tokenClient = new OkHttpClient();

// Build the request to get SAS token
        Request request = new Request.Builder()
                .url(BuildConfig.API_URL + "/api/v1/azure/sas")
                .get()
                .build();
        tokenClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle success
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected response code: " + response);
                }
                // Get the JSON response body
                String responseBody = response.body().string();
                // Use Gson to parse the JSON response
                Gson gson = new Gson();
                com.example.wayfare.Models.ResponseModel responseModel = gson.fromJson(responseBody, com.example.wayfare.Models.ResponseModel.class);
                if (responseModel != null && responseModel.success) {
                    JsonElement azureInfo = responseModel.data;
                    if (azureInfo != null) {
                        JsonObject azureInfoObject = azureInfo.getAsJsonObject();
                        String sasToken = azureInfoObject.get("sasToken").getAsString();
                        String accountName = azureInfoObject.get("accountName").getAsString();
                        uploadFile(fileUri, context,sasToken, accountName, containerName, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                // Handle failure
                                e.printStackTrace();
                                callback.onFailure(call, e);
                                // Perform any error handling logic, such as showing an error message to the user
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                // Handle success
                                if (!response.isSuccessful()) {
                                    throw new IOException("Unexpected response code: " + response);
                                }
                                // Handle successful upload
                                String url = response.request().url().toString();
                                // Do something with the uploaded file URL, such as displaying it to the user
                                Log.d("Upload", "File uploaded successfully. URL: " + url);
                                callback.onResponse(call, response);
                            }
                        });
                    } else {
                        System.out.println("AzureInfo is null in the response.");
                    }
                } else {
                    System.out.println("API response indicates failure.");
                }
            }
        });


    }
    private static void uploadFile(Uri fileUri,Context context,String sasToken,String accountName,String containerName,Callback callback){
        String fileName = getFileNameFromUri(fileUri,context);
        String extension = getFileExtension(fileName);
        String mediaTypeString;
        switch (extension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                mediaTypeString = "image/jpeg";
                break;
            case "png":
                mediaTypeString = "image/png";
                break;
            case "gif":
                mediaTypeString = "image/gif";
                break;
            case "mp4":
                mediaTypeString = "video/mp4";
                break;
            // Add more cases for other file types as needed
            default:
                mediaTypeString = "application/octet-stream";
                break;
        }
        OkHttpClient client = new OkHttpClient();
        String url = String.format("https://%s.blob.core.windows.net/%s/%s", accountName, containerName, fileName);
        final String toReturnUrl = url;
        if (sasToken != null && !sasToken.isEmpty()) {
            url += "?" + sasToken;
        }
        // Convert video content to byte array
        byte[] videoBytes;
        try {
            videoBytes = getBytesFromUri(fileUri,context);
        } catch (IOException e) {
            throw new RuntimeException("Error reading video content: " + e.getMessage(), e);
        }
        // Open an InputStream from the Uri
        RequestBody requestBody = RequestBody.create(MediaType.parse(mediaTypeString), videoBytes);
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








    private static byte[] getBytesFromUri(Uri uri, Context context) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri)) {
            Bitmap bmp = BitmapFactory.decodeStream(inputStream);
            bmp.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
        }
        return outputStream.toByteArray();
    }
    private static String getFileNameFromUri(Uri uri,Context context) {
        String[] projection = {MediaStore.Video.Media.DISPLAY_NAME};
        try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                return cursor.getString(columnIndex);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }
    public static String getBaseUrl(String url) {
        int index = url.indexOf("?");
        if (index != -1) {
            return url.substring(0, index);
        } else {
            return url; // If there is no question mark, return the original URL
        }
    }

}
