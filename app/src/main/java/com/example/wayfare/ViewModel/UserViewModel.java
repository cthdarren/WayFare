package com.example.wayfare.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wayfare.Models.UserModel;

public class UserViewModel extends ViewModel {
    private MutableLiveData<UserModel> userLiveData = new MutableLiveData<>();
    public LiveData<UserModel> getUser(){
        return userLiveData;
    }

    public UserViewModel(){

    }
}
