package com.example.healthcareapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ScheduleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        ViewPager2 viewPager = view.findViewById(R.id.viewPagerSchedule);
        TabLayout tabLayout = view.findViewById(R.id.tabLayoutSchedule);

        SchedulePagerAdapter adapter = new SchedulePagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("All"); break;
                case 1: tab.setText("Upcoming"); break;
                case 2: tab.setText("Cancelled"); break;
                case 3: tab.setText("Completed"); break;
            }
        }).attach();

        return view;
    }
}