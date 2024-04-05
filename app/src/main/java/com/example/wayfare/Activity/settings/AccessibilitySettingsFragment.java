package com.example.wayfare.Activity.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthHelper;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class AccessibilitySettingsFragment extends Fragment {

    ImageView privacy_back;
    SwitchMaterial theme_change_switch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_settings_accessibility, container, false);

        theme_change_switch = view.findViewById(R.id.theme_change_switch);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            theme_change_switch.setChecked(true);
        }
        privacy_back = view.findViewById(R.id.accessibility_back);

        privacy_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        //TODO DARK MODE LIGHT MODE CHANGE just tell them that changes will be applied on app restart
        theme_change_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    new AuthHelper(getContext()).sharedPreferences.edit().putString("Theme", "DARK").apply();
                    getActivity().recreate();
                }
                else {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    new AuthHelper(getContext()).sharedPreferences.edit().putString("Theme", "LIGHT").apply();
                    getActivity().recreate();
                }
//                Intent intent = new Intent(buttonView.getContext(), MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
            }
        });

        return view;
    }

}