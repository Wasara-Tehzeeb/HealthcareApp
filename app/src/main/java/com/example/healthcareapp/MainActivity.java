package com.example.healthcareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcareapp.LoginActivity;
import com.example.healthcareapp.R;

public class MainActivity extends AppCompatActivity {

    ImageView logo;
    private static final String PREF_NAME = "HealthCarePrefs";
    private static final String USERS_KEY = "users_data";
    private static final String KEY_DEFAULTS_ADDED = "isDefaultUsersAdded";
    private static final String HEALTH_PREFS = "HealthAppPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.logoImage);

        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade);
        logo.startAnimation(fade);

        addDefaultTestUsers();

        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

            Intent intent;
            if (isLoggedIn) {
                intent = new Intent(MainActivity.this, HomeActivity.class);
            }
            else {
                intent = new Intent(MainActivity.this, ChoiceActivity.class);
            }

            startActivity(intent);
            finish();
        }, 2000);
    }

    private void addDefaultTestUsers() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        boolean isAdded = sharedPreferences.getBoolean(KEY_DEFAULTS_ADDED, false);

        if (!isAdded) {
            String existingUsers = sharedPreferences.getString(USERS_KEY, "");

            String defaultUsers = "Fatima Ahmed,fatima@gmail.com,fatima123;" +
                    "Hamza Khan,hamza@gmail.com,hamza456;" +
                    "Zainab Ali,zainab@gmail.com,zainab789";

            SharedPreferences.Editor editor = sharedPreferences.edit();

            if (existingUsers.isEmpty()) {
                editor.putString(USERS_KEY, defaultUsers);
            }
            else {
                editor.putString(USERS_KEY, existingUsers + ";" + defaultUsers);
            }
            editor.putBoolean(KEY_DEFAULTS_ADDED, true);
            editor.apply();

            SharedPreferences healthPrefs = getSharedPreferences(HEALTH_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor healthEditor = healthPrefs.edit();

            healthEditor.putString("health_dob_fatima@gmail.com", "15/05/1998");
            healthEditor.putString("health_blood_fatima@gmail.com", "A+");
            healthEditor.putString("health_allergy_fatima@gmail.com", "Peanuts");

            healthEditor.putString("health_dob_hamza@gmail.com", "22/11/1995");
            healthEditor.putString("health_blood_hamza@gmail.com", "O-");
            healthEditor.putString("health_allergy_hamza@gmail.com", "None");

            healthEditor.putString("health_dob_zainab@gmail.com", "01/01/2000");
            healthEditor.putString("health_blood_zainab@gmail.com", "B+");
            healthEditor.putString("health_allergy_zainab@gmail.com", "Dust, Pollen");


            healthEditor.putInt("blood_A_plus", 10);
            healthEditor.putInt("blood_A_minus", 5);
            healthEditor.putInt("blood_B_plus", 8);
            healthEditor.putInt("blood_B_minus", 3);
            healthEditor.putInt("blood_AB_plus", 2);
            healthEditor.putInt("blood_AB_minus", 1);
            healthEditor.putInt("blood_O_plus", 12);
            healthEditor.putInt("blood_O_minus", 4);


            healthEditor.putString("prescription_fatima@gmail.com", "Panadol|500mg|Twice a day;Brufen|200mg|Once a day");
            healthEditor.putString("diet_plan_fatima@gmail.com", "High Sugar Foods|Fried Items|Caffeine");

            healthEditor.putString("prescription_hamza@gmail.com", "Amoxicillin|250mg|Three times a day");
            healthEditor.putString("diet_plan_hamza@gmail.com", "Dairy Products|Raw Vegetables|Sour Foods");

            healthEditor.putString("prescription_zainab@gmail.com", "Cetirizine|10mg|Once daily;Multivitamin|1 Tablet|Morning;Cough Syrup|10ml|Thrice daily");
            healthEditor.putString("diet_plan_zainab@gmail.com", "Dust Exposure|Cold Water|Ice Cream");


            healthEditor.commit();
        }
    }
}