package com.example.wayfare.Fragment.CreateListing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfare.Adapters.ListingPicturesAdapter;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.Utils.AzureStorageManager;
import com.example.wayfare.Utils.Helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CreateListingFragment6 extends Fragment implements RecyclerViewInterface {

    Button continue_button, addImageButton;
    RecyclerView listingImagesRecycler;
    ArrayList<String> picUrlList = new ArrayList<>();
    List<Uri> uriList = new ArrayList<>();
    int uploadCount;
    ActivityResultLauncher<String> getPics = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), new ActivityResultCallback<List<Uri>>() {
        @Override
        public void onActivityResult(List<Uri> o) {
            boolean changed = false;
            for (Uri uri : o){
                // TODO figure out how to parse the uri and upload to server, then get the url strings back
                if (uriList.size() < 10){
                    changed = true;
                    uriList.add(uri);
                }
            }
            if (changed)
                listingImagesRecycler.getAdapter().notifyDataSetChanged();
        }

    });
    public CreateListingFragment6() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();

        View view = inflater.inflate(R.layout.fragment_listing_create6, container, false);
        continue_button = view.findViewById(R.id.continue_button);
        addImageButton = view.findViewById(R.id.addImagesButton);
        listingImagesRecycler = view.findViewById(R.id.listingImagesRecycler);
        listingImagesRecycler.setAdapter(new ListingPicturesAdapter(getContext(), uriList, this));
        listingImagesRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                getPics.launch("image/*");
//                profile_picture.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        });

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continue_button.setEnabled(false);
                massUploadUriList(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                continue_button.setEnabled(true);
                                Helper.goToFragmentSlideInRightArgs(args, getParentFragmentManager(), R.id.container, new CreateListingFragment7());
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        args.putStringArrayList("thumbnailurls", picUrlList);
                        Helper.goToFragmentSlideInRightArgs(args, getParentFragmentManager(), R.id.container, new CreateListingFragment7());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                continue_button.setEnabled(true);
                            }
                        });
                    }
                });

            }
        });
        return view;
    }

    public void massUploadUriList(Callback callback){
        CountDownLatch latch = new CountDownLatch(uriList.size());
        for (Uri uri: uriList){
        AzureStorageManager.uploadBlob(getContext(), uri, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                picUrlList.add(response.request().url().toString());
                latch.countDown();
                if (latch.getCount() == 0){
                    callback.onResponse(call, response);
                }
            }
            });
        }
    }

    public void reduceCount(){
        uploadCount --;
    }
    @Override
    public void onItemClick(int position) {
        uriList.remove(position);
        listingImagesRecycler.getAdapter().notifyItemRemoved(position);
    }
}