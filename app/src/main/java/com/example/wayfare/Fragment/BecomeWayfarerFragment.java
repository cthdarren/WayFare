package com.example.wayfare.Fragment;

import static android.view.View.GONE;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthHelper;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

public class BecomeWayfarerFragment extends Fragment {

    TextInputEditText passwordField;
    Button submitpassword;
    BottomNavigationView navBar;
    ProgressBar progBar;
    ImageView backButton;

    public BecomeWayfarerFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navBar = getActivity().findViewById(R.id.bottomNavigationView);
        progBar = getActivity().findViewById(R.id.progressBar);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_become_wayfarer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        passwordField = view.findViewById(R.id.passwordField);
        submitpassword = view.findViewById(R.id.submitpassword);
        backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        submitpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new AuthService(getContext()).getResponse("/wayfarersignup", Helper.RequestType.REQ_POST, );
            }
        });
    }
}