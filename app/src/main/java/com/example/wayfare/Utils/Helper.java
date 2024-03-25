package com.example.wayfare.Utils;

import androidx.fragment.app.FragmentManager;

import com.example.wayfare.Fragment.SignInFragment;
import com.example.wayfare.R;

public class Helper {
    public static void goToLogin(FragmentManager fm){
        fm.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_bottom)
                .add(R.id.flFragment, new SignInFragment())
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }
}
