package com.example.wayfare.Activity;

import static org.bson.assertions.Assertions.fail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wayfare.R;
import com.example.wayfare.Fragment.SignInFragment;
import com.example.wayfare.Fragment.SignUpFragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class check_account extends AppCompatActivity {

    // asynchronous get to check if username exists in database

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

            Request request = new Request.Builder().url("http://143.198.223.202/api/v1/user" + String.format("/%s",usercheck)).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // on error redirect to sign up
                    Intent goToSignUp = new Intent(check_account.this, SignUpFragment.class);
                    e.printStackTrace();
                    Log.i("TAG", "It didn't work");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // on response redirect to sign in
                    Intent goToSignIn = new Intent(check_account.this, SignInFragment.class);
                    Log.i("TAG",response.body().string());
                }
            });
        }


}