package com.example.wayfare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wayfare.Fragment.SignUpFragment;
import com.example.wayfare.R;

public class UserLanding extends AppCompatActivity {

    Button sign_in, sign_up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_landing);

    }

    public void goToSignUp(View view) {
        Intent intent = new Intent(UserLanding.this, SignUpFragment.class);
        startActivity(intent);
        finish();
    }
    public void goToSignIn(View view) {
        Intent intent = new Intent(UserLanding.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}