package com.example.wayfare.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.wayfare.R;
import com.example.wayfare.Utils.Helper;

public class VerifySuccessFragment extends Fragment {

    Button sendAnother;
    public VerifySuccessFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_success, container, false);
        sendAnother = view.findViewById(R.id.sendemailbutton);
        sendAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
}