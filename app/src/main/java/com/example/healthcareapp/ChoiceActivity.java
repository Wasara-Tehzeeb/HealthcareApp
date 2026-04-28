package com.example.healthcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignup = findViewById(R.id.btnSignup);

        btnLogin.setOnClickListener(v -> {
            Intent loginIntent = new Intent(ChoiceActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        });

        btnSignup.setOnClickListener(v -> {
            Intent signupIntent = new Intent(ChoiceActivity.this, SignupActivity.class);
            startActivity(signupIntent);
            finish();
        });

    }
}