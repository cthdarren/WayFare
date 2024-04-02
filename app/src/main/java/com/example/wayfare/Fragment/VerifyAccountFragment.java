package com.example.wayfare.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.graphics.ImageDecoderKt;
import androidx.fragment.app.Fragment;

import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;

public class VerifyAccountFragment extends Fragment {

    Button verifyEmailButton;
    public VerifyAccountFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_account, container, false);
        verifyEmailButton = view.findViewById(R.id.verifyemailbutton);
        verifyEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyEmailButton.setEnabled(false);
                new AuthService(getContext()).getResponse("/generateverifylink", true, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
                    @Override
                    public void onError(String message) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                verifyEmailButton.setEnabled(true);
                                Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(ResponseModel json) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                verifyEmailButton.setEnabled(true);
                                Helper.goToFragmentSlideInRight(getActivity().getSupportFragmentManager(), R.id.container, new VerifySuccessFragment());
                            }
                        });
                    }
                });
            }
        });
        return view;
    }
}