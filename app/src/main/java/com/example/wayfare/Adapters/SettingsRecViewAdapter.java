package com.example.wayfare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.Models.SettingItemModel;

import java.util.ArrayList;
import java.util.List;

public class SettingsRecViewAdapter extends RecyclerView.Adapter<SettingsRecViewAdapter.ViewHolder>{

    private final List<SettingItemModel> settingItemModels;
    private final Context context;

    private final RecyclerViewInterface recyclerViewInterface;

    public SettingsRecViewAdapter(Context context, List<SettingItemModel> settingItemModels, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.settingItemModels = settingItemModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_list_item, parent,false);
        ViewHolder holder = new ViewHolder(view, recyclerViewInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.settingName.setText(settingItemModels.get(position).name);
        holder.settingIcon.setImageDrawable(settingItemModels.get(position).icon);

    }

    @Override
    public int getItemCount() {
        return settingItemModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView settingName;
        private ImageView settingIcon;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            settingName = itemView.findViewById(R.id.settings_item_name);
            settingIcon = itemView.findViewById(R.id.settings_item_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getBindingAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }

}
