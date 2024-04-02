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
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp3Fragment extends Fragment {

    Button continue_button;
    EditText first_name, last_name, phone_number;
    TextInputLayout firstnamewrapper, lastnamewrapper, phonenumberwrapper;
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
        firstnamewrapper = view.findViewById(R.id.first_name_wrapper);
        lastnamewrapper = view.findViewById(R.id.last_name_wrapper);
        phonenumberwrapper = view.findViewById(R.id.phone_number_wrapper);
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
                if (validateFields()) {
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
            }
        });
        return view;
    }

    public boolean validateFields(){
        boolean result = true;
        Pattern pattern = Pattern.compile("^[89]\\d{7}$");
        Matcher matcher = pattern.matcher(phone_number.getText());

        if (!matcher.find()){
            result = false;
            phonenumberwrapper.setErrorEnabled(true);
            phonenumberwrapper.setError("Invalid phone number");
        }
        else
            phonenumberwrapper.setErrorEnabled(false);

        if (first_name.getText().length() == 0) {
            firstnamewrapper.setErrorEnabled(true);
            firstnamewrapper.setError("First name cannot be blank.");
            result = false;
        }
        else
            firstnamewrapper.setErrorEnabled(false);

        if (last_name.getText().length() == 0) {
            lastnamewrapper.setErrorEnabled(true);
            lastnamewrapper.setError("Last name cannot be blank.");
            result = false;
        }
        else
            lastnamewrapper.setErrorEnabled(false);

        return result;
    }
}