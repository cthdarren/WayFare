package com.example.wayfare.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.wayfare.R;
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
                Helper.goToFullScreenFragmentFromBottom(getParentFragmentManager(), new VerifySuccessFragment());
            }
        });
        return view;
    }
}