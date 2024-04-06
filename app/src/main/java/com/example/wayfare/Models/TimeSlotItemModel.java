package com.example.wayfare.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TimeSlotItemModel implements Parcelable {
    public final int startTime;
    public final int endTime;

    public TimeSlotItemModel(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    protected TimeSlotItemModel(Parcel in){
        startTime = in.readInt();
        endTime = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(startTime);
        dest.writeInt(endTime);
    }

    public static final Creator<TimeSlotItemModel> CREATOR = new Creator<TimeSlotItemModel>() {
        @Override
        public TimeSlotItemModel createFromParcel(Parcel in) {
            return new TimeSlotItemModel(in);
        }

        @Override
        public TimeSlotItemModel[] newArray(int size) {
            return new TimeSlotItemModel[size];
        }
    };
}

