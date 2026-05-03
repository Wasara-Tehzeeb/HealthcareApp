package com.example.healthcareapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AdminAdapter extends FragmentStateAdapter {
    private final String userName;
    private final String userEmail;
    private final String userPassword;
    public AdminAdapter(@NonNull FragmentActivity fa, String name, String email, String password) {
        super(fa);
        this.userName = name;
        this.userEmail = email;
        this.userPassword = password;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 3) {
            AdminSettingsFragment settingsFragment = new AdminSettingsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("USERNAME", userName);
            bundle.putString("EMAIL", userEmail);
            bundle.putString("PASSWORD", userPassword);
            settingsFragment.setArguments(bundle);
            return settingsFragment;
        }

        switch (position) {
            case 0: return new AdminHomeFragment();
            case 1: return new AdminSettingsFragment();
            default: return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
