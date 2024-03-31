package com.example.wayfare.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.wayfare.BuildConfig;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUp2Fragment extends Fragment {

    Button continue_button;
    EditText password, verifyPassword;
    TextView helperTextHeader;
    BottomNavigationView navBar;
    ImageView register_back;
    String username;
    String email;
    boolean validpassword = false;
    public SignUp2Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        username = getArguments().getString("username");
        email = getArguments().getString("email");
        View view = inflater.inflate(R.layout.fragment_sign_up2, container, false);
        navBar = getActivity().findViewById(R.id.bottomNavigationView);
        navBar.setVisibility(View.INVISIBLE);
        password = view.findViewById(R.id.password);
        verifyPassword = view.findViewById(R.id.verify_password);
        register_back = view.findViewById(R.id.register_exit);
        continue_button = view.findViewById(R.id.continue_button);
        helperTextHeader = view.findViewById(R.id.helperTextHeader);
        continue_button.setEnabled(false);

        register_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\d\\s:])([^\\s]){8,16}$");
                Matcher matcher = pattern.matcher(String.valueOf(s));
                if (matcher.find()){
                    validpassword=true;
                    if (Objects.equals(verifyPassword.getText().toString(), s.toString())){
                        continue_button.setEnabled(true);
                    }
                    else{
                        continue_button.setEnabled(false);
                    }
                }
                else{
                    validpassword=false;
                    continue_button.setEnabled(false);
                }
            }
        });
        verifyPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Objects.equals(password.getText().toString(), s.toString()) & validpassword){
                    continue_button.setEnabled(true);
                }
                else{
                    continue_button.setEnabled(false);
                }
            }
        });
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("username", username);
                args.putString("email", email);
                args.putString("password", String.valueOf(password.getText()));
                args.putString("verifypassword", String.valueOf(verifyPassword.getText()));
                SignUp3Fragment fragment = new SignUp3Fragment();
                fragment.setArguments(args);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
            }
        });
        return view;
    }
}