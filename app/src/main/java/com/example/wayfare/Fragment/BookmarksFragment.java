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
import com.example.wayfare.Models.TourListModel;
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
                            bookmarkRecycler.getAdapter().notifyItemRangeChanged(0, bookmarkItemModels.size());
                        }
                    });
                }
            }
        });
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



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
        String listingId = bookmarkItemModels.get(position).listingId;

        // Making API call for specificc listing
        new AuthService(getContext()).getResponse("/api/v1/listing/" + listingId, true, Helper.RequestType.REQ_GET, null, new AuthService.ResponseListener() {
            @Override
            public void onResponse(ResponseModel json) {
                if (json.success) {
                    TourListModel listingDetails = new Gson().fromJson(json.data.getAsJsonObject(), TourListModel.class);
                    navigateToFullPageFragment(listingDetails);
                }
            }
            @Override
            public void onError(String message) {
                // Handle network errorrs
            }
        });
    }

    private void navigateToFullPageFragment(TourListModel listingDetails) {
        Bundle data = new Bundle();
        data.putString("title", listingDetails.getTitle());
        data.putString("location", listingDetails.getRegion());
        data.putString("rating", String.valueOf(listingDetails.getRating()));
        data.putString("price", String.valueOf(listingDetails.getPrice()));
        data.putString("thumbnail", listingDetails.getThumbnailUrls()[0]);
        data.putString("description", listingDetails.getDescription());
        data.putString("reviewCount", String.valueOf(listingDetails.getReviewCount()));
        data.putString("listingId", listingDetails.getId());
        data.putInt("minPax", listingDetails.getMinPax());
        data.putInt("maxPax", listingDetails.getMaxPax());
        data.putString("userId", listingDetails.getUserId());
        data.putString("category", listingDetails.getCategory());
        data.putParcelableArrayList("timeRangeList", new ArrayList<>(listingDetails.getTimeRangeList()));

        TourListingFull tourListingFullFragment = new TourListingFull();
        tourListingFullFragment.setArguments(data);

        Helper.goToFragmentSlideInRightArgs(data, getParentFragmentManager(), R.id.container, tourListingFullFragment);
    }
}