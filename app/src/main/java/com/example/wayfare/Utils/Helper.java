package com.example.wayfare.Utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.wayfare.Fragment.SignInFragment;
import com.example.wayfare.R;

public class Helper {
    public static void goToLogin(FragmentManager fm){
        fm.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_bottom)
                .add(R.id.container, new SignInFragment())
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }

    public static void goToFragment(FragmentManager fm, Fragment fragment){
        fm.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }

    public enum RequestType{
        REQ_GET,
        REQ_POST
    }
}
