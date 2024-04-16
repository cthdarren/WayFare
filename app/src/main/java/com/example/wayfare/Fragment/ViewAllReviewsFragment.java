package com.example.wayfare.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Adapters.AllReviewsAdapter;
import com.example.wayfare.Adapters.ReviewAdapter;
import com.example.wayfare.AlternateRecyclerViewInterface;
import com.example.wayfare.Models.BookmarkModel;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.ReviewItemModel;
import com.example.wayfare.Models.ReviewModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ViewAllReviewsFragment extends Fragment implements AlternateRecyclerViewInterface {

    RecyclerView allreviews_list;
    TextView allreviews_subtext;
    String username;
    List<ReviewItemModel> reviewItemModels = new ArrayList<>();
    ImageView back_btn;
    public ViewAllReviewsFragment(){}

    public void setupReviewModels(List<ReviewModel> reviewList) {
        for (ReviewModel review : reviewList) {
            ReviewItemModel toAdd = new ReviewItemModel(review.getTitle(),review.getUser().getUsername(), review.getUser().getPictureUrl(), review.getUser().getFirstName(), review.getReviewContent(), review.getDateCreated(), review.getDateModified());
            reviewItemModels.add(toAdd);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();
        if (args != null){
            if (args.getString("username") == null) {
                Toast.makeText(getContext(), "Username not found", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            }
            username = args.getString("username");
        }
        View view = inflater.inflate(R.layout.fragment_all_reviews, container, false);
        back_btn = view.findViewById(R.id.back_btn);

        allreviews_list = view.findViewById(R.id.allreviews_list);
        allreviews_list.setAdapter(new AllReviewsAdapter(getContext(), reviewItemModels, this));
        allreviews_list.setLayoutManager(new LinearLayoutManager(getContext()));

        allreviews_subtext = view.findViewById(R.id.allreviews_subtext);
        allreviews_subtext.setText("Viewing all reviews for user " + username);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

           return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new AuthService(getContext()).getResponse("/api/v1/user/"+ username + "/allreviews", false, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {

            @Override
            public void onError(String message) {
                makeToast(message);
                getParentFragmentManager().popBackStack();
            }

            @Override
            public void onResponse(ResponseModel json) {
                Type listType = new TypeToken<ArrayList<ReviewModel>>(){}.getType();
                List<ReviewModel> reviews = new Gson().fromJson(json.data, listType);
                setupReviewModels(reviews);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        allreviews_list.getAdapter().notifyDataSetChanged();
                    }
                });
            }
        });


    }

    public void makeToast(String msg) {

        if (getActivity() == null) {
            Log.d("ERROR", "ACTIVITY CONTEXT IS NULL, UNABLE TO MAKE TOAST");
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAlternateItemClick(int position) {
            Bundle args = new Bundle();
            args.putString("username", reviewItemModels.get(position).username);
            Helper.goToFragmentSlideInRightArgs(args, getParentFragmentManager(), R.id.container, new ProfileFragment());
    }
}