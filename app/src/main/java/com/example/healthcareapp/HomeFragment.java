package com.example.healthcareapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ScheduleAdapter.OnActionListener{
    private RecyclerView rvTodaySchedule;
    private TextView tvNoScheduleToday;
    private String loggedInUserEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageButton btnEmergency = view.findViewById(R.id.btnEmergency);
        ImageButton btnHospital = view.findViewById(R.id.btnHospital);
        ImageButton btnBlood = view.findViewById(R.id.btnBlood);
        ImageButton btnPrescription = view.findViewById(R.id.btnPrescription);
        ImageButton btnCheckup = view.findViewById(R.id.btnCheckup);
        ImageButton btnRadiology = view.findViewById(R.id.btnRadiology);

        EditText etSearchDoctor = view.findViewById(R.id.etSearchDoctor);

        rvTodaySchedule = view.findViewById(R.id.rvTodaySchedule);
        tvNoScheduleToday = view.findViewById(R.id.tvNoScheduleToday);
        TextView tvSeeAllSchedule = view.findViewById(R.id.tvSeeAllSchedule);

        rvTodaySchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTodaySchedule.setNestedScrollingEnabled(false);

        SharedPreferences prefs = requireActivity().getSharedPreferences("HealthCarePrefs", Context.MODE_PRIVATE);
        loggedInUserEmail = prefs.getString("currentUserEmail", "");

        loadTodaySchedules();

        tvSeeAllSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
                    viewPager.setCurrentItem(1, true);
                }
            }
        });

        RecyclerView rvDoctors = view.findViewById(R.id.rv_doctors_recommendation);
        rvDoctors.setHasFixedSize(true);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvDoctors.setLayoutManager(horizontalLayoutManager);

        Doctor[] doctorsList = new Doctor[] {
                new Doctor("Dr. Sara", "Eye Specialist", R.drawable.account, "Evercare Hospital", "+92 300 1111111","sara@health.com",4.8,"10 years", "Has over 15 years of experience in ophthalmology. She specializes in retinal surgeries and general eye checkups."),
                new Doctor("Dr. Zeeshan", "Radiology Specialist", R.drawable.account, "Jinnah Hospital", "+92 300 2222222","zeeshan@health.com",5.0,"12 years", "Leading radiologist with expertise in MRI and CT scan interpretations. Known for accurate diagnostics."),
                new Doctor("Dr. Ahad", "Cardiologist", R.drawable.account, "Services Hospital", "+92 300 3333333","ahad@health.com",4.0,"6 years", "Specializes in cardiovascular diseases. Performs echocardiograms and stress tests regularly."),
                new Doctor("Dr. Emaan", "Dermatologist", R.drawable.account, "Shaikh Zayed Hospital", "+92 300 4444444","emaan@health.com",4.5,"9 years", "Expert in treating acne, eczema, and cosmetic dermatology. Highly rated by patients."),
                new Doctor("Dr. Khalid", "General Physician", R.drawable.account, "Farooq Hospital", "+92 300 5555555","khalid@health.com",3.9,"3 years", "Expert in family medicine and routine health checkups. Provides comprehensive primary care for all ages."),
                new Doctor("Dr. Maria", "Pediatrician", R.drawable.account, "Jinnah Hospital", "+92 300 6666666","maria@health.com",4.7,"15 years", "Highly trusted child specialist. Focuses on childhood vaccinations, growth monitoring, and pediatric infections."),
                new Doctor("Dr. Wahaj", "Orthopedic Surgeon", R.drawable.account, "Services Hospital", "+92 300 7777777","wahaj@health.com",5.0,"20 years", "Specializes in bone fractures, joint replacements, and sports injuries. Uses minimally invasive surgical techniques."),
                new Doctor("Dr. Hina", "Gynecologist", R.drawable.account, "Evercare Hospital", "+92 300 8888888","hina@health.com",4.6 ,"11 years","Dedicated to women's health, pregnancy care, and reproductive health issues. Provides compassionate and confidential care.")
        };

        DoctorAdapter doctorAdapter = new DoctorAdapter(getContext(), doctorsList);
        rvDoctors.setAdapter(doctorAdapter);

        etSearchDoctor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                    String searchQuery = etSearchDoctor.getText().toString().trim().toLowerCase();
                    boolean isFound = false;

                    for (Doctor doctor : doctorsList) {
                        if (doctor.getName().toLowerCase().equals(searchQuery)) {
                            isFound = true;

                            Intent intent = new Intent(getContext(), DoctorDetailsActivity.class);
                            intent.putExtra("name", doctor.getName());
                            intent.putExtra("specialty", doctor.getSpecialty());
                            intent.putExtra("image", doctor.getImageResId());
                            intent.putExtra("hospital", doctor.getHospital());
                            intent.putExtra("phone", doctor.getPhone());
                            intent.putExtra("email", doctor.getEmail());
                            intent.putExtra("experience", doctor.getExperience());
                            intent.putExtra("bio", doctor.getBio());
                            intent.putExtra("rating", doctor.getRating());

                            startActivity(intent);
                            break;
                        }
                    }

                    if (!isFound) {
                        Toast.makeText(getContext(), "Doctor not found", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

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
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=nearest hospitals&view=maps");
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

    private void loadTodaySchedules() {
        String todayStr = ScheduleHelper.getTodayDate();
        List<Schedule> allUserSchedules = ScheduleHelper.getSchedules(getContext(), loggedInUserEmail);
        List<Schedule> todaySchedules = new ArrayList<>();

        for (Schedule s : allUserSchedules) {
            if (s.getDate().equals(todayStr) && s.getStatus().equals("upcoming")) {
                todaySchedules.add(s);
            }
        }

        if (todaySchedules.isEmpty()) {
            rvTodaySchedule.setVisibility(View.GONE);
            tvNoScheduleToday.setVisibility(View.VISIBLE);
        }
        else {
            rvTodaySchedule.setVisibility(View.VISIBLE);
            tvNoScheduleToday.setVisibility(View.GONE);
            rvTodaySchedule.setAdapter(new ScheduleAdapter(getContext(), todaySchedules, this));
        }
    }

    @Override
    public void onActionClick(Schedule schedule) {
        new AlertDialog.Builder(getContext())
                .setTitle("Cancel Appointment")
                .setMessage("Are you sure you want to cancel your appointment with " + schedule.getDoctorName() + "?")
                .setPositiveButton("Yes, Cancel", (dialog, which) -> {

                    ScheduleHelper.updateScheduleStatus(getContext(), loggedInUserEmail, schedule.getId(), "cancelled");
                    Toast.makeText(getContext(), "Appointment Cancelled", Toast.LENGTH_SHORT).show();
                    loadTodaySchedules();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTodaySchedules();
    }
}