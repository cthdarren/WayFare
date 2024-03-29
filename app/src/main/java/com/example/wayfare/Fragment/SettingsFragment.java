package com.example.wayfare.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

public class SettingsFragment extends Fragment implements RecyclerViewInterface {

    private RecyclerView settingsRecyclerView;
    private Button logoutBtn;
    private ImageView user_profile_pic;
    private TextView user_greeting;
    private BottomNavigationView navBar;
    private UserViewModel userViewModel;
    private ProgressBar progBar;
    private LinearLayout route_to_profile;
    private LinearLayout become_wayfarer;
    private List<SettingItemModel> settingItemModels = new ArrayList<>();

    private void setupSettingItems(Context context) {

        settingItemModels = Arrays.asList(
                new SettingItemModel("General", context.getDrawable(R.drawable.settings_icon), GeneralSettingsActivity.class),
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
        logoutBtn = view.findViewById(R.id.logoutBtn);
        route_to_profile = view.findViewById(R.id.route_to_profile);
        become_wayfarer = view.findViewById(R.id.become_wayfarer);
        progBar = getActivity().findViewById(R.id.progressBar);
        progBar.setVisibility(View.VISIBLE);
        navBar = getActivity().findViewById(R.id.bottomNavigationView);

        become_wayfarer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.goToFullScreenFragmentFromBottom(getParentFragmentManager(), new BecomeWayfarerFragment());
            }
        });
        route_to_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.goToFragment(getParentFragmentManager(), new ProfileFragment());
                progBar.setVisibility(View.VISIBLE);
                navBar.setVisibility(View.GONE);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AuthHelper(getActivity().getApplicationContext()).logout();
                getActivity().recreate();
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

        user_greeting.setText("Hi, " + userFirstName);

        // Setting up of recycler view and items
        setupSettingItems(view.getContext());

        settingsRecyclerView = view.findViewById(R.id.settings_list_recview);
        SettingsRecViewAdapter adapter = new SettingsRecViewAdapter(view.getContext(), settingItemModels, this);
        settingsRecyclerView.setAdapter(adapter);
        settingsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        if (!Objects.equals(picUrl, "") & picUrl != null) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Looper uiLooper = Looper.getMainLooper();
            final Handler handler = new Handler(uiLooper);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(picUrl);
                        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                user_profile_pic.setImageBitmap(image);
                                progBar.setVisibility(View.GONE);

                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        progBar.setVisibility(View.GONE);
                    }
                }
            });
        }
        else{
            user_profile_pic.setBackgroundResource(R.drawable.account_circle);
            progBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), settingItemModels.get(position).activity);
        startActivity(intent);
    }
}
