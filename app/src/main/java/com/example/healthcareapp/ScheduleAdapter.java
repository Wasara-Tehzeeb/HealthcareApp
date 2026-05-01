package com.example.healthcareapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.healthcareapp.R;
import com.example.healthcareapp.ScheduleHelper;
import com.example.healthcareapp.Schedule;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private Context context;
    private List<Schedule> list;
    private OnActionListener listener;

    public interface OnActionListener {
        void onActionClick(Schedule schedule);
    }

    public ScheduleAdapter(Context context, List<Schedule> list, OnActionListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_schedule, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Schedule s = list.get(position);
        holder.tvHospital.setText(s.getHospital());
        holder.tvDoctor.setText(s.getDoctorName());
        holder.tvSpecialty.setText(s.getSpecialty());
        holder.tvDateTime.setText(ScheduleHelper.formatDisplayDate(s.getDate()) + ", " + ScheduleHelper.formatTime(s.getTime()));
        holder.tvType.setText(s.getType());

        if (s.getStatus().equals("upcoming")) {
            holder.btnAction.setText("Cancel");
            holder.btnAction.setVisibility(View.VISIBLE);
            holder.btnAction.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF5252")));
            holder.btnAction.setTextColor(Color.WHITE);
            holder.btnAction.setEnabled(true);
            holder.btnAction.setAlpha(1.0f);
        }
        else if (s.getStatus().equals("cancelled")) {
            holder.btnAction.setText("Reschedule");
            holder.btnAction.setVisibility(View.VISIBLE);
            holder.btnAction.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#448AFF")));
            holder.btnAction.setTextColor(Color.WHITE);
            holder.btnAction.setEnabled(true);
            holder.btnAction.setAlpha(1.0f);
        }
        else if (s.getStatus().equals("completed")) {
            holder.btnAction.setText("Completed");
            holder.btnAction.setVisibility(View.VISIBLE);
            holder.btnAction.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#66BB6A")));
            holder.btnAction.setTextColor(Color.WHITE);
            holder.btnAction.setEnabled(false);
            holder.btnAction.setClickable(false);
            holder.btnAction.setAlpha(0.85f);
        }

        holder.btnAction.setOnClickListener(v -> listener.onActionClick(s));
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHospital, tvDoctor, tvSpecialty, tvDateTime, tvType;
        Button btnAction;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHospital = itemView.findViewById(R.id.tvHospital);
            tvDoctor = itemView.findViewById(R.id.tvDoctor);
            tvSpecialty = itemView.findViewById(R.id.tvSpecialty);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvType = itemView.findViewById(R.id.tvType);
            btnAction = itemView.findViewById(R.id.btnAction);
        }
    }
}