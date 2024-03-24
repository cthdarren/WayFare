package com.example.wayfare.Fragment.Public;

import static com.example.wayfare.Utils.Helper.goToLogin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wayfare.Fragment.SignInFragment;
import com.example.wayfare.R;

public class PublicSettingsFragment extends Fragment {

    Button loginBtn;
    TextView signUpBtn;

    public PublicSettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.public_fragment_settings, container, false);
        loginBtn = view.findViewById(R.id.loginBtn);
        signUpBtn = view.findViewById(R.id.signUpBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin(getParentFragmentManager());
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin(getParentFragmentManager());
            }
        });

        return view;
    }




}