package com.example.wayfare.Fragment;

import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Adapters.BookmarkAdapter;
import com.example.wayfare.Models.BookmarkItemModel;
import com.example.wayfare.Models.BookmarkModel;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookmarksFragment extends Fragment implements RecyclerViewInterface {

    RecyclerView bookmarkRecycler;
    List<BookmarkModel> bookmarkModelList;
    List<BookmarkItemModel> bookmarkItemModels;
    ImageView backButton;

    public void setupBookmarksList(List<BookmarkModel> bookmarkModels){
        for (BookmarkModel bm: bookmarkModels){
            bookmarkItemModels.add(new BookmarkItemModel(bm.listing.getTitle(), bm.listing.getThumbnailUrls()[0], bm.user.getUsername(), bm.listing.getRegion(), bm.listing.getRating(), bm.listing.getReviewCount(), bm.listing.getId()));
        }
    }
    public BookmarksFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bookmarkModelList = new ArrayList<>();
        bookmarkItemModels = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        bookmarkRecycler = view.findViewById(R.id.bookmarkRecycler);
        backButton = view.findViewById(R.id.profile_back);

        bookmarkRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        bookmarkRecycler.setAdapter(new BookmarkAdapter(getContext(), bookmarkItemModels, this));

        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        new AuthService(getContext()).getResponse("/getbookmarks", true, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(ResponseModel json) {
                if (json.success){
                    Type listType = new TypeToken<ArrayList<BookmarkModel>>(){}.getType();
                    bookmarkModelList = new Gson().fromJson(json.data, listType);
                    setupBookmarksList(bookmarkModelList);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bookmarkRecycler.getAdapter().notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        //TODO GO TO LISTING
//        TourListingFull tourListingFullFragment = new TourListingFull();
//        tourListingFullFragment.setArguments(data);
//        Helper.goToFragmentSlideInRight(getParentFragmentManager(), R.id.container, new TourListingFull());
    }
}