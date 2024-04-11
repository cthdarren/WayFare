package com.example.wayfare.Fragment.CreateListing;

import static android.content.Context.INPUT_METHOD_SERVICE;

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
import android.view.inputmethod.InputMethodManager;
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
import com.example.wayfare.Utils.AuthService;
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
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    View curr = getActivity().getCurrentFocus();
                    curr.clearFocus();
                    inputMethodManager.hideSoftInputFromWindow(curr.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                catch (NullPointerException ignored){}

                Double usdPrice = Helper.exchangeToUsd(
                        Integer.parseInt(localPrice.getText().toString().substring(currencyPrefix.length())),
                        new AuthHelper((getContext())).getSharedPrefsCurrencyName()
                );
                continue_button.setEnabled(false);

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
                Log.i("LocationString", locationString);
                String locationY = locationString.split(",")[0].substring(10, locationString.split(",")[0].length()-1);
                String locationX = locationString.split(",")[1].substring(0, locationString.split(",")[1].length()-1);

                final OkHttpClient client = new OkHttpClient();
                // TODO Complete JSON string
                String json = String.format("{\"title\": \"%s\", \"description\": \"%s\", \"thumbnailUrls\": %s, \"category\": \"%s\", \"location\": {\"y\": %s,\"x\": %s}, \"timeRangeList\": %s, \"price\": %s, \"maxPax\": %s, \"minPax\": %s }",
                        title, description, new Gson().toJson(thumbnailUrls), category, locationY, locationX, timeSlotItemModelList.toString(), usdPrice, maxPax, minPax);

                RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
                new AuthService(getContext()).getResponse("/wayfarer/listing/create", true, Helper.RequestType.REQ_POST, body, new AuthService.ResponseListener() {
                    @Override
                    public void onError(String message) {
                        makeToast(message);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                continue_button.setEnabled(true);
                            }
                        });
                    }

                    @Override
                    public void onResponse(ResponseModel json) {
                        Helper.goToFragmentSlideInRight(getParentFragmentManager(), R.id.container, new CreateListingFragmentSuccess());
                    }
                });

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
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}