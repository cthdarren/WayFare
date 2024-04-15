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
import java.time.Instant;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
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
        token = sharedPreferences.getString(JSON_DATA_KEY, "");
    }

    public interface ResponseListener {
        void onError(String message);
        void onResponse(ResponseModel json);
    }


    public void getListingResponseParams(Double longitude, Double latitude, Integer kmdistance, Integer numPax, Long startDate, Long endDate, ResponseListener responseListener) {
        final OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BuildConfig.API_URL + "/api/v1/listing/search").newBuilder();
        if (longitude != null & latitude != null & kmdistance != null) {
            urlBuilder.addQueryParameter("longitude", String.valueOf(longitude));
            urlBuilder.addQueryParameter("latitude", String.valueOf(latitude));
            urlBuilder.addQueryParameter("kmdistance", String.valueOf(kmdistance));
        }
        if (numPax != null){
            urlBuilder.addQueryParameter("numberPax", String.valueOf(numPax));
        }
        if (startDate != null & endDate != null){
            urlBuilder.addQueryParameter("startDate", String.valueOf(Instant.ofEpochMilli(startDate)));
            urlBuilder.addQueryParameter("endDate", String.valueOf(Instant.ofEpochMilli(endDate)));
        }
        String listingUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(listingUrl)
                .build();
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
                    responseListener.onResponse(parsedResponse);
                }
                else{
                    responseListener.onError("Server Error");
                }
            }
        });
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
            }
        });
    }
}

