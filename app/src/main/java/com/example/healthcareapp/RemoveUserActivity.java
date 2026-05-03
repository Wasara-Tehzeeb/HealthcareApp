package com.example.healthcareapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RemoveUserActivity extends AppCompatActivity {

    private static final String PREF_NAME = "HealthCarePrefs";
    private static final String USERS_KEY = "users_data";
    private static final String ADMIN_EMAIL = "wasara@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_user);

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());

        showUserList();
    }

    private void showUserList() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String existingUsers = prefs.getString(USERS_KEY, "");

        if (existingUsers.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("No Users")
                    .setMessage("No users available to remove.")
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .setOnCancelListener(dialog -> finish())
                    .show();
            return;
        }

        String[] users = existingUsers.split(";");
        String[] displayNames = new String[users.length];
        String[] emails = new String[users.length];
        int validCount = 0;

        for (String user : users) {
            String[] userData = user.split(",");
            if (userData.length >= 3) {
                String email = userData[1].trim();
                if (email.equalsIgnoreCase(ADMIN_EMAIL)) {
                    continue;
                }
                displayNames[validCount] = userData[0].trim() + "\n" + email;
                emails[validCount] = email;
                validCount++;
            }
        }

        if (validCount == 0) {
            new AlertDialog.Builder(this)
                    .setTitle("No Users")
                    .setMessage("No users available to remove.")
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .setOnCancelListener(dialog -> finish())
                    .show();
            return;
        }

        String[] finalNames = new String[validCount];
        String[] finalEmails = new String[validCount];
        System.arraycopy(displayNames, 0, finalNames, 0, validCount);
        System.arraycopy(emails, 0, finalEmails, 0, validCount);

        new AlertDialog.Builder(this)
                .setTitle("Select User to Remove")
                .setItems(finalNames, (dialog, which) -> {
                    String selectedEmail = finalEmails[which];
                    String selectedName = finalNames[which].split("\n")[0];
                    confirmRemoval(selectedName, selectedEmail);
                })
                .setNegativeButton("Back", (dialog, which) -> finish())
                .setOnCancelListener(dialog -> finish())
                .show();
    }

    private void confirmRemoval(String userName, String userEmail) {
        new AlertDialog.Builder(this)
                .setTitle("Remove User")
                .setMessage("Are you sure you want to remove " + userName + "? All their data including schedules and notifications will be permanently deleted.")
                .setPositiveButton("Yes, Remove", (dialog, which) -> {
                    removeUser(userEmail);
                    Toast.makeText(this, userName + " has been removed", Toast.LENGTH_SHORT).show();
                    showUserList();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void removeUser(String emailToRemove) {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String existingUsers = prefs.getString(USERS_KEY, "");

        if (existingUsers.isEmpty()) return;

        String[] users = existingUsers.split(";");
        StringBuilder updatedUsers = new StringBuilder();

        for (String user : users) {
            String[] userData = user.split(",");
            if (userData.length >= 3 && !userData[1].trim().equalsIgnoreCase(emailToRemove)) {
                if (updatedUsers.length() > 0) updatedUsers.append(";");
                updatedUsers.append(user);
            }
        }

        prefs.edit().putString(USERS_KEY, updatedUsers.toString()).apply();

        removeUserSchedules(emailToRemove);
        removeUserNotifications(emailToRemove);
        removeUserHealthData(emailToRemove);
        removeUserPrescriptions(emailToRemove);
    }

    private void removeUserSchedules(String email) {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove("schedules_" + email).apply();
    }

    private void removeUserNotifications(String email) {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove("notifications_" + email).apply();
    }

    private void removeUserHealthData(String email) {
        SharedPreferences prefs = getSharedPreferences("HealthAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("health_dob_" + email);
        editor.remove("health_blood_" + email);
        editor.remove("health_allergy_" + email);
        editor.apply();
    }

    private void removeUserPrescriptions(String email) {
        SharedPreferences prefs = getSharedPreferences("HealthAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("prescription_" + email);
        editor.remove("diet_plan_" + email);
        editor.apply();
    }
}