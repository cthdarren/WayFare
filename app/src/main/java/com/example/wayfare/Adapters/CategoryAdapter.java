package com.example.wayfare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Models.CategoryItemModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private final List<CategoryItemModel> categoryItemModels;
    private final Context context;

    private final RecyclerViewInterface recyclerViewInterface;

    public CategoryAdapter(Context context, List<CategoryItemModel> categoryItemModels, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.categoryItemModels = categoryItemModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent,false);
        ViewHolder holder = new ViewHolder(view, recyclerViewInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.categoryName.setText(categoryItemModels.get(position).name);
        holder.categoryIcon.setImageDrawable(categoryItemModels.get(position).icon);

    }

    @Override
    public int getItemCount() {
        return categoryItemModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView categoryName;
        private ImageView categoryIcon;
        private MaterialCardView categoryCard;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
            categoryCard = itemView.findViewById(R.id.categoryCard);

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
