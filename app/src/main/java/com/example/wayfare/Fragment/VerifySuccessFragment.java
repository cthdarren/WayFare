package com.example.wayfare.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wayfare.Models.UserModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.ViewModel.UserViewModel;

public class VerifySuccessFragment extends Fragment {

    Button sendAnother;
    TextView email;
    UserViewModel userViewModel;
    public VerifySuccessFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_success, container, false);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        UserModel userData = userViewModel.getUserProfileData();
        email = view.findViewById(R.id.email_address);
        sendAnother = view.findViewById(R.id.sendemailbutton);

        email.setText(userData.getEmail());
        sendAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
}