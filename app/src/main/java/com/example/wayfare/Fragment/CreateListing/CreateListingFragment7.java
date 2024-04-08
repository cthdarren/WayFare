package com.example.wayfare.Fragment.CreateListing;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.wayfare.BuildConfig;
import com.example.wayfare.Fragment.SignUpSuccessFragment;
import com.example.wayfare.Models.ResponseModel;
import com.example.wayfare.Models.TimeSlotItemModel;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthHelper;
import com.example.wayfare.Utils.Helper;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateListingFragment7 extends Fragment {

    Button continue_button;
    TextInputEditText localPrice;
    String title, description, category, locationString, locationName,
    locationAddress, minPax, maxPax;
    List<TimeSlotItemModel> timeSlotItemModelList;
    List<String> thumbnailUrls = new ArrayList<String>();

    public CreateListingFragment7() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing_create7, container, false);
        continue_button = view.findViewById(R.id.continue_button);
        localPrice = view.findViewById(R.id.usdPrice);
        Currency currency = Currency.getInstance(new AuthHelper(getContext()).getSharedPrefsCurrencyName());
        String currencyPrefix = currency.getSymbol();
        localPrice.setText(currencyPrefix);
        continue_button.setEnabled(false);

        localPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith(currencyPrefix)){
                    localPrice.setText(currencyPrefix);
                    Selection.setSelection(localPrice.getText(), localPrice.getText().length());
                }
                else {
                    if (s.toString().substring(currencyPrefix.length(), s.length()).length() == 0) {
                        continue_button.setEnabled(false);
                    } else
                        continue_button.setEnabled(true);
                }
            }
        });

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localPrice.clearFocus();
                Bundle args = getArguments();
                Double usdPrice = Helper.exchangeToUsd(
                        Integer.parseInt(localPrice.getText().toString().substring(currencyPrefix.length())),
                        new AuthHelper((getContext())).getSharedPrefsCurrencyName()
                );
                args.putString("price", String.valueOf(usdPrice));
                continue_button.setEnabled(false);
                //Helper.goToFragmentSlideInRightArgs(args, getParentFragmentManager(), R.id.container, new CreateListingFragmentSuccess());
                try {
                    listingCreate();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                continue_button.setEnabled(true);
            }
        });
        return view;
    }

    public void listingCreate() throws IOException {
        if (getActivity()  != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    title = getArguments().getString("title");
                    description = getArguments().getString("description");
                    category = getArguments().getString("category");
                    locationString = getArguments().getString("locationLatLng");
                    locationName = getArguments().getString("locationName");
                    locationAddress = getArguments().getString("locationAddress");
                    minPax = getArguments().getString("minPax");
                    maxPax = getArguments().getString("maxPax");
                    timeSlotItemModelList = getArguments().getParcelableArrayList("timeslots");
                    thumbnailUrls = getArguments().getStringArrayList("thumbnailurls");
                    String locationX = locationString.split(",")[0];
                    String locationY = locationString.split(",")[1];
                    String thumbnailUrlsString;

                    //Log.i("NULL CHECK", Arrays.toString(thumbnailUrls.toArray()));
                    if (thumbnailUrls == null) {
                        thumbnailUrlsString = "[]";
                    } else {
                        thumbnailUrlsString = thumbnailUrls.toString();
                    }

                    final OkHttpClient client = new OkHttpClient();
                    // TODO Complete JSON string
                    String json = String.format("{\"title\":\"%s\", \"description\":\"%s\", \"thumbnailUrls\":\"%s\", \"category\":\"%s\", \"location\": {\"y\":%s,\"x\":%s}, \"timeRangeList\": %s, \"price\":%s, \"maxPax\":%s, \"minPax\":%s}",
                            title, description, thumbnailUrlsString, category, locationY, locationX, timeSlotItemModelList.toString(), localPrice.getText(), maxPax, minPax);
                    Log.i("New TourListing JSON", json);

                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
                    Request request = new Request.Builder().url(BuildConfig.API_URL + "/wayfarer/listing/create").post(body).build();
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

                                            Bundle args = getArguments();
                                            CreateListingFragmentSuccess fragment = new CreateListingFragmentSuccess();
                                            fragment.setArguments(args);
                                            getParentFragmentManager().beginTransaction()
                                                    .replace(R.id.container, fragment)
                                                    .addToBackStack(null)
                                                    .setReorderingAllowed(true)
                                                    .commit();
                                        }
                                    });
                                }else {
                                    makeToast(res.data.getAsString());
                                }

                            } else {
                                makeToast("Server Error");
                            }
                        }
                    });

                }

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