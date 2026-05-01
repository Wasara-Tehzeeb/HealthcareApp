package com.example.healthcareapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SchedulePagerAdapter extends FragmentStateAdapter {

    public SchedulePagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new AllScheduleFragment();
            case 1: return new UpcomingScheduleFragment();
            case 2: return new CancelledScheduleFragment();
            case 3: return new CompletedScheduleFragment();
            default: return new AllScheduleFragment();
        }
    }

    @Override
    public int getItemCount() { return 4; }
}