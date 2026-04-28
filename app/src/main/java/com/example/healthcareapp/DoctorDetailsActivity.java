package com.example.healthcareapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

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

        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvSpecialty = findViewById(R.id.tvDetailSpecialty);
        ImageView ivImage = findViewById(R.id.ivDetailImage);
        TextView tvHospital = findViewById(R.id.tvDetailHospital);
        TextView tvPhone = findViewById(R.id.tvDetailPhone);
        TextView tvBio = findViewById(R.id.tvDetailBio);

        tvName.setText(name);
        tvSpecialty.setText(specialty);
        ivImage.setImageResource(imageResId);
        tvHospital.setText(hospital);
        tvPhone.setText(phone);
        tvBio.setText(bio);
    }
}