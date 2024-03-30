package com.example.wayfare.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wayfare.BuildConfig;
import com.example.wayfare.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpFragment extends Fragment {

    Button sign_up_button;
    EditText username, firstName, lastName, email, phoneNumber, password, verifyPassword;
    BottomNavigationView navBar;
    ImageView register_exit;

    public SignUpFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        navBar = getActivity().findViewById(R.id.bottomNavigationView);
        navBar.setVisibility(View.INVISIBLE);
        sign_up_button = view.findViewById(R.id.sign_up_button);
        username = view.findViewById(R.id.username);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.email);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        password = view.findViewById(R.id.password);
        verifyPassword = view.findViewById(R.id.verifyPassword);
        register_exit = view.findViewById(R.id.register_exit);

        register_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getParentFragmentManager().popBackStack();
            }
        });
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    register();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }


    public void register() throws IOException {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final OkHttpClient client = new OkHttpClient();
                    makeToast("bruh");
                    String json = String.format("{\"username\":\"%s\", \"firstName\":\"%s\", \"lastName\":\"%s\", \"email\":\"%s\", \"phoneNumber\":\"%s\", \"password\":\"%s\", \"verifyPassword\":\"%s\"}", username.getText(), firstName.getText(), lastName.getText(), email.getText(), phoneNumber.getText(), password.getText(), verifyPassword.getText());
                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
                    Request request = new Request.Builder().url(BuildConfig.API_URL + "/api/v1/auth/register")
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

    @Override
    public void onDestroy() {
        navBar = getActivity().findViewById(R.id.bottomNavigationView);
        navBar.setVisibility(View.VISIBLE);
        super.onDestroy();
    }
}