package com.example.wayfare.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Models.BookmarkItemModel;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.ReviewItemModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {


    private final List<BookmarkItemModel> bookmarkItemModels;
    private final Context context;

    private RecyclerViewInterface recyclerViewInterface;

    public BookmarkAdapter(Context context, List<BookmarkItemModel> bookmarkItemModels, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.bookmarkItemModels = bookmarkItemModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view, recyclerViewInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.listingTitle.setText(bookmarkItemModels.get(position).title);
        holder.listingLocation.setText(bookmarkItemModels.get(position).location);
        holder.wayfarerUsername.setText(bookmarkItemModels.get(position).username);
        holder.listingRating.setText(bookmarkItemModels.get(position).ratings);
        holder.bookmarkBtn.setChecked(true);
        holder.bookmarkBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String apiurl = "/unbookmark";
                if (isChecked) {
                    apiurl = "/bookmark";
                }
                String json = String.format("""
                            {"listingId": "%s"}
                            """, bookmarkItemModels.get(position).listingId);
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
                new AuthService(context).getResponse(apiurl, true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
                    @Override
                    public void onError(String message) {
                        //TODO handle error
                    }

                    @Override
                    public void onResponse(ResponseModel json) {
                        // TODO honestly don't have to do anythign??
                    }
                });
            }
        });

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Looper uiLooper = Looper.getMainLooper();
        final Handler handler = new Handler(uiLooper);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String picUrl = bookmarkItemModels.get(position).thumbnail;
                    URL url = new URL(picUrl);
                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.listingThumbnail.setImageBitmap(image);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookmarkItemModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView listingTitle;
        private TextView listingLocation;
        private TextView listingRating;
        private TextView wayfarerUsername;
        private ImageView listingThumbnail;
        private CheckBox bookmarkBtn;


        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            wayfarerUsername = itemView.findViewById(R.id.wayfarerUsername);
            listingLocation = itemView.findViewById(R.id.listingLocation);
            listingRating = itemView.findViewById(R.id.listingRating);
            listingTitle = itemView.findViewById(R.id.listingTitle);
            listingThumbnail = itemView.findViewById(R.id.listingThumbnail);
            bookmarkBtn = itemView.findViewById(R.id.bookmarkBtn);

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

