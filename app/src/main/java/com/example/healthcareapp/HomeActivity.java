package com.example.healthcareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager;
    TextView tvUserName;
    ImageButton btnNotification;

    private static final String PREFS_NAME = "HealthAppPrefs";
    private static final String KEY_DARK_MODE = "isDarkMode";
    private static final String KEY_FONT_SCALE = "fontScale";
    private static final String AUTH_PREFS = "HealthCarePrefs";

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float fontScale = prefs.getFloat(KEY_FONT_SCALE, 1.0f);

        Configuration config = new Configuration(newBase.getResources().getConfiguration());
        config.fontScale = fontScale;

        Context context = newBase.createConfigurationContext(config);
        super.attachBaseContext(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        boolean isDarkModeOn = prefs.getBoolean(KEY_DARK_MODE, false);
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_home);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tvUserName = findViewById(R.id.tvUserName);
        btnNotification = findViewById(R.id.btn_notification);

        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2, true);
            }
        });

        SharedPreferences authPrefs = getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
        String name = authPrefs.getString("currentUserName", "User");
        String email = authPrefs.getString("currentUserEmail", "");

        String password = getPasswordFromEmail(email);

        if (name != null && !name.isEmpty()) {
            tvUserName.setText(name);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, name, email, password);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Home");
                            tab.setIcon(R.drawable.home);
                            break;
                        case 1:
                            tab.setText("Schedule");
                            tab.setIcon(R.drawable.schedule);
                            break;
                        case 2:
                            tab.setText("Messages");
                            tab.setIcon(R.drawable.messages);
                            break;
                        case 3:
                            tab.setText("Settings");
                            tab.setIcon(R.drawable.settings);
                            break;
                    }
                }
        ).attach();
    }
    private String getPasswordFromEmail(String email) {
        if (email == null || email.isEmpty()) return "";

        SharedPreferences authPrefs = getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
        String existingUsers = authPrefs.getString("users_data", "");

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
}