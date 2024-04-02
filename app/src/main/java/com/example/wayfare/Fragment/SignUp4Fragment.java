package com.example.wayfare.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.wayfare.R;
import com.example.wayfare.Utils.Helper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp4Fragment extends Fragment {

    Button continue_button;
    EditText languages, bio;
    TextInputLayout languagesspokenwrapper;
    BottomNavigationView navBar;
    ImageView register_back;

    public SignUp4Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String[] listItems = Helper.languages.toArray(new String[0]);
        final boolean[] checkedItems = new boolean[listItems.length];

        final List<String> selectedItems = Arrays.asList(listItems);

        View view = inflater.inflate(R.layout.fragment_sign_up4, container, false);
        navBar = getActivity().findViewById(R.id.bottomNavigationView);
        navBar.setVisibility(View.INVISIBLE);

        languagesspokenwrapper = view.findViewById(R.id.languages_spoken_wrapper);
        languages = view.findViewById(R.id.languages_spoken);
        bio = view.findViewById(R.id.bio);
        register_back = view.findViewById(R.id.register_exit);
        continue_button = view.findViewById(R.id.continue_button);


        register_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    Bundle args = new Bundle();
                    args.putAll(getArguments());
                    args.putString("languages", String.valueOf(languages.getText()));
                    args.putString("bio", String.valueOf(bio.getText()));

                    SignUp5Fragment fragment = new SignUp5Fragment();
                    fragment.setArguments(args);
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment)
                            .addToBackStack(null)
                            .setReorderingAllowed(true)
                            .commit();
                }
            }
        });

        languages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("Select spoken languages")
                        .setMultiChoiceItems(listItems, checkedItems, ((dialog, which, isChecked) -> {
                            checkedItems[which] = isChecked;
                            String currentItem = selectedItems.get(which);
                        }))
                        .setCancelable(false)
                        .setPositiveButton("Done", (dialog, which) -> {
                            ArrayList<String> test = new ArrayList<>();
                            for (int i = 0; i < checkedItems.length; i++) {
                                if (checkedItems[i]) {
                                    String currentItem = selectedItems.get(i);
                                    if (currentItem.charAt(currentItem.length()-1) == ' ')
                                        currentItem = currentItem.substring(0, currentItem.length()-1);
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
        return view;
    }
    public boolean validateFields(){
        boolean result = true;

        if (languages.length() <= 0) {
            result = false;
            languagesspokenwrapper.setErrorEnabled(true);
            languagesspokenwrapper.setError("You must select at least one language");
        }

        return result;
    }
}