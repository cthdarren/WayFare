package com.example.wayfare.Activity.settings;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wayfare.Activity.MainActivity;
import com.example.wayfare.R;
import com.example.wayfare.Utils.AuthHelper;
import com.example.wayfare.Utils.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GeneralSettingsActivity extends AppCompatActivity {

    ImageView privacy_back;
    LinearLayout currencyBtn;
    TextView currencyText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings_general);

        privacy_back = findViewById(R.id.privacy_back);
        currencyBtn = findViewById(R.id.currency_button);
        currencyText = findViewById(R.id.currency_text);

        final String[] listItems = Helper.currencies.toArray(new String[0]);
        final int[] checkedItem = {new AuthHelper(this).getSharedPrefsValueInt("CURRENCYIDX")};
        currencyText.setText(listItems[checkedItem[0]]);
        currencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GeneralSettingsActivity.this)
                            .setTitle("Select preferred currency")
                            .setSingleChoiceItems(listItems, checkedItem[0], (dialog, which) -> {
                                new AuthHelper(GeneralSettingsActivity.this).setSharedPrefsValueInt("CURRENCYIDX", which);
                                new AuthHelper(GeneralSettingsActivity.this).setSharedPrefsValue("CURRENCYNAME", listItems[which]);
                                checkedItem[0] = which;
                                currencyText.setText(listItems[which]);
                                dialog.dismiss();
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                            });
                    AlertDialog languagesDialog = builder.create();
                    languagesDialog.show();
            }
        });
        privacy_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}