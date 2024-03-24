package com.example.wayfare.Fragment.Public;

import static com.example.wayfare.Utils.Helper.goToLogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Activity.settings.AccessibilitySettingsActivity;
import com.example.wayfare.Activity.settings.GeneralSettingsActivity;
import com.example.wayfare.Activity.settings.NotificationSettingsActivity;
import com.example.wayfare.Activity.settings.PaymentSettingsActivity;
import com.example.wayfare.Activity.settings.PrivacySettingsActivity;
import com.example.wayfare.Activity.settings.ReportSettingsActivity;
import com.example.wayfare.Adapters.SettingsRecViewAdapter;
import com.example.wayfare.Fragment.SignInFragment;
import com.example.wayfare.Models.SettingItemModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;

import java.util.ArrayList;

public class PublicSettingsFragment extends Fragment implements RecyclerViewInterface {

    private RecyclerView settingsRecyclerView;

    private ArrayList<SettingItemModel> settingItemModels = new ArrayList<>();
    Button loginBtn;
    TextView signUpBtn;

    public PublicSettingsFragment() {
    }
    private void setupSettingItems(Context context){
        settingItemModels.add(new SettingItemModel("Privacy", context.getDrawable(R.drawable.privacy), PrivacySettingsActivity.class));
        settingItemModels.add(new SettingItemModel("General", context.getDrawable(R.drawable.settings_icon), GeneralSettingsActivity.class));
        settingItemModels.add(new SettingItemModel("Accessibility", context.getDrawable(R.drawable.accessibility), AccessibilitySettingsActivity.class));
        settingItemModels.add(new SettingItemModel("Notifications", context.getDrawable(R.drawable.notifications), NotificationSettingsActivity.class));
        settingItemModels.add(new SettingItemModel("Payments", context.getDrawable(R.drawable.payment), PaymentSettingsActivity.class));
        settingItemModels.add(new SettingItemModel("Report a Problem", context.getDrawable(R.drawable.report), ReportSettingsActivity.class));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.public_fragment_settings, container, false);
        loginBtn = view.findViewById(R.id.loginBtn);
        signUpBtn = view.findViewById(R.id.signUpBtn);

        setupSettingItems(view.getContext());

        settingsRecyclerView = view.findViewById(R.id.settings_list_recview);
        SettingsRecViewAdapter adapter = new SettingsRecViewAdapter(view.getContext(), settingItemModels, this);
        settingsRecyclerView.setAdapter(adapter);
        settingsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin(getParentFragmentManager());
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin(getParentFragmentManager());
            }
        });

        return view;
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), settingItemModels.get(position).activity);
        startActivity(intent);
    }
}