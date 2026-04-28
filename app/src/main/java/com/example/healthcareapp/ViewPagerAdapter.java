package com.example.healthcareapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private final String userName;
    private final String userEmail;
    private final String userPassword;
    public ViewPagerAdapter(@NonNull FragmentActivity fa, String name, String email, String password) {
        super(fa);
        this.userName = name;
        this.userEmail = email;
        this.userPassword = password;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 3) {
            SettingsFragment settingsFragment = new SettingsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("USERNAME", userName);
            bundle.putString("EMAIL", userEmail);
            bundle.putString("PASSWORD", userPassword);
            settingsFragment.setArguments(bundle); // Pass bundle to fragment
            return settingsFragment;
        }

        switch (position) {
            case 0: return new HomeFragment();
            case 1: return new ScheduleFragment();
            case 2: return new MessageFragment();
            default: return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
