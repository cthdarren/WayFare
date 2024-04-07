package com.example.wayfare.Utils;

import static com.example.wayfare.Utils.AuthHelper.JSON_DATA_KEY;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.RequestBuilder;
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

public class AuthService {
        private final String token;
        private final String baseUrl;
        private final Context context;
        private final SharedPreferences sharedPreferences;

    public AuthService(Context context) {
        this.context = context;
        this.baseUrl = BuildConfig.API_URL;
        sharedPreferences = new AuthHelper(context).sharedPreferences;
        token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJxdWVlcmxvcmQxMjMiLCJpYXQiOjE3MTE3MDEwMDEsImV4cCI6MTcxNjg4NTAwMX0.zfhWhgWyyzOpgmULknDX6TqdKuUVH0-carQfo7IOUFupCIYkbiP110cAcQCIP__p";
    }

    public interface ResponseListener {
        void onError(String message);
        void onResponse(ResponseModel json);
    }

    public void getResponse(String apiUrl, boolean authenticated, Helper.RequestType requestType, RequestBody body, ResponseListener responseListener) {
        final OkHttpClient client = new OkHttpClient();
        Request.Builder requestBuilder;
        Request request;
        if (requestType == Helper.RequestType.REQ_GET){
            requestBuilder = new Request.Builder().url(BuildConfig.API_URL + apiUrl)
                    .get();
        }
        else if (requestType == Helper.RequestType.REQ_POST){
            requestBuilder = new Request.Builder().url(BuildConfig.API_URL + apiUrl)
                    .post(body);
        }
        else return;
        if (authenticated)
            request = requestBuilder.addHeader("Authorization", "Bearer " + token).build();
        else
            request = requestBuilder.build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException) {
                    e.printStackTrace();
                    responseListener.onError("Request Timed Out");
                } else if (e instanceof SocketException) {
                    Log.d("ERROR", "CHECK IF BACKEND SERVER IS RUNNING!");
                    e.printStackTrace();
                    responseListener.onError("Server Error");
                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String serverResponse = response.body().string();

                System.out.println(serverResponse);
                if (response.networkResponse().code() == 200) {
                    ResponseModel parsedResponse = new Gson().fromJson(serverResponse, ResponseModel.class);
                    Log.i("Tag", "it worked>");
                    responseListener.onResponse(parsedResponse);
                }
                else{
                    responseListener.onError("Server Error");
                }
                // sharedpref store
            }
        });
    }
}

