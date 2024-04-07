package com.example.wayfare.Fragment.CreateListing;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.wayfare.R;
import com.example.wayfare.Utils.Helper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CreateListingFragment5 extends Fragment {

    Button continue_button;

    TextInputEditText listingDesc, listingTitle;
    TextInputLayout listingDescWrapper, listingTitleWrapper;

    public CreateListingFragment5() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing_create5, container, false);
        continue_button = view.findViewById(R.id.continue_button);
        listingDesc = view.findViewById(R.id.listingDescription);
        listingTitle = view.findViewById(R.id.listingTitle);
        listingTitleWrapper = view.findViewById(R.id.listingTitleWrapper);
        listingDescWrapper = view.findViewById(R.id.listingDescriptionWrapper);

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

                if (listingDesc.getText().length() == 0)
                    listingDescWrapper.setError("Description cannot be empty");
                else
                    listingDescWrapper.setErrorEnabled(false);
                if (listingTitle.getText().length() == 0)
                    listingTitleWrapper.setError("Title cannot be empty");
                else
                    listingTitleWrapper.setErrorEnabled(false);

                if (listingTitle.getText().toString().strip().length() != 0 & listingDesc.getText().toString().strip().length() != 0){
                    Bundle args = getArguments();
                    args.putString("description", listingDesc.getText().toString());
                    args.putString("title", listingTitle.getText().toString());
                    Helper.goToFragmentSlideInRightArgs(args, getParentFragmentManager(), R.id.container, new CreateListingFragment6());
                }
                continue_button.setEnabled(true);
            }
        });
        return view;
    }

}