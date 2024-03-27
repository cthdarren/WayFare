package com.example.wayfare.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;
import androidx.activity.OnBackPressedCallback;

public class ExploreFragment extends Fragment {
    private Context context = null;
    private ViewPager2 shortsViewPager;
    private List<ShortsObject> shortsObjectList;
    private ShortsAdapter shortsAdapter;
    private BottomNavigationView bottomNavigationView;;
    private boolean doubleBackToExitPressedOnce = false;
    public ExploreFragment(){}
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getActivity(); // use this reference to invoke main callbacks
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);

        List<ShortsObject> shortsObjectList = new ArrayList<>();
        ShortsObject shortsObject1 = new ShortsObject();
        shortsObject1.setUrl("android.resource://"+getActivity().getPackageName()+"/"+R.raw.video1);
        shortsObject1.setTitle("Singapore!");
        shortsObject1.setDescription("Singapore Tours #sg #local");
        shortsObjectList.add(shortsObject1);

        ShortsObject shortsObject2 = new ShortsObject();
        shortsObject2.setUrl("android.resource://"+getActivity().getPackageName()+"/"+R.raw.video2);
        shortsObject2.setTitle("Overrated Cities?!");
        shortsObject2.setDescription("#dontgo #cities");
        shortsObjectList.add(shortsObject2);

        ShortsObject shortsObject3 = new ShortsObject();
        shortsObject3.setUrl("android.resource://"+getActivity().getPackageName()+"/"+R.raw.video3);
        shortsObject3.setTitle("Lorem");
        shortsObject3.setDescription("GreyNibba");
        shortsObjectList.add(shortsObject3);
        ShortsObject shortsObject4 = new ShortsObject();
        shortsObject4.setUrl("https://static.videezy.com/system/resources/previews/000/054/979/original/Fire_HD.mp4");
        shortsObject4.setTitle("10 best places SG");
        shortsObject4.setDescription("#must go");
        shortsObjectList.add(shortsObject4);
        ShortsObject shortsObject5 = new ShortsObject();
        shortsObject5.setUrl("android.resource://"+getActivity().getPackageName()+"/"+R.raw.video2);
        shortsObject5.setTitle("Lorem");
        shortsObject5.setDescription("GeyNibba");
        shortsObjectList.add(shortsObject5);
        ShortsObject shortsObject6 = new ShortsObject();
        shortsObject6.setUrl("android.resource://"+getActivity().getPackageName()+"/"+R.raw.video3);
        shortsObject6.setTitle("yyyy");
        shortsObject6.setDescription("yyyy");
        shortsObjectList.add(shortsObject6);
        ShortsObject shortsObject7 = new ShortsObject();
        shortsObject7.setUrl("android.resource://"+getActivity().getPackageName()+"/"+R.raw.video1);
        shortsObject7.setTitle("xxxx");
        shortsObject7.setDescription("xxxx");
        shortsObjectList.add(shortsObject7);

        shortsViewPager = rootView.findViewById(R.id.shortsViewPager);
        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        shortsAdapter = new ShortsAdapter(shortsObjectList,context);
        shortsViewPager.setAdapter(shortsAdapter);

        shortsViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                shortsAdapter.pauseVideo(shortsAdapter.getCurrentPosition());
                shortsAdapter.playVideo(position);
                //Log.e("Selected_Page", String.valueOf(shortsAdapter.getCurrentPosition()));
                shortsAdapter.updateCurrentPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        bottomNavigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remove the listener to prevent multiple calls
                bottomNavigationView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // Get the height of the BottomNavigationView
                int bottomNavHeight = bottomNavigationView.getHeight();
                Log.d("BottomNavHeight", "Height: " + bottomNavHeight);
                // Set bottom padding for the ViewPager2
                shortsViewPager.setPadding(0, 0, 0, bottomNavHeight);
            }
        });

        shortsViewPager.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {

            }

            @Override
            public void onViewDetachedFromWindow(View view) {
//                Log.i("position", viewPager2.getVerticalScrollbarPosition() + "");
                shortsAdapter.pauseVideo(shortsAdapter.getCurrentPosition());

            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    // Exit the app
                    shortsAdapter.stopAllVideo();
                    requireActivity().finish();
                } else {
                    moveToNextScroll();
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
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return rootView;
    }
    @Override public void onStart() {
        super.onStart();
    }
    @Override
    public void onPause() {
        super.onPause();
        pauseVideo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        continueVideo();

    }

    public void pauseVideo() {
        SharedPreferences currentPosPref = getActivity().getSharedPreferences("position", Context.MODE_PRIVATE);
        SharedPreferences.Editor positionEditor = currentPosPref.edit();
        int currentPosition = shortsAdapter.getCurrentPosition();
        positionEditor.putInt("position", currentPosition);
        shortsAdapter.pauseVideo(currentPosition);
        positionEditor.apply();
    }

    public void continueVideo() {
        SharedPreferences currentPosPref = getActivity().getSharedPreferences("position", Context.MODE_PRIVATE);
        int currentPosition = currentPosPref.getInt("position", -1);
        if (currentPosition != -1) {
            shortsAdapter.playVideo(currentPosition);
        }
    }
    private void moveToNextScroll() {
        int currentItem = shortsViewPager.getCurrentItem();
        if (currentItem < shortsAdapter.getItemCount() - 1 && currentItem+1 <shortsAdapter.getViewsCount()) {
            shortsViewPager.setCurrentItem(currentItem + 1, true); // Move to the next scroll
        } else {
            // Do something else, or handle reaching the end of the scrolls
        }
    }
}