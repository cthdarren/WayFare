package com.example.wayfare.Fragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wayfare.BuildConfig;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

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

    Button continue_button;
    EditText username, email;
    TextInputLayout usernameLayout, emailLayout;
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
        username = view.findViewById(R.id.username);
        usernameLayout = view.findViewById(R.id.username_layout);
        email = view.findViewById(R.id.emailAddress );
        emailLayout= view.findViewById(R.id.email_layout);
        register_exit = view.findViewById(R.id.register_exit);
        continue_button = view.findViewById(R.id.continue_button);
        register_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkUserNameAndEmail();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    private void checkUserNameAndEmail() {
        String json = String.format("{\"username\":\"%s\", \"email\":\"%s\"}", username.getText(), email.getText());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        new AuthService(getContext()).getResponse("/api/v1/auth/checknewuseremail", false, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {
                makeToast(message);
            }

            @Override
            public void onResponse(ResponseModel json) {
                if (json.success){
                    Bundle args = new Bundle();
                    args.putString("username", String.valueOf(username.getText()));
                    args.putString("email", String.valueOf(email.getText()));
                    SignUp2Fragment fragment = new SignUp2Fragment();
                    fragment.setArguments(args);
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment)
                            .addToBackStack(null)
                            .setReorderingAllowed(true)
                            .commit();
                }
                else{
                    JsonArray ja= json.data.getAsJsonArray();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            usernameLayout.setHelperText(ja.get(0).getAsString());
                            usernameLayout.setHelperTextColor(ColorStateList.valueOf(getContext().getResources().getColor(R.color.red)));
                            emailLayout.setHelperText(ja.get(1).getAsString());
                            emailLayout.setHelperTextColor(ColorStateList.valueOf(getContext().getResources().getColor(R.color.red)));
                        }
                    });
                }
            }
        });
    }


    public void register() throws IOException {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final OkHttpClient client = new OkHttpClient();
                    makeToast("bruh");
                    String json = String.format("{\"username\":\"%s\", \"email\":\"%s\"}", username.getText(), email.getText());
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