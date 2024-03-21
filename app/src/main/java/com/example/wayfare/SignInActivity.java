package com.example.wayfare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

public class SignInActivity extends AppCompatActivity {

    Button sign_in_button;
    EditText et_usernamelog, et_passwordlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        et_usernamelog = findViewById(R.id.usernamelog);
        et_passwordlog = findViewById(R.id.passwordlog);
        sign_in_button = findViewById(R.id.sign_in_button);

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUser();
            }
        });

    }

    public void authenticateUser(){

        RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
        // URL to POST
        String url = "http://143.198.223.202/api/auth/login";

        // Set Parameters:
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("usernamelog", et_usernamelog.getText().toString());
        params.put("passwordlog", et_passwordlog.getText().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String firstName = (String) jsonObject.get("firstName");
                    String lastName = (String) jsonObject.get("lastName");
                    String email = (String) jsonObject.get("email");

                    Intent goToProfile = new Intent(SignInActivity.this, ProfileActivity.class);
                    goToProfile.putExtra("firstName", firstName);
                    goToProfile.putExtra("lastName", lastName);
                    goToProfile.putExtra("email", email);



                    startActivity(goToProfile);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SignInActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(jsonObjectRequest);

    }




}