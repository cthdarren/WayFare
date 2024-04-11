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
import com.google.gson.JsonNull;

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
                    continue_button.setEnabled(false);
                    username.setEnabled(false);
                    email.setEnabled(false);
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
         RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        new AuthService(getContext()).getResponse("/api/v1/auth/checknewuseremail", false, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {
                makeToast(message);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        continue_button.setEnabled(true);
                        username.setEnabled(true);
                        email.setEnabled(true);
                    }
                });
            }

            @Override
            public void onResponse(ResponseModel json) {
                if (json.success){
                    Bundle args = new Bundle();
                    args.putString("username", String.valueOf(username.getText()));
                    args.putString("email", String.valueOf(email.getText()));
                    SignUp2Fragment fragment = new SignUp2Fragment();
                    fragment.setArguments(args);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            continue_button.setEnabled(true);
                            username.setEnabled(true);
                            email.setEnabled(true);
                        }
                    });
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
                            continue_button.setEnabled(true);
                            username.setEnabled(true);
                            email.setEnabled(true);

                            if (!(ja.get(0) instanceof JsonNull)) {
                                usernameLayout.setErrorEnabled(true);
                                usernameLayout.setError(ja.get(0).getAsString());
                            }
                            else
                                usernameLayout.setErrorEnabled(false);

                            if (!(ja.get(1) instanceof JsonNull)){
                                emailLayout.setErrorEnabled(true);
                                emailLayout.setError(ja.get(1).getAsString());
                            }
                            else
                                emailLayout.setErrorEnabled(false);
                        }
                    });
                }
            }
        });
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