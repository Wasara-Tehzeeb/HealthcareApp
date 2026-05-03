package com.example.healthcareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class SignupActivity extends AppCompatActivity {
    EditText etName, etEmail, etPass, etConfirm;
    MaterialButton btnLogin;
    TextView btnGoToLogin;

    private static final String PREF_NAME = "HealthCarePrefs";
    private static final String USERS_KEY = "users_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etConfirm = findViewById(R.id.etConfirm);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);

        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSignUp();
            }
        });

    }

    private boolean isEmailExists(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String existingUsers = sharedPreferences.getString(USERS_KEY, "");

        if (existingUsers.isEmpty()) return false;

        String[] users = existingUsers.split(";");
        for (String user : users) {
            String[] userData = user.split(",");
            if (userData.length >= 2 && userData[1].trim().equalsIgnoreCase(email.trim())) {
                return true;
            }
        }
        return false;
    }

    private boolean isPasswordExists(String password) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String existingUsers = sharedPreferences.getString(USERS_KEY, "");

        if (existingUsers.isEmpty()) return false;

        String[] users = existingUsers.split(";");
        for (String user : users) {
            String[] userData = user.split(",");
            if (userData.length >= 3 && userData[2].equals(password)) {
                return true;
            }
        }
        return false;
    }

    private void saveUser(String name, String email, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String existingUsers = sharedPreferences.getString(USERS_KEY, "");
        String newUser = name + "," + email + "," + password;

        if (existingUsers.isEmpty()) {
            editor.putString(USERS_KEY, newUser);
        }
        else {
            editor.putString(USERS_KEY, existingUsers + ";" + newUser);
        }
        editor.apply();
    }

    private void saveCurrentUserSession(String name, String email) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("currentUserName", name);
        editor.putString("currentUserEmail", email);
        editor.apply();
    }

    private void validateAndSignUp() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPass.getText().toString().trim();
        String confirmPassword = etConfirm.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            etName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            etPass.requestFocus();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            etPass.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show();
            etConfirm.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            etConfirm.requestFocus();
            return;
        }

        if (isEmailExists(email)) {
            Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return;
        }

        if (isPasswordExists(password)) {
            Toast.makeText(this, "Password already exists. Use another password", Toast.LENGTH_SHORT).show();
            etPass.requestFocus();
            return;
        }

        saveUser(name, email, password);
        saveCurrentUserSession(name, email);

        Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}