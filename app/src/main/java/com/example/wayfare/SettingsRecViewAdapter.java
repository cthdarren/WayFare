package com.example.wayfare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.models.SettingItem;

import java.util.ArrayList;

public class SettingsRecViewAdapter extends RecyclerView.Adapter<SettingsRecViewAdapter.ViewHolder>{

    private ArrayList<SettingItem> settingItems = new ArrayList<>();


    public SettingsRecViewAdapter(){}
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_list_item, parent,false);
        ViewHolder holder = new ViewHolder(view);

        settingItems.add(new SettingItem("Privacy", parent.getContext().getDrawable(R.drawable.settings_icon)));
        settingItems.add(new SettingItem("General", parent.getContext().getDrawable(R.drawable.settings_icon)));
        settingItems.add(new SettingItem("Accessibility", parent.getContext().getDrawable(R.drawable.settings_icon)));
        settingItems.add(new SettingItem("Notifications", parent.getContext().getDrawable(R.drawable.settings_icon)));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.settingName.setText(settingItems.get(position).name);
        holder.settingIcon.setImageDrawable(settingItems.get(position).icon);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView settingName;
        private ImageView settingIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            settingName = itemView.findViewById(R.id.settings_item_name);
            settingIcon = itemView.findViewById(R.id.settings_item_icon);

        }
    }

}
