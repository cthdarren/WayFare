package com.example.wayfare.Utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.wayfare.Fragment.SignInFragment;
import com.example.wayfare.R;

public class Helper {
    public static void goToLogin(Fragment fr){
        SignInFragment sif = new SignInFragment();
        sif.setListener((SignInFragment.SignInFragmentListener) fr);
        fr.getParentFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_bottom)
                .add(R.id.flFragment, sif)
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }

    public enum RequestType{
        REQ_GET,
        REQ_POST
    }
}
