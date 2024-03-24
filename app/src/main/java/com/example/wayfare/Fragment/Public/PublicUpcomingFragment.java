package com.example.wayfare.Fragment.Public;

import static com.example.wayfare.Utils.Helper.goToLogin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wayfare.Fragment.SignInFragment;
import com.example.wayfare.R;

public class PublicUpcomingFragment extends Fragment {

    Button toursLoginBtn;
    public PublicUpcomingFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.public_fragment_upcoming, container, false);
        toursLoginBtn = view.findViewById(R.id.toursLoginBtn);
        toursLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin(getParentFragmentManager());
            }
        });
        return view;
    }
}