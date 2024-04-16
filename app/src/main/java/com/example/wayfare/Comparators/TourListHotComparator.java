package com.example.wayfare.Comparators;

import com.example.wayfare.Models.TourListModel;

import java.util.Comparator;

public class TourListHotComparator implements Comparator<TourListModel> {
    @Override
    public int compare(TourListModel o1, TourListModel o2) {
        return Double.compare(o2.getWeightedScore(), o1.getWeightedScore());
    }
}
