package com.example.wayfare.Utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.wayfare.Fragment.SignInFragment;
import com.example.wayfare.R;

import java.time.Instant;

public class Helper {
    public static void goToLogin(FragmentManager fm) {
        fm.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_bottom)
                .add(R.id.container, new SignInFragment())
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }

    public static void goToFragment(FragmentManager fm, int fragmentId, Fragment fragment) {
        fm.beginTransaction()
                .replace(fragmentId, fragment)
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }

    public static void goToFragmentSlideInRight(FragmentManager fm, int fragmentId, Fragment fragment) {
        fm.beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_left_to_right, // enter
                        R.anim.slide_right_to_left, // exit
                        R.anim.fade_in, // popEnter
                        R.anim.slide_out_left_to_right// popExit
                )
                .add(fragmentId, fragment)
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }

    public static void goToFullScreenFragmentFromBottom(FragmentManager fm, Fragment fragment) {
        fm.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_bottom)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }

    public enum RequestType {
        REQ_GET,
        REQ_POST
    }

    public static String getDifferenceInTimeString(Instant firstInstant, Instant secondInstant) {
        long number;
        String timeCategory;

        long epochDiff = firstInstant.getEpochSecond() - secondInstant.getEpochSecond();
        if (epochDiff > 60 * 60 * 24 * 365) {
            number = epochDiff / (60 * 60 * 24 * 365);
            timeCategory = "year";
        } else if (epochDiff > 60 * 60 * 24 * 12) {
            number = epochDiff / (60 * 60 * 24 * 12);
            timeCategory = "month";
        } else if (epochDiff > 60 * 60 * 24) {
            number = epochDiff / (60 * 60 * 24);
            timeCategory = "day";
        } else if (epochDiff > 60 * 60) {
            number = epochDiff / (60 * 60);
            timeCategory = "hour";
        } else if (epochDiff > 60) {
            number = epochDiff / 60;
            timeCategory = "minute";
        } else {
            number = epochDiff;
            timeCategory = "second";
        }
        if (number > 1) {
            timeCategory += "s";
        }
        return String.format("%s %s", number, timeCategory);
    }
}
