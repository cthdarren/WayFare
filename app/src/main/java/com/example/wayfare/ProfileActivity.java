package com.example.wayfare;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileActivity extends AppCompatActivity {

    TextView tv_firstName, tv_lastName, tv_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        tv_firstName = findViewById(R.id.firstName);
        tv_lastName = findViewById(R.id.lastName);
        tv_email = findViewById(R.id.email);

        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        String email = getIntent().getStringExtra("email");

        tv_firstName.setText(firstName);
        tv_lastName.setText(lastName);
        tv_email.setText(email);

    }
}