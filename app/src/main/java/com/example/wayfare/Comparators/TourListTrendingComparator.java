package com.example.wayfare.Comparators;

import com.example.wayfare.Models.TourListModel;

import java.util.Comparator;

public class TourListTrendingComparator implements Comparator<TourListModel> {
    @Override
    public int compare(TourListModel o1, TourListModel o2) {
        return Double.compare(o2.getRating(), o1.getRating());
    }
}
