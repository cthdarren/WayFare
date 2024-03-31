package com.example.wayfare.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.wayfare.R;
import com.example.wayfare.Utils.Helper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SignUp4Fragment extends Fragment {

    Button continue_button;
    EditText languages, bio;
    BottomNavigationView navBar;
    ImageView register_back;

    public SignUp4Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up4, container, false);
        navBar = getActivity().findViewById(R.id.bottomNavigationView);
        navBar.setVisibility(View.INVISIBLE);
        languages = view.findViewById(R.id.languages_spoken);
        bio = view.findViewById(R.id.bio);
        register_back = view.findViewById(R.id.register_exit);
        continue_button = view.findViewById(R.id.continue_button);

        register_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.goToFragmentSlideInRight(getParentFragmentManager(), R.id.container, new SignUp4Fragment());
            }
        });
        return view;
    }
}