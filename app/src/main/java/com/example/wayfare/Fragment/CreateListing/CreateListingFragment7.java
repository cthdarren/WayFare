package com.example.wayfare.Fragment.CreateListing;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthHelper;
import com.example.wayfare.Utils.Helper;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Currency;

public class CreateListingFragment7 extends Fragment {

    Button continue_button;
    TextInputEditText localPrice;

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
                Helper.goToFragmentSlideInRightArgs(args, getParentFragmentManager(), R.id.container, new CreateListingFragmentSuccess());
                continue_button.setEnabled(true);
            }
        });
        return view;
    }

}