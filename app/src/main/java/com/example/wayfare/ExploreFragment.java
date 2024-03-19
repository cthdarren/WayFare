package com.example.wayfare;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {

    private ViewPager2 viewPager2;
    private List<ShortsData> shortsDataList;
    private ShortsAdapter shortsAdapter;

    public ExploreFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        shortsDataList = new ArrayList<>();
        viewPager2 = rootView.findViewById(R.id.viewPager2);

        shortsDataList.add(new ShortsData("android.resource://"+ getActivity().getPackageName() + "/" + R.id.Media1, "@ChinaTownGuideSG", "Guide to chinatown", R.drawable.outline_person_24));
    }
}