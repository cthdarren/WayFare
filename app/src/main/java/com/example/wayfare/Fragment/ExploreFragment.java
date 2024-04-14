package com.example.wayfare.Fragment;

import com.example.wayfare.Activity.MainActivity;
import com.example.wayfare.BuildConfig;
import com.example.wayfare.Models.Comment;
import com.example.wayfare.Models.TourListModel;
import com.example.wayfare.Models.UserModel;
import com.example.wayfare.Models.ResponseModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.wayfare.Utils.AuthHelper;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.ViewModel.UserViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.wayfare.Adapters.ShortsAdapter;
import com.example.wayfare.Models.ShortsObject;
import com.example.wayfare.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import androidx.activity.OnBackPressedCallback;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExploreFragment extends Fragment {
    private boolean loggedIn;
    private UserViewModel userViewModel;
    private Context context = null;
    private ViewPager2 shortsViewPager;
    private List<ShortsObject> shortsObjectList;
    private ShortsAdapter shortsAdapter;
    private BottomNavigationView bottomNavigationView;
    ;
    private boolean doubleBackToExitPressedOnce = false;

    public ExploreFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getActivity(); // use this reference to invoke main callbacks
        } catch (IllegalStateException e) {
            throw new IllegalStateException();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<ShortsObject> shortsObjectList = getShortsObjects();
        OkHttpClient tokenClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BuildConfig.API_URL + "/api/v1/shorts")
                .get()
                .build();
        tokenClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle success
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected response code: " + response);
                }
                String responseBody = response.body().string();
                Gson gson = new Gson();
                ResponseModel responseModel = gson.fromJson(responseBody,ResponseModel.class);
                if (responseModel != null && responseModel.success) {
                    JsonArray allShortsInfo = responseModel.data.getAsJsonArray();
                    shortsObjectList.clear();
                    for (JsonElement shorts : allShortsInfo){
                        String eachString = shorts.toString();
                        ShortsObject testing = new Gson().fromJson(eachString, ShortsObject.class);
                        Collections.sort(testing.getComments(), new Comparator<Comment>() {
                            @Override
                            public int compare(Comment o1, Comment o2) {
                                // Compare dates in descending order
                                return o2.getDateCreated().compareTo(o1.getDateCreated());
                            }
                        });
                        shortsObjectList.add(testing);
                    }
                    Collections.sort(shortsObjectList, new Comparator<ShortsObject>() {
                        @Override
                        public int compare(ShortsObject o1, ShortsObject o2) {
                            // Compare dates in descending order
                            return o2.getDatePosted().compareTo(o1.getDatePosted());
                        }
                    });
                    if (getActivity() != null) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                shortsAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                } else {
                    System.out.println("API response indicates failure.");
                }
            }
        });

//        get user stuff
//        if (new AuthHelper(requireActivity().getApplicationContext()).isLoggedIn()) {
//            loggedIn = true;
//        } else {
//            loggedIn = false;
//        }
//        if (loggedIn) {
//            userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
//            UserModel userData = userViewModel.getUserProfileData();
//        }
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        UserModel userData = userViewModel.getUserProfileData();
        String userName;
        if(userData == null){
            userName = null;
        }else{
            userName = userData.getUsername();
        }
        shortsViewPager = view.findViewById(R.id.shortsViewPager);
        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        shortsAdapter = new ShortsAdapter(shortsObjectList, context,getParentFragmentManager(),ExploreFragment.this,userData);
        shortsViewPager.setAdapter(shortsAdapter);
        shortsViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffset == 0 && positionOffsetPixels == 0) {
                        int prevPosition = shortsAdapter.getCurrentPosition();
                        int currPosition = position;
                        if(prevPosition==currPosition){
                            if(prevPosition==0){
                                shortsAdapter.playVideo(position);
                            }else if(prevPosition==shortsObjectList.size()-1){
                                Toast.makeText(requireContext(), "End of Journeys", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            shortsAdapter.stopVideo(prevPosition);
                            Log.d("ViewHolderPosition", "stopping: " + shortsAdapter.getCurrentPosition());
                            shortsAdapter.updateCurrentPosition(currPosition);
                            shortsAdapter.playVideo(currPosition);
                            Log.d("ViewHolderPosition", "playing: " + position);
                        }
                    //Log.e("Selected_Page", String.valueOf(shortsAdapter.getCurrentPosition()));

                }

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
//                shortsAdapter.pauseVideo(shortsAdapter.getCurrentPosition());
//                shortsAdapter.playVideo(position);
//                //Log.e("Selected_Page", String.valueOf(shortsAdapter.getCurrentPosition()));
//                shortsAdapter.updateCurrentPosition(position);
//                shortsAdapter.initiaLizeVideo(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

//        bottomNavigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                // Remove the listener to prevent multiple calls
//                bottomNavigationView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//
//                // Get the height of the BottomNavigationView
//                int bottomNavHeight = bottomNavigationView.getHeight();
//                Log.d("BottomNavHeight", "Height: " + bottomNavHeight);
//                // Set bottom padding for the ViewPager2
//                shortsViewPager.setPadding(0, 0, 0, bottomNavHeight);
//            }
//        });

        shortsViewPager.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                onStart();
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
//                Log.i("position", viewPager2.getVerticalScrollbarPosition() + "");
//                shortsAdapter.stopAllVideo();
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (getParentFragmentManager().getBackStackEntryCount() > 1) {
                    getParentFragmentManager().popBackStack();
                    String prev = getParentFragmentManager().getBackStackEntryAt(getParentFragmentManager().getBackStackEntryCount()-2).getName();
                    int idToGo;
                    boolean back = true;
                    switch (prev){
                        case "com.example.wayfare.Fragment.SettingsFragment":
                            idToGo = R.id.account;
                            break;
                        case "com.example.wayfare.Fragment.Public.PublicSettingsFragment":
                            idToGo = R.id.account;
                            break;
                        case "com.example.wayfare.Fragment.ToursFragment":
                            idToGo = R.id.tours;
                            break;
                        case "com.example.wayfare.Fragment.UpcomingFragment":
                            idToGo = R.id.upcoming;
                            break;
                        case "com.example.wayfare.Fragment.Public.PublicUpcomingFragment":
                            idToGo = R.id.upcoming;
                            break;
                        default:
                            idToGo = R.id.explore;
                            back = false;
                            break;
                    }
                    ((MainActivity) getActivity()).setBacking(back);
                    bottomNavigationView.setSelectedItemId(idToGo);
                }
                else{
                    if (doubleBackToExitPressedOnce) {
                        // Exit the app
                        shortsAdapter.stopAllVideo();
                        requireActivity().finish();
                    } else {
//                        moveToNextScroll();
                        // Show toast message
                        Toast.makeText(requireContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
                        // Set the flag
                        doubleBackToExitPressedOnce = true;
                        // Reset the flag after a certain delay
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                doubleBackToExitPressedOnce = false;
                            }
                        }, 2000); // Adjust the duration as needed (here, 2000 milliseconds = 2 seconds)
                    }
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }
    public void disableShortsViewPagerScroll() {
        if (shortsViewPager != null) {
            shortsViewPager.setUserInputEnabled(false);
        }
    }

    // Method to enable scrolling in shortsViewPager
    public void enableShortsViewPagerScroll() {
        if (shortsViewPager != null) {
            shortsViewPager.setUserInputEnabled(true);
        }
    }

    @NonNull
    private static List<ShortsObject> getShortsObjects() {
        List<ShortsObject> shortsObjectList = new ArrayList<>();
//        ShortsObject shortsObject1 = new ShortsObject();
//        shortsObject1.setShortsUrl("https://wayfareshorts.blob.core.windows.net/test/video1.mp4");
//        shortsObject1.setUserName("Singapore!");
//        shortsObject1.setDescription("Singapore Tours #sg #local");
//        shortsObjectList.add(shortsObject1);
//
//        ShortsObject shortsObject2 = new ShortsObject();
//        shortsObject2.setShortsUrl("https://wayfareshorts.blob.core.windows.net/test/video2.mp4");
//        shortsObject2.setUserName("Overrated Cities?!");
//        shortsObject2.setDescription("#dontgo #cities");
//        shortsObjectList.add(shortsObject2);
//
//        ShortsObject shortsObject3 = new ShortsObject();
//        shortsObject3.setShortsUrl("https://wayfareshorts.blob.core.windows.net/test/video3.mp4");
//        shortsObject3.setUserName("Lorem");
//        shortsObject3.setDescription("GreyNibba");
//        shortsObjectList.add(shortsObject3);
//        ShortsObject shortsObject4 = new ShortsObject();
//        shortsObject4.setShortsUrl("https://static.videezy.com/system/resources/previews/000/054/979/original/Fire_HD.mp4");
//        shortsObject4.setUserName("10 best places SG");
//        shortsObject4.setDescription("#must go");
//        shortsObjectList.add(shortsObject4);
//        ShortsObject shortsObject5 = new ShortsObject();
//        shortsObject5.setShortsUrl("https://wayfareshorts.blob.core.windows.net/test/video2.mp4");
//        shortsObject5.setUserName("Lorem");
//        shortsObject5.setDescription("GeyNibba");
//        shortsObjectList.add(shortsObject5);
//        ShortsObject shortsObject6 = new ShortsObject();
//        shortsObject6.setShortsUrl("https://wayfareshorts.blob.core.windows.net/test/video3.mp4");
//        shortsObject6.setUserName("yyyy");
//        shortsObject6.setDescription("yyyy");
//        shortsObjectList.add(shortsObject6);
//        ShortsObject shortsObject7 = new ShortsObject();
//        shortsObject7.setShortsUrl("https://wayfareshorts.blob.core.windows.net/test/video1.mp4");
//        shortsObject7.setUserName("xxxx");
//        shortsObject7.setDescription("xxxx");
//        shortsObjectList.add(shortsObject7);
        return shortsObjectList;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        shortsAdapter.pauseVideo(shortsAdapter.getCurrentPosition());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        shortsAdapter.stopAllVideo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        shortsAdapter.stopAllVideo();
    }

    public void pauseVideo() {
        //save pos
        SharedPreferences currentShortsPref = requireActivity().getSharedPreferences("shortsSharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor positionEditor = currentShortsPref.edit();
        int currentPosition = shortsAdapter.getCurrentPosition();
        positionEditor.putInt("position", currentPosition);
        shortsAdapter.pauseVideo(currentPosition);
        positionEditor.apply();
    }

    public void continueVideo() {
        SharedPreferences currentPosPref = requireActivity().getSharedPreferences("shortsSharedPref", Context.MODE_PRIVATE);
        int currentPosition = currentPosPref.getInt("position", -1);
        if (currentPosition != -1) {
            shortsAdapter.playVideo(currentPosition);
        }
    }

    private void moveToNextScroll() {
        int currentItem = shortsViewPager.getCurrentItem();
        if (currentItem < shortsAdapter.getItemCount() - 1 && currentItem + 1 < shortsAdapter.getViewsCount()) {
            shortsViewPager.setCurrentItem(currentItem + 1, true); // Move to the next scroll
        } else {
            // Do something else, or handle reaching the end of the scrolls
        }
    }
}