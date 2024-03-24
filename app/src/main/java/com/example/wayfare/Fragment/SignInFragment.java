package com.example.wayfare.Fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wayfare.BuildConfig;
import com.example.wayfare.R;

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

public class SignInFragment extends Fragment {

    Button sign_in_button;
    EditText username, password;

    public SignInFragment() {
    }

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

//
//
//        Request request = new Request.Builder().url(BuildConfig.API_URL + "/api/v1/auth/login").post(body).build();
//
//        Response response = client.newCall(request).execute();

        new Thread(new Runnable() {
            @Override
            public void run() {

                String json = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username.getText(), password.getText());
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
                Request request = new Request.Builder().url(BuildConfig.API_URL + "/api/v1/auth/login")
                        .post(body)
                        .addHeader("Connection","keep-alive")
                        .build();

                OkHttpClient client = new OkHttpClient();
                client.connectTimeoutMillis();
                client.readTimeoutMillis();
                client.callTimeoutMillis();
                Call call = client.newCall(request);

                Response response = null;

                try {
                    response = call.execute();
                    String serverResponse = response.body().string();

                    getActivity().runOnUiThread((new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(serverResponse);
                            Log.i("Tag", "it worked>");

                        }
                    }));

                }
                catch(SocketTimeoutException e){
//                    Toast.makeText(getContext(), "Request Timed Out", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }
                catch (SocketException e){
//                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                    Log.d("ERROR", "CHECK IF BACKEND SERVER IS RUNNING!");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
       /* FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft
                .replace(, new ExploreFragment())
                .setReorderingAllowed(true)
                .addToBackStack("name") // Name can be null
                .commit();*/
    }

}