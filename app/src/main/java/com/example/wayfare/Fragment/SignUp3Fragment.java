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

public class SignUp3Fragment extends Fragment {

    Button continue_button;
    EditText first_name, last_name, phone_number;
    BottomNavigationView navBar;
    ImageView register_back;

    public SignUp3Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up3, container, false);
        navBar = getActivity().findViewById(R.id.bottomNavigationView);
        navBar.setVisibility(View.INVISIBLE);
        first_name = view.findViewById(R.id.first_name);
        last_name = view.findViewById(R.id.last_name);
        phone_number = view.findViewById(R.id.phone_number);
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
                Bundle args = new Bundle();
                args.putAll(getArguments());
                args.putString("firstname", String.valueOf(first_name.getText()));
                args.putString("lastname", String.valueOf(last_name.getText()));
                args.putString("phonenumber", String.valueOf(phone_number.getText()));
                SignUp4Fragment fragment = new SignUp4Fragment();
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