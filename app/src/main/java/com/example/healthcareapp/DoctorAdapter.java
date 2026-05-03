package com.example.healthcareapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private Doctor[] doctorList;
    private Context context;
    private boolean isAdmin;

    public DoctorAdapter(Context context, Doctor[] doctorList) {
        this.context = context;
        this.doctorList = doctorList;
        this.isAdmin = false;
    }

    public DoctorAdapter(Context context, Doctor[] doctorList, boolean isAdmin) {
        this.context = context;
        this.doctorList = doctorList;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList[position];

        holder.tvName.setText(doctor.getName());
        holder.tvSpecialty.setText(doctor.getSpecialty());
        holder.ivImage.setImageResource(doctor.getImageResId());

        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DoctorDetailsActivity.class);

                intent.putExtra("name", doctor.getName());
                intent.putExtra("specialty", doctor.getSpecialty());
                intent.putExtra("image", doctor.getImageResId());
                intent.putExtra("hospital", doctor.getHospital());
                intent.putExtra("phone", doctor.getPhone());
                intent.putExtra("email", doctor.getEmail());
                intent.putExtra("rating", doctor.getRating());
                intent.putExtra("experience", doctor.getExperience());
                intent.putExtra("bio", doctor.getBio());
                intent.putExtra("isAdmin", isAdmin);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.length; }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        android.widget.ImageView ivImage;
        TextView tvName, tvSpecialty;
        MaterialButton btnDetails;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_doctor_image);
            tvName = itemView.findViewById(R.id.tv_doctor_name);
            tvSpecialty = itemView.findViewById(R.id.tv_doctor_specialty);
            btnDetails = itemView.findViewById(R.id.btn_view_details);
        }
    }
}