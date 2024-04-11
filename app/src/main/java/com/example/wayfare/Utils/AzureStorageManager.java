package com.example.wayfare.Utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.wayfare.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import id.zelory.compressor.Compressor;
import okhttp3.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AzureStorageManager {

    public static void uploadBlob(Context context, Uri fileUri, boolean profilePicBoolean, Callback callback) {
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
                        uploadFile(fileUri, context, sasToken, accountName, containerName, profilePicBoolean, new Callback() {
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
                                String url = response.request().url().toString().split("\\?")[0];
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
    private static void uploadFile(Uri fileUri,Context context,String sasToken,String accountName,String containerName,boolean profilePicBoolean, Callback callback){
        String fileName = getFileNameFromUri(fileUri,context);
        if (profilePicBoolean)
            fileName = "profilePic" + fileName;
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
        RequestBody requestBody;
        // if it's an image, the request body would be a multipart form
        if (mediaTypeString.startsWith("image")) {
            File tempFile;
            try {
                tempFile = getImageFileFromUri(fileUri, context, profilePicBoolean);
                requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), tempFile);
            } catch (IOException e) {
                throw new RuntimeException("Error reading image data: " + e.getMessage(), e);
            }
        } else { // otherwise it is a bytearray for anything else
            byte[] fileBytes;
            try {
                fileBytes = getBytesFromUri(fileUri, context, mediaTypeString);
                requestBody = RequestBody.create(MediaType.parse(mediaTypeString), fileBytes);
            } catch (IOException e) {
                throw new RuntimeException("Error reading video content: " + e.getMessage(), e);
            }
        }
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

    private static byte[] getBytesFromUri(Uri uri, Context context, String mediaType) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return outputStream.toByteArray();
    }

    private static File getImageFileFromUri(Uri uri, Context c, boolean profilePic) throws IOException {
        int requiredSize;
        if (profilePic)
            // max size 400x400
            requiredSize = 200;
        else
            requiredSize = 500;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        // smallest side will be < 1k pixels
        while (width_tmp / 2 >= requiredSize && height_tmp / 2 >= requiredSize) {
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bmp = BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
        if (profilePic) {
            Bitmap croppedBmp;
            if (bmp.getWidth() >= bmp.getHeight()) {
                croppedBmp = Bitmap.createBitmap(bmp, bmp.getWidth() / 2 - bmp.getHeight() / 2, 0, bmp.getHeight(), bmp.getHeight());
            } else
                croppedBmp = Bitmap.createBitmap(bmp, 0, bmp.getHeight() / 2 - bmp.getWidth() / 2, bmp.getWidth(), bmp.getWidth());
            bmp = croppedBmp;
        }
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
        byte[] refactoredByteArray = outputStream.toByteArray();
        File tempfile = File.createTempFile("temp", ".jpg");
        FileOutputStream fos = new FileOutputStream(tempfile);
        fos.write(refactoredByteArray);
        fos.close();
        return tempfile;
    }
    private static String getFileNameFromUri(Uri uri, Context context) {
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
