package com.example.healthcareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageButton btnEmergency = view.findViewById(R.id.btnEmergency);
        ImageButton btnHospital = view.findViewById(R.id.btnHospital);
        ImageButton btnBlood = view.findViewById(R.id.btnBlood);
        ImageButton btnPrescription = view.findViewById(R.id.btnPrescription);
        ImageButton btnCheckup = view.findViewById(R.id.btnCheckup);
        ImageButton btnRadiology = view.findViewById(R.id.btnRadiology);

        RecyclerView rvDoctors = view.findViewById(R.id.rv_doctors_recommendation);
        rvDoctors.setHasFixedSize(true);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvDoctors.setLayoutManager(horizontalLayoutManager);

        Doctor[] doctorsList = new Doctor[] {
                new Doctor("Dr. Sara", "Eye Specialist", R.drawable.account, "Evercare Hospital", "+92 300 1111111", "Has over 15 years of experience in ophthalmology. She specializes in retinal surgeries and general eye checkups."),
                new Doctor("Dr. Zeeshan", "Radiology Specialist", R.drawable.account, "Jinnah Hospital", "+92 300 2222222", "Leading radiologist with expertise in MRI and CT scan interpretations. Known for accurate diagnostics."),
                new Doctor("Dr. Ahad", "Cardiologist", R.drawable.account, "Services Hospital", "+92 300 3333333", "Specializes in cardiovascular diseases. Performs echocardiograms and stress tests regularly."),
                new Doctor("Dr. Emaan", "Dermatologist", R.drawable.account, "Shaikh Zayed Hospital", "+92 300 4444444", "Expert in treating acne, eczema, and cosmetic dermatology. Highly rated by patients."),
                new Doctor("Dr. Khalid", "General Physician", R.drawable.account, "Farooq Hospital", "+92 300 5555555", "Expert in family medicine and routine health checkups. Provides comprehensive primary care for all ages."),
                new Doctor("Dr. Maria", "Pediatrician", R.drawable.account, "Jinnah Hospital", "+92 300 6666666", "Highly trusted child specialist. Focuses on childhood vaccinations, growth monitoring, and pediatric infections."),
                new Doctor("Dr. Wahaj", "Orthopedic Surgeon", R.drawable.account, "Services Hospital", "+92 300 7777777", "Specializes in bone fractures, joint replacements, and sports injuries. Uses minimally invasive surgical techniques."),
                new Doctor("Dr. Hina", "Gynecologist", R.drawable.account, "Evercare Hospital", "+92 300 8888888", "Dedicated to women's health, pregnancy care, and reproductive health issues. Provides compassionate and confidential care.")
        };

        DoctorAdapter doctorAdapter = new DoctorAdapter(getContext(), doctorsList);
        rvDoctors.setAdapter(doctorAdapter);

        btnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:1122"));
                startActivity(dialIntent);
            }
        });

        btnHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=nearest hospital");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
                else {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/nearest+hospital"));
                    startActivity(webIntent);
                }
            }
        });

        btnBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BloodActivity.class);
                startActivity(intent);
            }
        });

        btnPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrescriptionActivity.class);
                startActivity(intent);
            }
        });

        btnCheckup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("HealthCarePrefs", Context.MODE_PRIVATE);
                String loggedInUserEmail = sharedPreferences.getString("currentUserEmail", "");

                Intent intent = new Intent(getContext(), CheckupActivity.class);
                intent.putExtra("USER_EMAIL", loggedInUserEmail);
                startActivity(intent);
            }
        });

        btnRadiology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("HealthCarePrefs", Context.MODE_PRIVATE);
                String loggedInUserEmail = sharedPreferences.getString("currentUserEmail", "");

                Intent intent = new Intent(getContext(), RadiologyActivity.class);
                intent.putExtra("USER_EMAIL", loggedInUserEmail);
                startActivity(intent);
            }
        });

        return view;
    }
}