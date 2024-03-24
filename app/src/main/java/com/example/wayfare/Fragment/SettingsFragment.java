package com.example.wayfare.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Activity.settings.AccessibilitySettingsActivity;
import com.example.wayfare.Activity.settings.GeneralSettingsActivity;
import com.example.wayfare.Activity.settings.NotificationSettingsActivity;
import com.example.wayfare.Activity.settings.PaymentSettingsActivity;
import com.example.wayfare.Activity.settings.PrivacySettingsActivity;
import com.example.wayfare.Activity.settings.ReportSettingsActivity;
import com.example.wayfare.Adapters.SettingsRecViewAdapter;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.Models.SettingItem;

import java.util.ArrayList;

public class SettingsFragment extends Fragment implements RecyclerViewInterface {

    private RecyclerView settingsRecyclerView;

    private ArrayList<SettingItem> settingItems = new ArrayList<>();



    private void setupSettingItems(Context context){
        settingItems.add(new SettingItem("Privacy", context.getDrawable(R.drawable.privacy), PrivacySettingsActivity.class));
        settingItems.add(new SettingItem("General", context.getDrawable(R.drawable.settings_icon), GeneralSettingsActivity.class));
        settingItems.add(new SettingItem("Accessibility", context.getDrawable(R.drawable.accessibility), AccessibilitySettingsActivity.class));
        settingItems.add(new SettingItem("Notifications", context.getDrawable(R.drawable.notifications), NotificationSettingsActivity.class));
        settingItems.add(new SettingItem("Payments", context.getDrawable(R.drawable.payment), PaymentSettingsActivity.class));
        settingItems.add(new SettingItem("Report a Problem", context.getDrawable(R.drawable.report), ReportSettingsActivity.class));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSettingItems(view.getContext());

        settingsRecyclerView = view.findViewById(R.id.settings_list_recview);
        SettingsRecViewAdapter adapter = new SettingsRecViewAdapter(view.getContext(), settingItems, this);
        settingsRecyclerView.setAdapter(adapter);
        settingsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), settingItems.get(position).activity);
        startActivity(intent);
    }
}
