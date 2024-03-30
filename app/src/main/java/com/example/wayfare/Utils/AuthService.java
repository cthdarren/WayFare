package com.example.wayfare.Utils;

import static com.example.wayfare.Utils.AuthHelper.JSON_DATA_KEY;
import static com.example.wayfare.Utils.AuthHelper.PREFS_NAME;
import static com.example.wayfare.Utils.AuthHelper.sharedPreferences;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

    public AuthService(Context context) {
        this.context = context;
        this.baseUrl = BuildConfig.API_URL;
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(JSON_DATA_KEY, "");

    }

    public interface ResponseListener {
        void onError(String message);
        void onResponse(ResponseModel json);
    }

    public void getResponse(String apiUrl, Helper.RequestType requestType, RequestBody body, ResponseListener responseListener) {
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

