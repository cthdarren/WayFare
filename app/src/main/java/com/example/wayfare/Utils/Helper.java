package com.example.wayfare.Utils;

import androidx.fragment.app.FragmentManager;

import com.example.wayfare.Fragment.SignInFragment;
import com.example.wayfare.R;

public class Helper {
    public static void goToLogin(FragmentManager fm){
        fm.beginTransaction()
                .replace(R.id.flFragment, new SignInFragment())
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }
}
