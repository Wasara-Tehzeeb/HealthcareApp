package com.example.healthcareapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.healthcareapp.R;
import com.example.healthcareapp.ScheduleAdapter;
import com.example.healthcareapp.ScheduleHelper;
import com.example.healthcareapp.Schedule;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public abstract class BaseScheduleFragment extends Fragment implements ScheduleAdapter.OnActionListener {

    protected RecyclerView recyclerView;
    protected TextView tvEmpty;
    protected String email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_list, container, false);
        recyclerView = view.findViewById(R.id.rvSchedules);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        email = requireActivity().getSharedPreferences("HealthCarePrefs", Context.MODE_PRIVATE)
                .getString("currentUserEmail", "");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadData();
    }

    protected void loadData() {
        List<Schedule> list = getSchedulesList();
        if (list.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setAdapter(new ScheduleAdapter(getContext(), list, this));
        }
    }

    public abstract List<Schedule> getSchedulesList();

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onActionClick(Schedule schedule) {
        if (schedule.getStatus().equals("completed")) {
            return;
        } else if (schedule.getStatus().equals("upcoming")) {
            showCancelDialog(schedule);
        } else if (schedule.getStatus().equals("cancelled")) {
            showRescheduleDialog(schedule);
        }
    }

    private void showCancelDialog(Schedule schedule) {
        new AlertDialog.Builder(getContext())
                .setTitle("Cancel Appointment")
                .setMessage("Are you sure you want to cancel?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    ScheduleHelper.updateScheduleStatus(getContext(), email, schedule.getId(), "cancelled");
                    loadData();
                    Toast.makeText(getContext(), "Appointment Cancelled", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null).show();
    }

    private void showRescheduleDialog(Schedule schedule) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_reschedule, null);
        DatePicker datePicker = dialogView.findViewById(R.id.datePickerReschedule);
        TimePicker timePicker = dialogView.findViewById(R.id.timePickerReschedule);

        datePicker.setMinDate(System.currentTimeMillis() - 1000);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Reschedule Appointment")
                .setView(dialogView)
                .setPositiveButton("Confirm", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            Calendar selectedDateTime = Calendar.getInstance();
            selectedDateTime.set(year, month, day, hour, minute, 0);
            selectedDateTime.set(Calendar.MILLISECOND, 0);

            Calendar currentTime = Calendar.getInstance();

            if (selectedDateTime.before(currentTime)) {
                Toast.makeText(getContext(), "Cannot select a past date and time!", Toast.LENGTH_SHORT).show();
                return;
            }

            String newDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year);
            String newTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

            ScheduleHelper.rescheduleAppointment(getContext(), email, schedule.getId(), newDate, newTime);

            dialog.dismiss();
            loadData();
            Toast.makeText(getContext(), "Successfully Rescheduled!", Toast.LENGTH_SHORT).show();
        });
    }
}