package com.example.wayfare.Adapters;
import static com.example.wayfare.Utils.Helper.convertStringToShortDate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wayfare.Fragment.ProfileFragment;
import com.example.wayfare.Fragment.SingularJourneyFragment;
import com.example.wayfare.Models.Comment;
import com.example.wayfare.Models.ShortsObject;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.Models.UserModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.tourListing_RecyclerViewInterface;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>{
    private ShortsAdapter.ShortsViewHolder shortsView;
    ArrayList<Comment> commentsList;
    Context context;
    String userNameAuthor;
    FragmentManager fragmentManager;
    SingularJourneyFragment singularJourneyFragment;
    private int selectedPosition = RecyclerView.NO_POSITION;
    public CommentsAdapter(Context context, ArrayList<Comment> commentsList, String userNameAuthor, FragmentManager fragmentManager, ShortsAdapter.ShortsViewHolder shortsView, SingularJourneyFragment singularJourneyFragment){
        this.context = context;
        this.commentsList = commentsList;
        this.userNameAuthor = userNameAuthor;
        this.fragmentManager = fragmentManager;
        this.shortsView = shortsView;
        this.singularJourneyFragment = singularJourneyFragment;
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
        holder.setCommentsData(commentsList.get(position));
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }
    public class CommentsViewHolder extends RecyclerView.ViewHolder{
        ImageView comment_avatar;
        TextView userName,person_comment,comment_date,commentsAuthor;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            comment_avatar = itemView.findViewById(R.id.comment_avatar);
            userName = itemView.findViewById(R.id.person_username);
            person_comment = itemView.findViewById(R.id.person_comment);
            comment_date = itemView.findViewById(R.id.comment_date);
            commentsAuthor = itemView.findViewById(R.id.commentsAuthor);
        }
        @SuppressLint("ClickableViewAccessibility")
        void setCommentsData(Comment comment){
            Glide.with(context)
                    .load(getBaseUrl(comment.user.getPictureUrl())) // Load the first URL from the array
                    .override(30, 30) // Set the dimensions to 30dp by 30dp
                    .into(comment_avatar);
            userName.setText(comment.user.getUsername());
            if(comment.user.getUsername().equals(userNameAuthor)){
                commentsAuthor.setVisibility(View.VISIBLE);
            }else{commentsAuthor.setVisibility(View.INVISIBLE);}
            person_comment.setText(comment.getCommentContent());
            comment_date.setText(convertStringToShortDate(comment.getDateCreated()));
            comment_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(singularJourneyFragment!=null){
                        singularJourneyFragment.pauseVideo();
                    }
                    if(shortsView!=null){
                        shortsView.pauseVideo();
                    }
                    Bundle username = new Bundle();
                    username.putString("username", comment.user.getUsername());
                    ProfileFragment pf = new ProfileFragment();
                    pf.setArguments(username);
                    Helper.goToFragmentSlideInRight((fragmentManager), R.id.container, pf);
                }
            });
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
