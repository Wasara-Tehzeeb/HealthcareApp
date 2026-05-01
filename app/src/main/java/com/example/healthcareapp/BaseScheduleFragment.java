package com.example.healthcareapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
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

                    String cancelMessage = "Your " + schedule.getType().toLowerCase() + " with " + schedule.getDoctorName() + " has been cancelled.";
                    Calendar now = Calendar.getInstance();
                    String currentTimeStr = String.format(Locale.getDefault(), "%02d:%02d", now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));

                    NotificationHelper.saveNotification(getContext(), email,
                            new Notification(
                                    "NC" + System.currentTimeMillis(),
                                    "Appointment Cancelled",
                                    cancelMessage,
                                    currentTimeStr,
                                    ScheduleHelper.getTodayDate(),
                                    schedule.getType(),
                                    schedule.getDoctorName()
                            ));

                    showSystemNotification("Appointment Cancelled", cancelMessage);

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

            String readableDate = ScheduleHelper.formatDisplayDate(newDate);
            String readableTime = ScheduleHelper.formatTime(newTime);
            String rescheduleMessage = "Your " + schedule.getType().toLowerCase() + " with " + schedule.getDoctorName() + " has been rescheduled to " + readableDate + ", " + readableTime + ".";

            NotificationHelper.saveNotification(getContext(), email,
                    new Notification(
                            "NR" + System.currentTimeMillis(),
                            "Appointment Rescheduled",
                            rescheduleMessage,
                            newTime,
                            newDate,
                            schedule.getType(),
                            schedule.getDoctorName()
                    ));

            showSystemNotification("Appointment Rescheduled", rescheduleMessage);

            dialog.dismiss();
            loadData();
            Toast.makeText(getContext(), "Successfully Rescheduled!", Toast.LENGTH_SHORT).show();
        });
    }

    private void showSystemNotification(String title, String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requireActivity().requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
                return;
            }
        }

        String CHANNEL_ID = "schedule_updates";
        NotificationManager manager = requireContext().getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Schedule Updates", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Updates about appointment cancellations and rescheduling");
            manager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(requireContext(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_new)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat.from(requireContext()).notify(1002, builder.build());
    }
}