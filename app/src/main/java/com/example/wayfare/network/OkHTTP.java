package com.example.wayfare.network;

import static org.bson.assertions.Assertions.fail;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHTTP {

    // asynchronous get to check if username exists in database
    // call in check account
    // on response redirect to sign in
    // on error redirect to sign up

    private final OkHttpClient client = new OkHttpClient();

    public void getRequest() {
        Request request = new Request.Builder().url("http://143.198.223.202/api/v1/user/{username}").build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                fail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("TAG","it worked");
            }
        });
    }


}
