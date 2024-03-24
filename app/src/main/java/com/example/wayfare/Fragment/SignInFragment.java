package com.example.wayfare;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.media3.ui.BuildConfig;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignInFragment extends Fragment {

    Button sign_in_button;
    EditText username,password;
    final OkHttpClient client = new OkHttpClient();

    public SignInFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        sign_in_button = view.findViewById(R.id.sign_in_button);
        username = view.findViewById(R.id.usernamelog);
        password = view.findViewById(R.id.passwordlog);

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    login();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return view;
    }

    public void login() throws IOException {

        String json = String.format("{\"username\":%s, \"password\": %s}", username.toString(), password.toString());

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);

        Request request = new Request.Builder().url(BuildConfig.API_URL + "/api/v1/auth/login").post(body).build();

        Call call = client.newCall(request);
        Response response = call.execute();

        System.out.println(body);
    }

}