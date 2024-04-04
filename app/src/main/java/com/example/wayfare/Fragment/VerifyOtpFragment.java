package com.example.wayfare.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wayfare.Activity.MainActivity;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.UserModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.ViewModel.UserViewModel;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class VerifyOtpFragment extends Fragment {

    Button verifyOtpButton;
    TextView sendAnother;
    TextView email;
    TextInputEditText otpText;
    UserViewModel userViewModel;
    String emailText = "";
    public VerifyOtpFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_success, container, false);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        UserModel userData = userViewModel.getUserProfileData();

        email = view.findViewById(R.id.email_address);
        sendAnother = view.findViewById(R.id.sendemailbutton);
        verifyOtpButton = view.findViewById(R.id.verifyotpbutton);
        otpText = view.findViewById(R.id.otptextbox);

        //TODO FIX THIS
        if (getArguments() != null)
            emailText = getArguments().getString("email");
        else if (userData != null)
            emailText = userData.getEmail();
        else
            emailText = "Your email address";

        email.setText(emailText);
        sendAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAnother.setEnabled(false);
                new AuthService(getContext()).getResponse("/generateotp", true, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
                    @Override
                    public void onError(String message) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sendAnother.setEnabled(true);
                                makeToast(message);
                            }
                        });
                    }

                    @Override
                    public void onResponse(ResponseModel json) {
                        makeToast("Another OTP has been sent to your email.");
                        try {
                            Thread.sleep(30000);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sendAnother.setEnabled(true);
                                }
                            });
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });

        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = String.format("""
                        {"otp" : "%s"}
                        """,otpText.getText().toString());
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
                new AuthService(getContext()).getResponse("/verifyotp", true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
                    @Override
                    public void onError(String message) {
                        makeToast(message);
                    }

                    @Override
                    public void onResponse(ResponseModel json) {
                        makeToast("Account verified");
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
            }
        });

        otpText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyOtpButton.setEnabled(otpText.length() == 6);
            }
        });
        return view;
    }

    public void makeToast(String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}