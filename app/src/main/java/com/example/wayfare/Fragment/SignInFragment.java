package com.example.wayfare.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wayfare.Activity.MainActivity;
import com.example.wayfare.BuildConfig;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.Helper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignInFragment extends Fragment{

    Button sign_in_button;
    EditText username, password;

    BottomNavigationView navBar;
    ImageView login_exit;
    private TextView signUpBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        navBar = getActivity().findViewById(R.id.bottomNavigationView);
        navBar.setVisibility(View.INVISIBLE);
        sign_in_button = view.findViewById(R.id.sign_in_button);
        signUpBtn = view.findViewById(R.id.signUpBtn);
        username = view.findViewById(R.id.usernamelog);
        password = view.findViewById(R.id.passwordlog);
        login_exit = view.findViewById(R.id.login_exit);
        view.setClickable(true);

        login_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getParentFragmentManager().popBackStack();
            }
        });
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    login();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        
        signUpBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Helper.goToFragmentSlideInRight(getParentFragmentManager(),R.id.container, new SignUpFragment());
            }
        });
        return view;
    }

    public void login() throws IOException {
        final OkHttpClient client = new OkHttpClient();
        String json = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username.getText(), password.getText());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.Builder().url(BuildConfig.API_URL + "/api/v1/auth/login")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException) {
                    makeToast("Request Timed Out");
                    e.printStackTrace();
                } else if (e instanceof SocketException) {
                    makeToast("Server Error");
                    Log.d("ERROR", "CHECK IF BACKEND SERVER IS RUNNING!");
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String serverResponse = response.body().string();
                // debugging
                System.out.println(serverResponse);
                // sharedpref store
                try {
                    Gson gson = new Gson();
                    ResponseModel res = gson.fromJson(serverResponse, ResponseModel.class);
                    if (res.success) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user_info", res.data.getAsString())
                                .apply();

                        makeToast("Welcome back");
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        makeToast(res.data.getAsString());
                    }

                } catch (Exception e) {
                    makeToast("Unexpected Error");
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    public void makeToast(String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navBar.setVisibility(View.VISIBLE);
    }
}