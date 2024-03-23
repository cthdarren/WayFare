package com.example.wayfare;

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

import com.example.wayfare.models.SettingItem;

import java.util.ArrayList;

public class SettingsFragment extends Fragment implements RecyclerViewInterface{

    private RecyclerView settingsRecyclerView;

    private ArrayList<SettingItem> settingItems = new ArrayList<>();



    private void setupSettingItems(Context context){
        settingItems.add(new SettingItem("Privacy", context.getDrawable(R.drawable.privacy)));
        settingItems.add(new SettingItem("General", context.getDrawable(R.drawable.settings_icon)));
        settingItems.add(new SettingItem("Accessibility", context.getDrawable(R.drawable.accessibility)));
        settingItems.add(new SettingItem("Notifications", context.getDrawable(R.drawable.notifications)));
        settingItems.add(new SettingItem("Payments", context.getDrawable(R.drawable.payment)));
        settingItems.add(new SettingItem("Report a Problem", context.getDrawable(R.drawable.report)));
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
        System.out.println(String.valueOf(position));
    }
}
