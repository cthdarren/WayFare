package com.example.wayfare.Fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
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
import android.widget.Toast;

import com.example.wayfare.Activity.MainActivity;
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
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    public void login() throws IOException {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final OkHttpClient client = new OkHttpClient();
                    makeToast("bruh");
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
                            System.out.println(serverResponse);
                            Log.i("Tag", "it worked>");
                        }
                    });
                }
       /* FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft
                .replace(, new ExploreFragment())
                .setReorderingAllowed(true)
                .addToBackStack("name") // Name can be null
                .commit();*/
            });
        }
    }
    public void makeToast(String msg) {

        if (getActivity() == null) {
            Log.d("ERROR", "ACTIVITY CONTEXT IS NULL, UNABLE TO MAKE TOAST");
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}