package com.example.wayfare.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CreateReviewFragment extends Fragment {

    Button cancel, submit;
    ImageView star1, star2, star3, star4, star5;
    TextInputEditText title, description;
    TextInputLayout titleWrapper, descriptionWrapper;
    TextView reviewHeader;
    int rating;
    String wayfarer;
    String listingId = "";
    public CreateReviewFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review_create, container, false);

        if (getArguments() != null) {
            listingId = getArguments().getString("listingId", "");
            wayfarer = getArguments().getString("wayfarer", "");
        }
        if (Objects.equals(listingId, "")){
            getParentFragmentManager().popBackStack();
        }
        reviewHeader = view.findViewById(R.id.reviewHeader);
        cancel = view.findViewById(R.id.reviewCancel);
        submit = view.findViewById(R.id.submitReview);
        star1 = view.findViewById(R.id.score1);
        star2 = view.findViewById(R.id.score2);
        star3 = view.findViewById(R.id.score3);
        star4 = view.findViewById(R.id.score4);
        star5 = view.findViewById(R.id.score5);
        title = view.findViewById(R.id.reviewTitle);
        titleWrapper = view.findViewById(R.id.reviewTitleWrapper);
        description = view.findViewById(R.id.reviewDescription);
        descriptionWrapper = view.findViewById(R.id.reviewDescriptionWrapper);

        reviewHeader.setText(String.format("Leave a review for your previous trip with %s!", wayfarer));
        submit.setEnabled(false);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().length() < 1)
                    titleWrapper.setError("Title cannot be empty");
                else
                    titleWrapper.setErrorEnabled(false);

                if (description.getText().length() < 1)
                    descriptionWrapper.setError("Title cannot be empty");
                else
                    descriptionWrapper.setErrorEnabled(false);

                if (!(description.getText().length() < 1 | title.getText().length() < 1)) {
                    String json = String.format("""
                            {"title":"%s", "score":%d,"reviewContent":"%s","listingId":"%s"} """, title.getText().toString(), rating, description.getText().toString(), listingId);
                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
                    new AuthService(getContext()).getResponse("/review/create", true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
                        @Override
                        public void onError(String message) {
                            makeToast(message);
                        }

                        @Override
                        public void onResponse(ResponseModel json) {
                            makeToast(json.data.getAsString());
                            getParentFragmentManager().popBackStack();
                        }
                    });
                }
            }
        });
        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = 1;
                changeStars(rating);
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = 2;
                changeStars(rating);
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = 3;
                changeStars(rating);
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = 4;
                changeStars(rating);
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = 5;
                changeStars(rating);
            }
        });

        return view;
    }
    public void changeStars(int value){
        switch (value){
            case 1:
                star1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_fill, null));
                star2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_empty, null));
                star3.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_empty, null));
                star4.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_empty, null));
                star5.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_empty, null));
                break;
            case 2:
                star1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_fill, null));
                star2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_fill, null));
                star3.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_empty, null));
                star4.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_empty, null));
                star5.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_empty, null));
                break;
            case 3:
                star1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_fill, null));
                star2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_fill, null));
                star3.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_fill, null));
                star4.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_empty, null));
                star5.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_empty, null));
                break;
            case 4:
                star1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_fill, null));
                star2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_fill, null));
                star3.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_fill, null));
                star4.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_fill, null));
                star5.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_empty, null));
                break;
            default:
                star1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_fill, null));
                star2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_fill, null));
                star3.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_fill, null));
                star4.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_fill, null));
                star5.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_fill, null));
                break;
        }
        submit.setEnabled(true);
    }

    public void makeToast(String message){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}