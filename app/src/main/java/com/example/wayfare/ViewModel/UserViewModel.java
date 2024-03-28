package com.example.wayfare.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wayfare.Models.UserModel;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<UserModel> userLiveData = new MutableLiveData<>();
    public UserModel getUserProfileData(){
        return userLiveData.getValue();
    }

    public void updateUserData(UserModel user){
        userLiveData.setValue(user);
    }
}
