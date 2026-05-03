package com.example.healthcareapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;

public class AdminSettingsFragment extends Fragment {

    private TextView tvUsername, tvEmail, tvPassword;
    private TextView tvDob, tvBloodGroup;
    private TextView tvCurrentTextSize;
    private LinearLayout btnChangeName, btnChangeEmail, btnChangePassword, btnTextSize;
    private LinearLayout btnDob, btnBloodGroup;
    private SwitchMaterial switchDarkMode;
    private MaterialButton btnLogout;

    private static final String PREFS_NAME = "HealthAppPrefs";
    private static final String KEY_DARK_MODE = "isDarkMode";
    private static final String KEY_FONT_SCALE = "fontScale";

    private String KEY_DOB;
    private String KEY_BLOOD_GROUP;

    private static final String AUTH_PREFS = "HealthCarePrefs";
    private static final String USERS_KEY = "users_data";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_settings, container, false);

        tvUsername = view.findViewById(R.id.tv_settings_username);
        tvEmail = view.findViewById(R.id.tv_settings_email);
        tvPassword = view.findViewById(R.id.tv_settings_password);
        btnChangeName = view.findViewById(R.id.btn_change_name);
        btnChangeEmail = view.findViewById(R.id.btn_change_email);
        btnChangePassword = view.findViewById(R.id.btn_change_password);

        tvDob = view.findViewById(R.id.tv_dob);
        tvBloodGroup = view.findViewById(R.id.tv_blood_group);
        btnDob = view.findViewById(R.id.btn_dob);
        btnBloodGroup = view.findViewById(R.id.btn_blood_group);

        switchDarkMode = view.findViewById(R.id.switch_dark_mode);
        tvCurrentTextSize = view.findViewById(R.id.tv_current_text_size);
        btnTextSize = view.findViewById(R.id.btn_text_size);
        btnLogout = view.findViewById(R.id.btn_logout);

        loadUserDataFromSharedPreferences();
        loadHealthProfileData();
        loadSavedPreferences();

        setupClickListeners();

        return view;
    }

    private void loadHealthProfileData() {
        SharedPreferences authPrefs = requireActivity().getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
        String currentEmail = authPrefs.getString("currentUserEmail", "unknown@email.com");

        KEY_DOB = "health_dob_" + currentEmail;
        KEY_BLOOD_GROUP = "health_blood_" + currentEmail;

        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String dob = prefs.getString(KEY_DOB, "");
        String bloodGroup = prefs.getString(KEY_BLOOD_GROUP, "");

        tvDob.setText(dob.isEmpty() ? "Not set" : dob);
        tvBloodGroup.setText(bloodGroup.isEmpty() ? "Not set" : bloodGroup);
    }

    private void loadUserDataFromSharedPreferences() {
        SharedPreferences authPrefs = requireActivity().getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
        String email = authPrefs.getString("currentUserEmail", "");
        String name = authPrefs.getString("currentUserName", "User");
        String password = getPasswordFromEmail(email);

        tvUsername.setText(name);
        tvEmail.setText(email);
        tvPassword.setText(password.isEmpty() ? "Not found" : "******");
    }

    private String getPasswordFromEmail(String email) {
        if (email == null || email.isEmpty()) return "";
        SharedPreferences authPrefs = requireActivity().getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
        String existingUsers = authPrefs.getString(USERS_KEY, "");
        if (existingUsers.isEmpty()) return "";

        String[] users = existingUsers.split(";");
        for (String user : users) {
            String[] userData = user.split(",");
            if (userData.length >= 3 && userData[1].trim().equalsIgnoreCase(email.trim())) {
                return userData[2];
            }
        }
        return "";
    }

    private void loadSavedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        switchDarkMode.setChecked(sharedPreferences.getBoolean(KEY_DARK_MODE, false));
        updateTextSizeLabel(sharedPreferences.getFloat(KEY_FONT_SCALE, 1.0f));
    }

    private void setupClickListeners() {
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
            editor.putBoolean(KEY_DARK_MODE, isChecked);
            editor.apply();
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        btnTextSize.setOnClickListener(v -> showTextSizeDialog());

        btnChangeName.setOnClickListener(v -> showChangeNameDialog());
        btnChangeEmail.setOnClickListener(v -> showChangeEmailDialog());
        btnChangePassword.setOnClickListener(v -> showChangePasswordDialog());

        btnDob.setOnClickListener(v -> showDatePickerDialog());
        btnBloodGroup.setOnClickListener(v -> showBloodGroupDialog());

        btnLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(requireContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
            String formattedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);

            requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                    .edit().putString(KEY_DOB, formattedDate).apply();

            tvDob.setText(formattedDate);
        }, year, month, day).show();
    }

    private void showBloodGroupDialog() {
        String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        int currentSelection = -1;
        String currentGroup = tvBloodGroup.getText().toString();

        for (int i = 0; i < bloodGroups.length; i++) {
            if (bloodGroups[i].equals(currentGroup)) {
                currentSelection = i;
                break;
            }
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Select Blood Group")
                .setSingleChoiceItems(bloodGroups, currentSelection, (dialog, which) -> {
                    String selectedGroup = bloodGroups[which];

                    requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                            .edit().putString(KEY_BLOOD_GROUP, selectedGroup).apply();

                    tvBloodGroup.setText(selectedGroup);
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showTextSizeDialog() {
        String[] sizes = {"Small", "Medium (Default)", "Large"};
        final float[] scaleValues = {0.85f, 1.0f, 1.25f};

        new AlertDialog.Builder(getContext())
                .setTitle("Select Text Size")
                .setItems(sizes, (dialog, which) -> {
                    float selectedScale = scaleValues[which];
                    requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
                            .putFloat(KEY_FONT_SCALE, selectedScale).apply();
                    updateTextSizeLabel(selectedScale);
                    requireActivity().recreate();
                })
                .show();
    }

    private void updateTextSizeLabel(float scale) {
        if (scale == 0.85f) tvCurrentTextSize.setText("Small");
        else if (scale == 1.0f) tvCurrentTextSize.setText("Medium");
        else if (scale == 1.25f) tvCurrentTextSize.setText("Large");
    }

    private void showChangeNameDialog() {
        if (getContext() == null) return;
        SharedPreferences authPrefs = requireActivity().getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
        String currentEmail = authPrefs.getString("currentUserEmail", "");
        EditText etNewName = new EditText(getContext());
        etNewName.setHint("Enter new name");

        new AlertDialog.Builder(getContext()).setTitle("Change Name").setView(etNewName)
                .setPositiveButton("Update", (dialog, which) -> {
                    String newName = etNewName.getText().toString().trim();
                    if (newName.isEmpty()) { Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show(); return; }
                    updateUserInSharedPreferences(currentEmail, 0, newName);
                    authPrefs.edit().putString("currentUserName", newName).apply();
                    tvUsername.setText(newName);
                    Toast.makeText(getContext(), "Name updated", Toast.LENGTH_SHORT).show();
                }).setNegativeButton("Cancel", null).show();
    }

    private void showChangeEmailDialog() {
        if (getContext() == null) return;
        SharedPreferences authPrefs = requireActivity().getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
        String currentEmail = authPrefs.getString("currentUserEmail", "");
        EditText etNewEmail = new EditText(getContext());
        etNewEmail.setHint("Enter new email");
        etNewEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        new AlertDialog.Builder(getContext()).setTitle("Change Email").setView(etNewEmail)
                .setPositiveButton("Update", (dialog, which) -> {
                    String newEmail = etNewEmail.getText().toString().trim();
                    if (newEmail.isEmpty()) { Toast.makeText(getContext(), "Email cannot be empty", Toast.LENGTH_SHORT).show(); return; }
                    if (isEmailTakenBySomeoneElse(newEmail, currentEmail)) { Toast.makeText(getContext(), "Email already exists", Toast.LENGTH_SHORT).show(); return; }
                    updateUserInSharedPreferences(currentEmail, 1, newEmail);
                    authPrefs.edit().putString("currentUserEmail", newEmail).apply();
                    tvEmail.setText(newEmail);
                    Toast.makeText(getContext(), "Email updated", Toast.LENGTH_SHORT).show();
                }).setNegativeButton("Cancel", null).show();
    }

    private boolean isEmailTakenBySomeoneElse(String newEmail, String myCurrentEmail) {
        SharedPreferences authPrefs = requireActivity().getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
        String existingUsers = authPrefs.getString(USERS_KEY, "");
        if (existingUsers.isEmpty()) return false;
        String[] users = existingUsers.split(";");
        for (String user : users) {
            String[] userData = user.split(",");
            if (userData.length >= 2 && userData[1].trim().equalsIgnoreCase(newEmail.trim()) && !userData[1].trim().equalsIgnoreCase(myCurrentEmail.trim())) return true;
        }
        return false;
    }

    private void showChangePasswordDialog() {
        if (getContext() == null) return;
        SharedPreferences authPrefs = requireActivity().getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
        String currentEmail = authPrefs.getString("currentUserEmail", "");
        String currentStoredPassword = getPasswordFromEmail(currentEmail);

        LinearLayout dialogLayout = new LinearLayout(getContext());
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setPadding(50, 40, 50, 10);
        final EditText etCurrentPass = new EditText(getContext());
        etCurrentPass.setHint("Current Password");
        etCurrentPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        dialogLayout.addView(etCurrentPass);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 20;
        final EditText etNewPass = new EditText(getContext());
        etNewPass.setHint("New Password");
        etNewPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etNewPass.setLayoutParams(params);
        dialogLayout.addView(etNewPass);
        final EditText etConfirmPass = new EditText(getContext());
        etConfirmPass.setHint("Confirm New Password");
        etConfirmPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etConfirmPass.setLayoutParams(params);
        dialogLayout.addView(etConfirmPass);

        new AlertDialog.Builder(getContext()).setTitle("Change Password").setView(dialogLayout)
                .setPositiveButton("Update", (dialog, which) -> {
                    String enteredCurrentPass = etCurrentPass.getText().toString().trim();
                    String newPass = etNewPass.getText().toString().trim();
                    String confirm = etConfirmPass.getText().toString().trim();
                    if (enteredCurrentPass.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) { Toast.makeText(getContext(), "All fields required", Toast.LENGTH_SHORT).show(); }
                    else if (!enteredCurrentPass.equals(currentStoredPassword)) { Toast.makeText(getContext(), "Incorrect current password", Toast.LENGTH_SHORT).show(); }
                    else if (newPass.length() < 6) { Toast.makeText(getContext(), "Password must be 6+ chars", Toast.LENGTH_SHORT).show(); }
                    else if (!newPass.equals(confirm)) { Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show(); }
                    else { updateUserInSharedPreferences(currentEmail, 2, newPass); tvPassword.setText("******"); Toast.makeText(getContext(), "Password updated", Toast.LENGTH_SHORT).show(); }
                }).setNegativeButton("Cancel", null).show();
    }

    private void updateUserInSharedPreferences(String currentEmail, int fieldIndex, String newValue) {
        SharedPreferences authPrefs = requireActivity().getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
        String existingUsers = authPrefs.getString(USERS_KEY, "");
        if (existingUsers.isEmpty()) return;
        String[] users = existingUsers.split(";");
        StringBuilder updatedUsersList = new StringBuilder();
        for (String user : users) {
            String[] userData = user.split(",");
            if (userData.length >= 3 && userData[1].trim().equalsIgnoreCase(currentEmail.trim())) {
                userData[fieldIndex] = newValue;
                if (updatedUsersList.length() > 0) updatedUsersList.append(";");
                updatedUsersList.append(userData[0]).append(",").append(userData[1]).append(",").append(userData[2]);
            } else {
                if (updatedUsersList.length() > 0) updatedUsersList.append(";");
                updatedUsersList.append(user);
            }
        }
        authPrefs.edit().putString(USERS_KEY, updatedUsersList.toString()).apply();
    }

    private void showLogoutDialog() {
        if (getContext() == null) return;
        new AlertDialog.Builder(getContext()).setTitle("Log Out").setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    SharedPreferences.Editor editor = requireActivity().getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE).edit();
                    editor.remove("isLoggedIn");
                    editor.remove("currentUserName");
                    editor.remove("currentUserEmail");
                    editor.apply();

                    Intent intent = new Intent(requireActivity(), ChoiceActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    if (getActivity() != null) getActivity().finish();
                }).setNegativeButton("Cancel", null).show();
    }
}