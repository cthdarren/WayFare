package com.example.wayfare.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wayfare.Activity.MainActivity;
import com.example.wayfare.Activity.WayfarerActivity;
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
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.ViewModel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SettingsFragment extends Fragment implements RecyclerViewInterface {

    private RecyclerView settingsRecyclerView;
    private Button logoutBtn, verifyButton;
    private ImageView user_profile_pic;
    private TextView user_greeting;
    private BottomNavigationView navBar;
    private UserViewModel userViewModel;
    private MaterialCardView verification_prompt;
    private ProgressBar progBar;
    private LinearLayout route_to_profile;
    private LinearLayout become_wayfarer;
    private List<SettingItemModel> settingItemModels = new ArrayList<>();
    private TextView become_wayfarer_text;
    private ImageView become_wayfarer_icon;

    private void setupSettingItems(Context context) {

        settingItemModels = Arrays.asList(
                new SettingItemModel("General", context.getDrawable(R.drawable.settings_icon), GeneralSettingsActivity.class),
                new SettingItemModel("Account Settings", context.getDrawable(R.drawable.icon_selection_account), new AccountSettingsFragment()),
                new SettingItemModel("Privacy", context.getDrawable(R.drawable.privacy), PrivacySettingsActivity.class),
                new SettingItemModel("Accessibility", context.getDrawable(R.drawable.accessibility), AccessibilitySettingsActivity.class),
                new SettingItemModel("Notifications", context.getDrawable(R.drawable.notifications), NotificationSettingsActivity.class),
                new SettingItemModel("Payments", context.getDrawable(R.drawable.payment), PaymentSettingsActivity.class),
                new SettingItemModel("Report a Problem", context.getDrawable(R.drawable.report), ReportSettingsActivity.class)
        );
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        progBar = view.findViewById(R.id.settingsProgBar);
        progBar.setVisibility(View.VISIBLE);
        navBar = getActivity().findViewById(R.id.bottomNavigationView);

        logoutBtn = view.findViewById(R.id.logoutBtn);
        verifyButton = view.findViewById(R.id.verifybutton);
        route_to_profile = view.findViewById(R.id.route_to_profile);
        become_wayfarer = view.findViewById(R.id.become_wayfarer);
        become_wayfarer_text = view.findViewById(R.id.become_wayfarer_text);
        become_wayfarer_icon = view.findViewById(R.id.become_wayfarer_icon);
        verification_prompt = view.findViewById(R.id.verification_prompt);

        route_to_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle username = new Bundle();
                username.putString("username", userViewModel.getUserProfileData().getUsername());
                ProfileFragment pf = new ProfileFragment();
                pf.setArguments(username);
                Helper.goToFragmentSlideInRight(getParentFragmentManager(), R.id.container, pf);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AuthHelper(getActivity().getApplicationContext()).logout();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.goToFragmentSlideInRight(getParentFragmentManager(), R.id.container, new VerifyAccountFragment());
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user_greeting = view.findViewById(R.id.user_greeting);
        user_profile_pic = view.findViewById(R.id.user_profile_picture);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        UserModel userData = userViewModel.getUserProfileData();
        String picUrl = userData.getPictureUrl();
        String userFirstName = userData.getFirstName();
        if (!userData.isVerified()){
            verification_prompt.setVisibility(View.VISIBLE);
        }
        else{
            verification_prompt.setVisibility(View.GONE);
        }


        Intent intent;
        String wayfarerview;
        if (Objects.equals(userData.getRole(), "ROLE_WAYFARER")){
            if (new AuthHelper(getContext()).getSharedPrefsValue("WAYFARER_VIEW").equals("TRUE")) {
                become_wayfarer_text.setText("Switch to Travelling");
                intent = new Intent(getActivity(), MainActivity.class);
                wayfarerview = "FALSE";
            }
            else {
                become_wayfarer_text.setText("Switch to Hosting");
                intent = new Intent(getActivity(), WayfarerActivity.class);
                wayfarerview = "TRUE";
            }
            become_wayfarer_icon.setImageResource(R.drawable.swap);
            become_wayfarer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AuthHelper(getContext()).setSharedPrefsValue("WAYFARER_VIEW", wayfarerview);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
            });
        }
        else{
            become_wayfarer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helper.goToFullScreenFragmentFromBottom(getParentFragmentManager(), new BecomeWayfarerFragment());
                }
            });
        }
        user_greeting.setText("Hi, " + userFirstName);

        // Setting up of recycler view and items
        setupSettingItems(view.getContext());

        settingsRecyclerView = view.findViewById(R.id.settings_list_recview);
        SettingsRecViewAdapter adapter = new SettingsRecViewAdapter(view.getContext(), settingItemModels, this);
        settingsRecyclerView.setAdapter(adapter);
        settingsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        Glide.with(SettingsFragment.this)
                .load(picUrl)
                .centerCrop()
                .sizeMultiplier(0.6f)
                .into(user_profile_pic);
    }

    @Override
    public void onItemClick(int position) {
//        Helper.goToFullScreenFragmentFromBottom(getParentFragmentManager(), new AccessibilitySettingsFragment());
        if (settingItemModels.get(position).activity != null) {
            Intent intent = new Intent(getActivity(), settingItemModels.get(position).activity);
            startActivity(intent);
        }
        if (settingItemModels.get(position).fragment != null){
            Helper.goToFragmentSlideInRight(getParentFragmentManager(), R.id.container, settingItemModels.get(position).fragment);
        }
    }
}
