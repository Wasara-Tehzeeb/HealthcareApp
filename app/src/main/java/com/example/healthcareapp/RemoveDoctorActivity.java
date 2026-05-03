package com.example.healthcareapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RemoveDoctorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_doctor);

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());

        showDoctorList();
    }

    private void showDoctorList() {
        Doctor[] doctors = DoctorHelper.getDoctorsArray(this);

        if (doctors.length == 0) {
            new AlertDialog.Builder(this)
                    .setTitle("No Doctors")
                    .setMessage("No doctors available to remove.")
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .setOnCancelListener(dialog -> finish())
                    .show();
            return;
        }

        String[] items = new String[doctors.length];
        for (int i = 0; i < doctors.length; i++) {
            items[i] = doctors[i].getName() + " — " + doctors[i].getSpecialty();
        }

        new AlertDialog.Builder(this)
                .setTitle("Select Doctor to Remove")
                .setItems(items, (dialog, which) -> {
                    Doctor selected = doctors[which];
                    confirmRemoval(selected.getName());
                })
                .setNegativeButton("Back", (dialog, which) -> finish())
                .setOnCancelListener(dialog -> finish())
                .show();
    }

    private void confirmRemoval(String doctorName) {
        new AlertDialog.Builder(this)
                .setTitle("Remove Doctor")
                .setMessage("Are you sure you want to remove " + doctorName + "? This cannot be undone.")
                .setPositiveButton("Yes, Remove", (dialog, which) -> {
                    DoctorHelper.removeDoctor(this, doctorName);
                    Toast.makeText(this, doctorName + " removed", Toast.LENGTH_SHORT).show();
                    showDoctorList();
                })
                .setNegativeButton("No", null)
                .show();
    }
}