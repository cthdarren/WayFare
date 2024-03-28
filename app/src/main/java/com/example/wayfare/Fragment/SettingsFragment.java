package com.example.wayfare.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wayfare.Activity.settings.AccessibilitySettingsActivity;
import com.example.wayfare.Activity.settings.GeneralSettingsActivity;
import com.example.wayfare.Activity.settings.NotificationSettingsActivity;
import com.example.wayfare.Activity.settings.PaymentSettingsActivity;
import com.example.wayfare.Activity.settings.PrivacySettingsActivity;
import com.example.wayfare.Activity.settings.ReportSettingsActivity;
import com.example.wayfare.Adapters.SettingsRecViewAdapter;
import com.example.wayfare.Models.UserModel;
import com.example.wayfare.R;
import com.example.wayfare.RecyclerViewInterface;
import com.example.wayfare.Models.SettingItemModel;
import com.example.wayfare.Utils.AuthHelper;
import com.example.wayfare.ViewModel.UserViewModel;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsFragment extends Fragment implements RecyclerViewInterface {

    private RecyclerView settingsRecyclerView;
    private Button logoutBtn;
    private ImageView user_profile_pic;
    private TextView user_greeting;
    private UserViewModel userViewModel;
    private List<SettingItemModel> settingItemModels = new ArrayList<>();

    private void setupSettingItems(Context context){
        settingItemModels = Arrays.asList(
        new SettingItemModel("Privacy", context.getDrawable(R.drawable.privacy), PrivacySettingsActivity.class),
        new SettingItemModel("General", context.getDrawable(R.drawable.settings_icon), GeneralSettingsActivity.class),
        new SettingItemModel("Accessibility", context.getDrawable(R.drawable.accessibility), AccessibilitySettingsActivity.class),
        new SettingItemModel("Notifications", context.getDrawable(R.drawable.notifications), NotificationSettingsActivity.class),
        new SettingItemModel("Payments", context.getDrawable(R.drawable.payment), PaymentSettingsActivity.class),
        new SettingItemModel("Report a Problem", context.getDrawable(R.drawable.report), ReportSettingsActivity.class)
        );
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setupSettingItems(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        logoutBtn = view.findViewById(R.id.logoutBtn);
        user_greeting = view.findViewById(R.id.user_greeting);
        user_profile_pic = view.findViewById(R.id.user_profile_picture);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        UserModel userData = userViewModel.getUserProfileData();
        String picUrl = userData.getPictureUrl();
        String userFirstName = userData.getFirstName();

        user_greeting.setText("Hi, " +  userFirstName);
        if (picUrl != "" & picUrl != null) {
//            Glide.with(view.getContext())
//                    .load(picUrl).into(user_profile_pic);
        }
        //TODO LOAD THE DAMN PIC

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AuthHelper(getActivity().getApplicationContext()).logout();
                getActivity().recreate();
            }
        });

        settingsRecyclerView = view.findViewById(R.id.settings_list_recview);
        SettingsRecViewAdapter adapter = new SettingsRecViewAdapter(view.getContext(), settingItemModels, this);
        settingsRecyclerView.setAdapter(adapter);
        settingsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return view;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), settingItemModels.get(position).activity);
        startActivity(intent);
    }
}
