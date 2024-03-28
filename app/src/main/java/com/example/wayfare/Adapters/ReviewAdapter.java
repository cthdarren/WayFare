package com.example.wayfare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Models.ReviewItemModel;
import com.example.wayfare.Models.SettingItemModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;

import java.util.List;

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

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{


    private final List<ReviewItemModel> reviewItemModels;
    private final Context context;

    private final RecyclerViewInterface recyclerViewInterface;

    public ReviewAdapter(Context context, List<ReviewItemModel> reviewItemModels, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.reviewItemModels = reviewItemModels;
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
        holder.settingName.setText(reviewItemModels.get(position));
        holder.settingIcon.setImageDrawable(reviewItemModels.get(position).icon);

    }

    @Override
    public int getItemCount() {
        return reviewItemModels.size();
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

