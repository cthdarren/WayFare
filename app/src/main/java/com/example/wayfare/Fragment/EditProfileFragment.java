package com.example.wayfare.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wayfare.BuildConfig;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.UserModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthService;
import com.example.wayfare.Utils.AzureStorageManager;
import com.example.wayfare.Utils.Helper;
import com.example.wayfare.ViewModel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditProfileFragment extends Fragment {

    UserViewModel userViewModel;
    Button save_changes;
    CardView open_gallery;
    ImageView register_back;
    EditText languages, bio;
    ImageView profile_picture;
    TextInputLayout languagesspokenwrapper;
    Uri pictureUri;
    boolean pictureChanged;
    String pictureUrl;
    UserModel userData;

    ActivityResultLauncher<String> getPic = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri o) {
            pictureUri = o;
            profile_picture.setImageURI(pictureUri);
            pictureChanged = true;
        }
    });

    public EditProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        userData = userViewModel.getUserProfileData();

        open_gallery = view.findViewById(R.id.profile_picture_wrapper);
        register_back = view.findViewById(R.id.register_back);
        save_changes = view.findViewById(R.id.save_changes);
        profile_picture = view.findViewById(R.id.profile_picture);
        languagesspokenwrapper = view.findViewById(R.id.languages_spoken_wrapper);
        languages = view.findViewById(R.id.languages_spoken);
        bio = view.findViewById(R.id.about_me);

        bio.setText(userData.getAboutMe());

        if (!Objects.equals(userData.getPictureUrl(), "") & userData.getPictureUrl() != null)
            profile_picture.setImageURI(Uri.parse(userData.getPictureUrl()));

        final String[] listItems = Helper.languages.toArray(new String[0]);
        final boolean[] checkedItems = new boolean[listItems.length];

        final List<String> selectedItems = Arrays.asList(listItems);

        if (userData.getLanguagesSpoken() != null) {
            for (String language : userData.getLanguagesSpoken()) {
                for (int i = 0; i < listItems.length; i++) {
                    if (Objects.equals(language, listItems[i])) {
                        checkedItems[i] = true;
                    }
                }
            }
        }
        register_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_changes.setEnabled(false);
                if (pictureChanged) {
                    uploadPicture(new AuthService.ResponseListener() {
                        @Override
                        public void onError(String message) {
                            makeToast(message);
                            save_changes.setEnabled(true);
                        }

                        @Override
                        public void onResponse(ResponseModel json) {
                            sendRequest();
                        }
                    });
                } else {
                    pictureUrl = userData.getPictureUrl();
                    save_changes.setEnabled(true);
                    sendRequest();
                }
            }
        });
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                getPic.launch("image/*");
                profile_picture.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        });
        languages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("Select spoken languages")
                        .setMultiChoiceItems(listItems, checkedItems, ((dialog, which, isChecked) -> {
                            checkedItems[which] = isChecked;
                        }))
                        .setCancelable(false)
                        .setPositiveButton("Done", (dialog, which) -> {
                            ArrayList<String> test = new ArrayList<>();
                            for (int i = 0; i < checkedItems.length; i++) {
                                if (checkedItems[i]) {
                                    String currentItem = listItems[i];
                                    if (currentItem.charAt(currentItem.length() - 1) == ' ')
                                        currentItem = currentItem.substring(0, currentItem.length() - 1);
                                    test.add(currentItem);
                                }
                            }
                            languages.setText(String.join(", ", test));
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                        })
                        .setNeutralButton("Clear", (dialog, which) -> {
                            Arrays.fill(checkedItems, false);
                            languages.setText("");
                        });
                AlertDialog languagesDialog = builder.create();
                languagesDialog.show();
            }
        });

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Looper uiLooper = Looper.getMainLooper();
        final Handler handler = new Handler(uiLooper);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String picUrl = userData.getPictureUrl();
                    URL url = new URL(picUrl);
                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            profile_picture.setImageBitmap(image);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }


    public void makeToast(String msg) {

        if (getActivity() == null) {
            Log.d("ERROR", "ACTIVITY CONTEXT IS NULL, UNABLE TO MAKE TOAST");
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void sendRequest(){
        String json = String.format("""
                            {"pictureUrl":"%s","aboutMe":"%s", "languagesSpoken":%s}""", pictureUrl, bio.getText().toString(), new Gson().toJson(languages.getText().toString().split(",")));
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        new AuthService(getContext()).getResponse("/profile/edit", true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
            @Override
            public void onError(String message) {
                makeToast(message);
            }

            @Override
            public void onResponse(ResponseModel json) {
                if (json.success){
                    makeToast("Successfully updated profile information!");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            userData.setPictureUrl(pictureUrl);
                            userData.setAboutMe(bio.getText().toString());
                            userData.setLanguagesSpoken(Arrays.asList(languages.getText().toString().split(",")));
                            userViewModel.updateUserData(userData);
                            save_changes.setEnabled(true);
                        }
                    });
                    ProfileFragment pf = new ProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("username", userData.getUsername());
                    pf.setArguments(bundle);
                    getParentFragmentManager().popBackStack();
                    getParentFragmentManager().popBackStack();
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.container, pf)
                            .addToBackStack(pf.getClass().getName())
                            .commit();
                }
                else
                    makeToast(json.data.getAsString());
            }
        });
    }
    public void uploadPicture(AuthService.ResponseListener callback){
        AzureStorageManager.uploadBlob(getContext(), pictureUri, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError("Failed to upload picture to server");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                pictureUrl = response.request().url().toString();
                callback.onResponse(null);
            }
        });
    }
}
