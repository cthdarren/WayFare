package com.example.wayfare.Utils;

import static com.example.wayfare.Utils.AuthHelper.JSON_DATA_KEY;
import static com.example.wayfare.Utils.AuthHelper.PREFS_NAME;
import static com.example.wayfare.Utils.AuthHelper.sharedPreferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.wayfare.Activity.MainActivity;
import com.example.wayfare.BuildConfig;
import com.example.wayfare.Models.ResponseModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RunAuthRequest implements Runnable{
        private final String token;
        private final String apiUrl;
        private final Helper.RequestType requestType;
        private final RequestBody body;

    public RunAuthRequest(Context context, String apiUrl, Helper.RequestType requestType, RequestBody body) {
        this.apiUrl = apiUrl;
        this.requestType = requestType;
        this.body = body;
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(JSON_DATA_KEY, "");

    }

    @Override
    public void run() {
        final OkHttpClient client = new OkHttpClient();
        Request request;
        if (requestType == Helper.RequestType.REQ_GET){
            request = new Request.Builder().url(BuildConfig.API_URL + apiUrl)
                    .addHeader("Authorization", "Bearer " + token)
                    .get()
                    .build();
        }
        else if (requestType == Helper.RequestType.REQ_POST){
            request = new Request.Builder().url(BuildConfig.API_URL + apiUrl)
                    .addHeader("Authorization", "Bearer " + token)
                    .post(body)
                    .build();
        }
        else return;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException) {
                    e.printStackTrace();
                } else if (e instanceof SocketException) {
                    Log.d("ERROR", "CHECK IF BACKEND SERVER IS RUNNING!");
                    e.printStackTrace();
                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String serverResponse = response.body().string();
                // debugging
                System.out.println(serverResponse);
                Log.i("Tag", "it worked>");
                // sharedpref store
            }
        });
    }
}

