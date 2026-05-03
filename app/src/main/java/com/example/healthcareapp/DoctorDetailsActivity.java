package com.example.healthcareapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class DoctorDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        ImageView ivBack = findViewById(R.id.ivBackDoctor);
        ivBack.setOnClickListener(v -> finish());

        String name = getIntent().getStringExtra("name");
        String specialty = getIntent().getStringExtra("specialty");
        int imageResId = getIntent().getIntExtra("image", R.drawable.account);
        String hospital = getIntent().getStringExtra("hospital");
        String phone = getIntent().getStringExtra("phone");
        String bio = getIntent().getStringExtra("bio");
        String email = getIntent().getStringExtra("email");
        String experience = getIntent().getStringExtra("experience");
        double rating = getIntent().getDoubleExtra("rating", 0.0);
        boolean isAdmin = getIntent().getBooleanExtra("isAdmin", false);
        MaterialButton btnBookAppointment = findViewById(R.id.btnBookAppointment);

        if (isAdmin) {
            btnBookAppointment.setVisibility(View.GONE);
        }

        ImageView ivImage = findViewById(R.id.ivDetailImage);
        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvSpecialty = findViewById(R.id.tvDetailSpecialty);
        TextView tvHospital = findViewById(R.id.tvDetailHospital);
        TextView tvExperience = findViewById(R.id.tvDetailExperience);
        TextView tvPhone = findViewById(R.id.tvDetailPhone);
        TextView tvEmail = findViewById(R.id.tvDetailEmail);
        TextView tvBio = findViewById(R.id.tvDetailBio);
        TextView tvDetailRating = findViewById(R.id.tvDetailRating);

        ImageButton btnCallDoctor = findViewById(R.id.btnCallDoctor);
        ImageButton btnEmailDoctor = findViewById(R.id.btnEmailDoctor);

        ivImage.setImageResource(imageResId);
        tvName.setText(name);
        tvSpecialty.setText(specialty);
        tvHospital.setText(hospital);
        tvExperience.setText(experience);
        tvPhone.setText(phone);
        tvEmail.setText(email);
        tvBio.setText(bio);
        tvDetailRating.setText(rating + " ★");

        btnCallDoctor.setOnClickListener(v -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + phone));
            startActivity(dialIntent);
        });

        btnEmailDoctor.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + email));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Appointment Inquiry - " + name);

            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        });

        btnBookAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorDetailsActivity.this, BookAppointmentActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("specialty", specialty);
            intent.putExtra("image", imageResId);
            intent.putExtra("hospital", hospital);
            intent.putExtra("phone", phone);
            intent.putExtra("email", email);
            intent.putExtra("experience", experience);
            intent.putExtra("bio", bio);
            intent.putExtra("rating", rating);
            startActivity(intent);
        });
    }
}