package com.example.healthcareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {
    EditText etName, etEmail, etPass;
    MaterialButton btnLogin;

    private static final String PREF_NAME = "HealthCarePrefs";
    private static final String USERS_KEY = "users_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndLogin();
            }
        });

    }

    private String[] findUser(String name, String email, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String existingUsers = sharedPreferences.getString(USERS_KEY, "");

        if (existingUsers.isEmpty()) {
            return null;
        }

        String[] users = existingUsers.split(";");
        for (String user : users) {
            String[] userData = user.split(",");
            if (userData.length >= 3 &&
                    userData[0].trim().equalsIgnoreCase(name.trim()) &&
                    userData[1].trim().equalsIgnoreCase(email.trim()) &&
                    userData[2].equals(password)) {
                return userData;
            }
        }
        return null;
    }

    private void saveCurrentUserSession(String name, String email) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("currentUserName", name);
        editor.putString("currentUserEmail", email);
        editor.apply();
    }
    private void validateAndLogin() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPass.getText().toString().trim();

        if (name.isEmpty()) {
            showToast("Please enter your name");
            etName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            showToast("Please enter your email");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            showToast("Please enter your password");
            etPass.requestFocus();
            return;
        }

        String[] userData = findUser(name, email, password);
        if (userData == null) {
            showToast("User not found");
            etName.requestFocus();
            return;
        }

        saveCurrentUserSession(name, email);
        showToast("Login successful!");

        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}