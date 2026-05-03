package com.example.healthcareapp;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

public class DoctorHelper {
    private static final String PREF_NAME = "HealthCarePrefs";
    private static final String KEY_LIST = "doctors_list";
    private static final String KEY_SEEDED = "isDoctorSeeded";

    public static void seedDefaultDoctors(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (!prefs.getBoolean(KEY_SEEDED, false)) {
            List<Doctor> list = new ArrayList<>();
            list.add(new Doctor("Dr. Sara", "Eye Specialist", R.drawable.account, "Evercare Hospital", "+92 300 1111111", "sara@health.com", 4.8, "10 years", "Has over 15 years of experience in ophthalmology. She specializes in retinal surgeries and general eye checkups."));
            list.add(new Doctor("Dr. Zeeshan", "Radiology Specialist", R.drawable.account, "Jinnah Hospital", "+92 300 2222222", "zeeshan@health.com", 5.0, "12 years", "Leading radiologist with expertise in MRI and CT scan interpretations. Known for accurate diagnostics."));
            list.add(new Doctor("Dr. Ahad", "Cardiologist", R.drawable.account, "Services Hospital", "+92 300 3333333", "ahad@health.com", 4.0, "6 years", "Specializes in cardiovascular diseases. Performs echocardiograms and stress tests regularly."));
            list.add(new Doctor("Dr. Emaan", "Dermatologist", R.drawable.account, "Shaikh Zayed Hospital", "+92 300 4444444", "emaan@health.com", 4.5, "9 years", "Expert in treating acne, eczema, and cosmetic dermatology. Highly rated by patients."));
            list.add(new Doctor("Dr. Khalid", "General Physician", R.drawable.account, "Farooq Hospital", "+92 300 5555555", "khalid@health.com", 3.9, "3 years", "Expert in family medicine and routine health checkups. Provides comprehensive primary care for all ages."));
            list.add(new Doctor("Dr. Maria", "Pediatrician", R.drawable.account, "Jinnah Hospital", "+92 300 6666666", "maria@health.com", 4.7, "15 years", "Highly trusted child specialist. Focuses on childhood vaccinations, growth monitoring, and pediatric infections."));
            list.add(new Doctor("Dr. Wahaj", "Orthopedic Surgeon", R.drawable.account, "Services Hospital", "+92 300 7777777", "wahaj@health.com", 5.0, "20 years", "Specializes in bone fractures, joint replacements, and sports injuries. Uses minimally invasive surgical techniques."));
            list.add(new Doctor("Dr. Hina", "Gynecologist", R.drawable.account, "Evercare Hospital", "+92 300 8888888", "hina@health.com", 4.6, "11 years", "Dedicated to women's health, pregnancy care, and reproductive health issues. Provides compassionate and confidential care."));
            saveList(context, list);
            prefs.edit().putBoolean(KEY_SEEDED, true).apply();
        }
    }

    public static List<Doctor> getDoctors(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String data = prefs.getString(KEY_LIST, "");
        List<Doctor> list = new ArrayList<>();
        if (!data.isEmpty()) {
            for (String item : data.split(";")) {
                String[] parts = item.split("\\|");
                if (parts.length == 9) {
                    list.add(new Doctor(
                            parts[0], parts[1], Integer.parseInt(parts[2]),
                            parts[3], parts[4], parts[5], Double.parseDouble(parts[6]),
                            parts[7], parts[8]
                    ));
                }
            }
        }
        return list;
    }

    public static Doctor[] getDoctorsArray(Context context) {
        List<Doctor> list = getDoctors(context);
        return list.toArray(new Doctor[0]);
    }

    public static void removeDoctor(Context context, String doctorName) {
        List<Doctor> list = getDoctors(context);
        list.removeIf(d -> d.getName().equals(doctorName));
        saveList(context, list);
    }

    public static void addDoctor(Context context, Doctor doctor) {
        List<Doctor> list = getDoctors(context);
        list.add(doctor);
        saveList(context, list);
    }

    private static void saveList(Context context, List<Doctor> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(";");
            Doctor d = list.get(i);
            sb.append(d.getName()).append("|")
                    .append(d.getSpecialty()).append("|")
                    .append(d.getImageResId()).append("|")
                    .append(d.getHospital()).append("|")
                    .append(d.getPhone()).append("|")
                    .append(d.getEmail()).append("|")
                    .append(d.getRating()).append("|")
                    .append(d.getExperience()).append("|")
                    .append(d.getBio());
        }
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                .putString(KEY_LIST, sb.toString()).apply();
    }
}