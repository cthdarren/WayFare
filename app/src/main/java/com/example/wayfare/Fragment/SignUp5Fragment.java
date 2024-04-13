package com.example.wayfare.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wayfare.Activity.MainActivity;
import com.example.wayfare.BuildConfig;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthHelper;
import com.example.wayfare.Utils.AzureStorageManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUp5Fragment extends Fragment {

    Button continue_button, open_gallery;
    BottomNavigationView navBar;
    ImageView register_back;
    ImageView profile_picture;
    Uri pictureUri;
    String username, email, password, verifyPassword, firstName, lastName, phoneNumber, languages, bio, pictureUrl;

    ActivityResultLauncher<String> getPic = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri o) {
            pictureUri = o;
            profile_picture.setImageURI(pictureUri);
            continue_button.setEnabled(true);
        }
    });

    public SignUp5Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up5, container, false);
        navBar = getActivity().findViewById(R.id.bottomNavigationView);
        navBar.setVisibility(View.INVISIBLE);
        open_gallery = view.findViewById(R.id.open_gallery);
        register_back = view.findViewById(R.id.register_exit);
        continue_button = view.findViewById(R.id.complete_sign_up);
        profile_picture = view.findViewById(R.id.profile_picture);

        register_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continue_button.setEnabled(false);
                if (pictureUri == null){
                    try {
                        register();
                    } catch (IOException e) {
                        makeToast("Failed to create account");
                        continue_button.setEnabled(true);
                    }
                }
                else {
                    AzureStorageManager.uploadBlob(getContext(), pictureUri, true, new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    makeToast("Failed to upload picture to server");
                                    continue_button.setEnabled(true);
                                }
                            });
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pictureUrl = response.request().url().toString();
                                    Bundle args = new Bundle();
                                    args.putAll(getArguments());
                                    try {
                                        register();
                                    } catch (IOException e) {
                                        makeToast("Unexpected Error");
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continue_button.setEnabled(false);
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                getPic.launch("image/*");
                profile_picture.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        });

        return view;
    }


    public void register() throws IOException {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    username = getArguments().getString("username");
                    email = getArguments().getString("email");
                    password = getArguments().getString("password");
                    verifyPassword = getArguments().getString("verifypassword");
                    firstName = getArguments().getString("firstname");
                    lastName = getArguments().getString("lastname");
                    phoneNumber = getArguments().getString("phonenumber");
                    bio = getArguments().getString("bio");
                    languages = getArguments().getString("languages");

                    final OkHttpClient client = new OkHttpClient();
                    String json = String.format("{\"username\":\"%s\", \"firstName\":\"%s\", \"lastName\":\"%s\", \"email\":\"%s\", \"phoneNumber\":\"%s\", \"password\":\"%s\", \"verifyPassword\":\"%s\", \"aboutMe\":%s,\"languagesSpoken\":%s,\"pictureUrl\":\"%s\" }", username, firstName, lastName, email, phoneNumber, password, verifyPassword, new Gson().toJson(bio), new Gson().toJson(languages.trim().split(",")), pictureUrl);

                     RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
                    Request request = new Request.Builder().url(BuildConfig.API_URL + "/api/v1/auth/register")
                            .post(body)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            if (e instanceof SocketTimeoutException) {
                                makeToast("Request Timed Out");
                                e.printStackTrace();
                            } else if (e instanceof SocketException) {
                                makeToast("Server Error");
                                Log.d("ERROR", "CHECK IF BACKEND SERVER IS RUNNING!");
                                e.printStackTrace();
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    continue_button.setEnabled(true);
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.code() == 200) {
                                ResponseModel res = new Gson().fromJson(response.body().string(), ResponseModel.class);
                                if (res.success) {
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("user_info", res.data.getAsString())
                                            .apply();

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            continue_button.setEnabled(true);
                                            Bundle args = getArguments();
                                            SignUpSuccessFragment fragment = new SignUpSuccessFragment();
                                            fragment.setArguments(args);
                                            getParentFragmentManager().beginTransaction()
                                                    .replace(R.id.container, fragment)
                                                    .addToBackStack(null)
                                                    .setReorderingAllowed(true)
                                                    .commit();
                                        }
                                    });
                                }else {
                                    makeToast("Server Error");
                                }

                            } else {
                                makeToast("Server Error");
                            }
                        }

                    });
                }
       /* FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft
                .replace(, new ExploreFragment())
                .setReorderingAllowed(true)
                .addToBackStack("name") // Name can be null
                .commit();*/
            });
        }
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
}
