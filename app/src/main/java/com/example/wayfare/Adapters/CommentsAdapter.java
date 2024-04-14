package com.example.wayfare.Adapters;
import static com.example.wayfare.Utils.Helper.convertStringToShortDate;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wayfare.Models.Comment;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.R;
import com.example.wayfare.tourListing_RecyclerViewInterface;

import java.util.ArrayList;
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>{
    ArrayList<Comment> commentsList;
    Context context;
    private int selectedPosition = RecyclerView.NO_POSITION;
    public CommentsAdapter(Context context, ArrayList<Comment> commentsList){
        this.context = context;
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public CommentsAdapter.CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        CommentsAdapter.CommentsViewHolder viewHolder = new CommentsAdapter.CommentsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.CommentsViewHolder holder, int position) {
        Glide.with(context)
                .load(getBaseUrl(commentsList.get(position).user.getPictureUrl())) // Load the first URL from the array
                .into(holder.comment_avatar);
        holder.userName.setText(commentsList.get(position).user.getUsername());
        holder.person_comment.setText(commentsList.get(position).getCommentContent());
        holder.comment_date.setText(convertStringToShortDate(commentsList.get(position).getDateCreated()));
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }
    public class CommentsViewHolder extends RecyclerView.ViewHolder{
        ImageView comment_avatar;
        TextView userName,person_comment,comment_date;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            comment_avatar = itemView.findViewById(R.id.comment_avatar);
            userName = itemView.findViewById(R.id.person_username);
            person_comment = itemView.findViewById(R.id.person_comment);
            comment_date = itemView.findViewById(R.id.comment_date);
        }
    }
    public static String getBaseUrl(String url) {
        int index = url.indexOf("?");
        if (index != -1) {
            return url.substring(0, index);
        } else {
            return url; // If there is no question mark, return the original URL
        }
    }
}
