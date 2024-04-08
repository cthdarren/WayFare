package com.example.wayfare.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.graphics.ImageDecoderKt;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.UserModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.ViewModel.UserViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AccountSettingsFragment extends Fragment {

    UserViewModel userViewModel;
    TextInputEditText firstName, lastName, email, phoneNumber;
    TextInputLayout firstNameWrapper, lastNameWrapper, emailWrapper, phoneNumberWrapper;

    public AccountSettingsFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        UserModel userDetails = userViewModel.getUserProfileData();

         firstName = view.findViewById(R.id.first_name);
         lastName = view.findViewById(R.id.last_name);
         email = view.findViewById(R.id.email_address);
         phoneNumber = view.findViewById(R.id.phone_number);
         firstNameWrapper = view.findViewById(R.id.first_name_wrapper);
         lastNameWrapper = view.findViewById(R.id.last_name_wrapper);
         emailWrapper = view.findViewById(R.id.email_address_wrapper);
         phoneNumberWrapper = view.findViewById(R.id.phone_number_wrapper);


        firstName.setText(userDetails.getFirstName());
        lastName.setText(userDetails.getLastName());
        email.setText(userDetails.getEmail());
        phoneNumber.setText(userDetails.getPhoneNumber());

        view.findViewById(R.id.edit_profile_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        view.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    String json = String.format("""
                            {"email":"%s", "firstName":"%s", "lastName":"%s", "phoneNumber":"%s"}""",email.getText().toString(), firstName.getText().toString(), lastName.getText().toString(), phoneNumber.getText().toString());
                    RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
                    new AuthService(getContext()).getResponse("/account/edit", true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
                        @Override
                        public void onError(String message) {
                            makeToast(message);
                        }

                        @Override
                        public void onResponse(ResponseModel json) {
                            if (json.success){
                                makeToast("Successfully updated account details");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        userViewModel.updateUserData(userDetails);
                                    }
                                });
                                getParentFragmentManager().popBackStack();
                            }
                            else{
                                makeToast(json.data.getAsString());
                            }

                        }
                    });
                }
            }
        });
        return view;
    }
    public boolean validateFields(){
        boolean result = true;
        Pattern pattern = Pattern.compile("^[89]\\d{7}$");

        Matcher matcher = pattern.matcher(phoneNumber.getText().toString());

        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        Matcher emailMatcher = emailPattern.matcher(email.getText().toString());
        if (!matcher.find()){
            result = false;
            phoneNumberWrapper.setErrorEnabled(true);
            phoneNumberWrapper.setError("Invalid phone number");
        }
        else
            phoneNumberWrapper.setErrorEnabled(false);

        if (!emailMatcher.find()){
            result = false;
            emailWrapper.setErrorEnabled(true);
            emailWrapper.setError("Invalid phone number");
        }
        else
            emailWrapper.setErrorEnabled(false);

        if (firstName.getText().length() == 0) {
            firstNameWrapper.setErrorEnabled(true);
            firstNameWrapper.setError("First name cannot be blank.");
            result = false;
        }
        else
            firstNameWrapper.setErrorEnabled(false);

        if (lastName.getText().length() == 0) {
            lastNameWrapper.setErrorEnabled(true);
            lastNameWrapper.setError("Last name cannot be blank.");
            result = false;
        }
        else
            lastNameWrapper.setErrorEnabled(false);

        return result;
    }
    public void makeToast(String msg) {

        if (getActivity() == null) {
            Log.d("ERROR", "ACTIVITY CONTEXT IS NULL, UNABLE TO MAKE TOAST");
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}