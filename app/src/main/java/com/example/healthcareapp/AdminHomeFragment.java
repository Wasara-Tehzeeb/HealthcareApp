package com.example.healthcareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

public class AdminHomeFragment extends Fragment {

    private EditText etSearchDoctor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        MaterialButton btnAddDoctor = view.findViewById(R.id.btnAddDoctor);
        MaterialButton btnRemoveDoctor = view.findViewById(R.id.btnRemoveDoctor);
        MaterialButton btnRemoveUser = view.findViewById(R.id.btnRemoveUser);

        etSearchDoctor = view.findViewById(R.id.etSearchDoctor);

        RecyclerView rvDoctors = view.findViewById(R.id.rv_doctors_recommendation);
        rvDoctors.setHasFixedSize(true);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvDoctors.setLayoutManager(horizontalLayoutManager);

        Doctor[] doctorsList = DoctorHelper.getDoctorsArray(getContext());

        DoctorAdapter doctorAdapter = new DoctorAdapter(getContext(), doctorsList, true);
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
                            intent.putExtra("isAdmin", true);

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

        btnAddDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddDoctorActivity.class);
                startActivity(intent);
            }
        });


        btnRemoveDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RemoveDoctorActivity.class);
                startActivity(intent);
            }
        });

        btnRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RemoveUserActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        RecyclerView rvDoctors = getView().findViewById(R.id.rv_doctors_recommendation);
        Doctor[] doctorsList = DoctorHelper.getDoctorsArray(getContext());
        rvDoctors.setAdapter(new DoctorAdapter(getContext(), doctorsList, true));
    }
}