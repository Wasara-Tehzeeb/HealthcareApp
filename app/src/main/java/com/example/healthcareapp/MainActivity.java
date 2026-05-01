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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        addDefaultSchedules();
        addDefaultNotifications();

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

    private void addDefaultSchedules() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (!prefs.getBoolean("isScheduleSeeded", false)) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar cal = Calendar.getInstance();

            String today = sdf.format(cal.getTime());

            cal.add(Calendar.DAY_OF_MONTH, 1);
            String tomorrow = sdf.format(cal.getTime());

            cal.add(Calendar.DAY_OF_MONTH, -2);
            String twoDaysAgo = sdf.format(cal.getTime());

            cal.add(Calendar.DAY_OF_MONTH, -1);
            String threeDaysAgo = sdf.format(cal.getTime());

            cal.add(Calendar.DAY_OF_MONTH, -1);
            String fourDaysAgo = sdf.format(cal.getTime());

            ScheduleHelper.saveSchedule(this, "fatima@gmail.com", new Schedule("F001", "Dr. Zeeshan", "Radiology Specialist", "Jinnah Hospital", today, "09:00", "Checkup", "upcoming"));
            ScheduleHelper.saveSchedule(this, "fatima@gmail.com", new Schedule("F002", "Dr. Ahad", "Cardiologist", "Services Hospital", tomorrow, "14:00", "Consultation", "upcoming"));
            ScheduleHelper.saveSchedule(this, "fatima@gmail.com", new Schedule("F003", "Dr. Hina", "Gynecologist", "Evercare Hospital", today, "16:00", "Follow-up", "upcoming"));
            ScheduleHelper.saveSchedule(this, "fatima@gmail.com", new Schedule("F004", "Dr. Emaan", "Dermatologist", "Shaikh Zayed Hospital", twoDaysAgo, "11:00", "Follow-up", "completed"));
            ScheduleHelper.saveSchedule(this, "fatima@gmail.com", new Schedule("F005", "Dr. Khalid", "General Physician", "Farooq Hospital", threeDaysAgo, "10:00", "Checkup", "completed"));
            ScheduleHelper.saveSchedule(this, "fatima@gmail.com", new Schedule("F006", "Dr. Sara", "Eye Specialist", "Evercare Hospital", twoDaysAgo, "15:00", "Checkup", "cancelled"));
            ScheduleHelper.saveSchedule(this, "fatima@gmail.com", new Schedule("F007", "Dr. Wahaj", "Orthopedic Surgeon", "Services Hospital", fourDaysAgo, "08:30", "Consultation", "cancelled"));

            ScheduleHelper.saveSchedule(this, "hamza@gmail.com", new Schedule("H001", "Dr. Wahaj", "Orthopedic Surgeon", "Services Hospital", today, "10:30", "Consultation", "upcoming"));
            ScheduleHelper.saveSchedule(this, "hamza@gmail.com", new Schedule("H002", "Dr. Maria", "Pediatrician", "Jinnah Hospital", tomorrow, "11:00", "Checkup", "upcoming"));
            ScheduleHelper.saveSchedule(this, "hamza@gmail.com", new Schedule("H003", "Dr. Zeeshan", "Radiology Specialist", "Jinnah Hospital", twoDaysAgo, "09:00", "Checkup", "completed"));
            ScheduleHelper.saveSchedule(this, "hamza@gmail.com", new Schedule("H004", "Dr. Emaan", "Dermatologist", "Shaikh Zayed Hospital", fourDaysAgo, "14:00", "Follow-up", "completed"));
            ScheduleHelper.saveSchedule(this, "hamza@gmail.com", new Schedule("H005", "Dr. Khalid", "General Physician", "Farooq Hospital", threeDaysAgo, "09:00", "Checkup", "cancelled"));

            ScheduleHelper.saveSchedule(this, "zainab@gmail.com", new Schedule("Z001", "Dr. Hina", "Gynecologist", "Evercare Hospital", tomorrow, "11:00", "Checkup", "upcoming"));
            ScheduleHelper.saveSchedule(this, "zainab@gmail.com", new Schedule("Z002", "Dr. Sara", "Eye Specialist", "Evercare Hospital", today, "13:00", "Consultation", "upcoming"));
            ScheduleHelper.saveSchedule(this, "zainab@gmail.com", new Schedule("Z003", "Dr. Ahad", "Cardiologist", "Services Hospital", twoDaysAgo, "15:00", "Follow-up", "completed"));
            ScheduleHelper.saveSchedule(this, "zainab@gmail.com", new Schedule("Z004", "Dr. Khalid", "General Physician", "Farooq Hospital", threeDaysAgo, "10:00", "Checkup", "completed"));
            ScheduleHelper.saveSchedule(this, "zainab@gmail.com", new Schedule("Z005", "Dr. Wahaj", "Orthopedic Surgeon", "Services Hospital", twoDaysAgo, "12:00", "Consultation", "cancelled"));
            ScheduleHelper.saveSchedule(this, "zainab@gmail.com", new Schedule("Z006", "Dr. Maria", "Pediatrician", "Jinnah Hospital", fourDaysAgo, "09:30", "Checkup", "cancelled"));


            prefs.edit().putBoolean("isScheduleSeeded", true).apply();
        }
    }

    private void addDefaultNotifications() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (!prefs.getBoolean("isNotificationSeeded", false)) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar cal = Calendar.getInstance();

            String today = sdf.format(cal.getTime());

            cal.add(Calendar.DAY_OF_MONTH, -1);
            String yesterday = sdf.format(cal.getTime());

            cal.add(Calendar.DAY_OF_MONTH, -1);
            String twoDaysAgo = sdf.format(cal.getTime());

            cal.add(Calendar.DAY_OF_MONTH, -1);
            String threeDaysAgo = sdf.format(cal.getTime());

            cal.add(Calendar.DAY_OF_MONTH, -2);
            String fiveDaysAgo = sdf.format(cal.getTime());

            NotificationHelper.saveNotification(this, "fatima@gmail.com", new Notification("NF001", "Appointment Reminder", "You have an upcoming checkup appointment today. Please arrive 10 minutes early.", "09:00", today, "Checkup", "Dr. Zeeshan"));
            NotificationHelper.saveNotification(this, "fatima@gmail.com", new Notification("NF002", "Consultation Scheduled", "Your consultation with Dr. Ahad has been scheduled for tomorrow at Services Hospital.", "14:00", today, "Consultation", "Dr. Ahad"));
            NotificationHelper.saveNotification(this, "fatima@gmail.com", new Notification("NF003", "Follow-up Reminder", "It's time for your follow-up visit with Dr. Hina regarding your previous treatment.", "16:00", today, "Follow up", "Dr. Hina"));
            NotificationHelper.saveNotification(this, "fatima@gmail.com", new Notification("NF004", "Appointment Completed", "Your checkup with Dr. Emaan has been completed successfully. Reports will be available soon.", "11:00", yesterday, "Checkup", "Dr. Emaan"));
            NotificationHelper.saveNotification(this, "fatima@gmail.com", new Notification("NF005", "Follow-up Completed", "Your follow-up with Dr. Khalid is complete. Please continue your prescribed medication.", "10:00", twoDaysAgo, "Follow up", "Dr. Khalid"));
            NotificationHelper.saveNotification(this, "fatima@gmail.com", new Notification("NF006", "Appointment Cancelled", "Your appointment with Dr. Sara has been cancelled as per your request.", "15:00", twoDaysAgo, "Checkup", "Dr. Sara"));


            NotificationHelper.saveNotification(this, "hamza@gmail.com", new Notification("NH001", "Consultation Reminder", "Your consultation with Dr. Wahaj is scheduled for today. Bring your previous X-rays.", "10:30", today, "Consultation", "Dr. Wahaj"));
            NotificationHelper.saveNotification(this, "hamza@gmail.com", new Notification("NH002", "Checkup Scheduled", "Your child's checkup with Dr. Maria is scheduled for tomorrow at Jinnah Hospital.", "11:00", today, "Checkup", "Dr. Maria"));
            NotificationHelper.saveNotification(this, "hamza@gmail.com", new Notification("NH003", "Appointment Completed", "Your checkup with Dr. Zeeshan is complete. MRI results are normal.", "09:00", twoDaysAgo, "Checkup", "Dr. Zeeshan"));
            NotificationHelper.saveNotification(this, "hamza@gmail.com", new Notification("NH004", "Follow-up Due", "Please schedule a follow-up with Dr. Emaan for your skin treatment progress review.", "14:00", fiveDaysAgo, "Follow up", "Dr. Emaan"));
            NotificationHelper.saveNotification(this, "hamza@gmail.com", new Notification("NH005", "Appointment Cancelled", "Your checkup with Dr. Khalid has been cancelled due to doctor's unavailability.", "09:00", threeDaysAgo, "Checkup", "Dr. Khalid"));


            NotificationHelper.saveNotification(this, "zainab@gmail.com", new Notification("NZ001", "Checkup Reminder", "You have a checkup appointment scheduled for tomorrow at Evercare Hospital.", "11:00", today, "Checkup", "Dr. Hina"));
            NotificationHelper.saveNotification(this, "zainab@gmail.com", new Notification("NZ002", "Consultation Today", "Your eye consultation with Dr. Sara is today. Please bring your previous prescription.", "13:00", today, "Consultation", "Dr. Sara"));
            NotificationHelper.saveNotification(this, "zainab@gmail.com", new Notification("NZ003", "Follow-up Completed", "Your follow-up with Dr. Ahad is complete. Keep monitoring your heart health regularly.", "15:00", twoDaysAgo, "Follow up", "Dr. Ahad"));
            NotificationHelper.saveNotification(this, "zainab@gmail.com", new Notification("NZ004", "Checkup Completed", "Your general checkup with Dr. Khalid has been completed. All vitals are normal.", "10:00", threeDaysAgo, "Checkup", "Dr. Khalid"));
            NotificationHelper.saveNotification(this, "zainab@gmail.com", new Notification("NZ005", "Appointment Cancelled", "Your consultation with Dr. Wahaj has been cancelled as per your request.", "12:00", twoDaysAgo, "Consultation", "Dr. Wahaj"));
            NotificationHelper.saveNotification(this, "zainab@gmail.com", new Notification("NZ006", "Appointment Cancelled", "Your checkup with Dr. Maria has been cancelled due to emergency at hospital.", "09:30", fiveDaysAgo, "Checkup", "Dr. Maria"));

            prefs.edit().putBoolean("isNotificationSeeded", true).apply();
        }
    }
}