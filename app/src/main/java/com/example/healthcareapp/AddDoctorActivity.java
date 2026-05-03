package com.example.healthcareapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddDoctorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());

        TextInputEditText etName = findViewById(R.id.etDoctorName);
        TextInputEditText etSpecialty = findViewById(R.id.etSpecialty);
        TextInputEditText etHospital = findViewById(R.id.etHospital);
        TextInputEditText etPhone = findViewById(R.id.etPhone);
        TextInputEditText etEmail = findViewById(R.id.etEmail);
        TextInputEditText etExperience = findViewById(R.id.etExperience);
        TextInputEditText etRating = findViewById(R.id.etRating);
        TextInputEditText etBio = findViewById(R.id.etBio);

        MaterialButton btnAdd = findViewById(R.id.btnAddDoctor);

        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String specialty = etSpecialty.getText().toString().trim();
            String hospital = etHospital.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String experience = etExperience.getText().toString().trim();
            String ratingStr = etRating.getText().toString().trim();
            String bio = etBio.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Enter doctor name", Toast.LENGTH_SHORT).show();
                etName.requestFocus();
                return;
            }
            if (specialty.isEmpty()) {
                Toast.makeText(this, "Enter specialty", Toast.LENGTH_SHORT).show();
                etSpecialty.requestFocus();
                return;
            }
            if (hospital.isEmpty()) {
                Toast.makeText(this, "Enter hospital", Toast.LENGTH_SHORT).show();
                etHospital.requestFocus();
                return;
            }
            if (phone.isEmpty()) {
                Toast.makeText(this, "Enter phone number", Toast.LENGTH_SHORT).show();
                etPhone.requestFocus();
                return;
            }
            if (email.isEmpty()) {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
                etEmail.requestFocus();
                return;
            }
            if (experience.isEmpty()) {
                Toast.makeText(this, "Enter experience", Toast.LENGTH_SHORT).show();
                etExperience.requestFocus();
                return;
            }
            if (ratingStr.isEmpty()) {
                Toast.makeText(this, "Enter rating", Toast.LENGTH_SHORT).show();
                etRating.requestFocus();
                return;
            }
            if (bio.isEmpty()) {
                Toast.makeText(this, "Enter bio", Toast.LENGTH_SHORT).show();
                etBio.requestFocus();
                return;
            }

            double rating;
            try {
                rating = Double.parseDouble(ratingStr);
                if (rating < 0.0 || rating > 5.0) {
                    Toast.makeText(this, "Rating must be between 0.0 and 5.0", Toast.LENGTH_SHORT).show();
                    etRating.requestFocus();
                    return;
                }
            }
            catch (NumberFormatException e) {
                Toast.makeText(this, "Enter a valid rating", Toast.LENGTH_SHORT).show();
                etRating.requestFocus();
                return;
            }

            Doctor newDoctor = new Doctor(
                    "Dr. " + name,
                    specialty,
                    R.drawable.account,
                    hospital,
                    phone,
                    email,
                    rating,
                    experience,
                    bio
            );

            DoctorHelper.addDoctor(this, newDoctor);

            Toast.makeText(this, "Dr. " + name + " added successfully", Toast.LENGTH_SHORT).show();

            etName.setText("");
            etSpecialty.setText("");
            etHospital.setText("");
            etPhone.setText("");
            etEmail.setText("");
            etExperience.setText("");
            etRating.setText("");
            etBio.setText("");
            etName.requestFocus();
        });
    }
}