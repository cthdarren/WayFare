package com.example.wayfare;

import static org.bson.assertions.Assertions.fail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class check_account extends AppCompatActivity {

    Button continue_button;
    EditText usercheck;
    final OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_account);

        continue_button = findViewById(R.id.continue_button);
        usercheck = findViewById(R.id.usercheck);

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticate();
            }
        });

    }

    public void authenticate(){

            Request request = new Request.Builder().url("http://143.198.223.202/api/v1/user/test").build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("TAG", "It didnt work");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i("TAG","it worked");
                }
            });
        }


}