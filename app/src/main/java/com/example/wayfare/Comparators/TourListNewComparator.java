package com.example.wayfare.Comparators;

import com.example.wayfare.Models.TourListModel;

import java.util.Comparator;

public class TourListNewComparator implements Comparator<TourListModel> {
    @Override
    public int compare(TourListModel o1, TourListModel o2) {
        return o1.getDateCreatedInstant().compareTo(o2.getDateCreatedInstant());
    }
}
