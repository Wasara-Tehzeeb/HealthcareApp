package com.example.healthcareapp;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookAppointmentActivity extends AppCompatActivity {

    private TextInputEditText etDate, etTime;
    private RadioGroup rgAppointmentType;

    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private boolean isDateSelected = false, isTimeSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        Calendar defaultCal = Calendar.getInstance();
        selectedYear = defaultCal.get(Calendar.YEAR);
        selectedMonth = defaultCal.get(Calendar.MONTH);
        selectedDay = defaultCal.get(Calendar.DAY_OF_MONTH);
        selectedHour = defaultCal.get(Calendar.HOUR_OF_DAY);
        selectedMinute = defaultCal.get(Calendar.MINUTE);

        ImageView ivBack = findViewById(R.id.ivBackBook);
        ivBack.setOnClickListener(v -> finish());

        String doctorName = getIntent().getStringExtra("name");
        String doctorSpecialty = getIntent().getStringExtra("specialty");
        String doctorHospital = getIntent().getStringExtra("hospital");
        int doctorImage = getIntent().getIntExtra("image", R.drawable.account);
        double doctorRating = getIntent().getDoubleExtra("rating", 0.0);

        ImageView ivDoctorImage = findViewById(R.id.ivBookDoctorImage);
        TextView tvDoctorName = findViewById(R.id.tvBookDoctorName);
        TextView tvDoctorSpecialty = findViewById(R.id.tvBookDoctorSpecialty);
        TextView tvDoctorRating = findViewById(R.id.tvBookDoctorRating);
        TextView tvDoctorHospital = findViewById(R.id.tvBookDoctorHospital);

        ivDoctorImage.setImageResource(doctorImage);
        if (doctorName != null) tvDoctorName.setText(doctorName);
        if (doctorSpecialty != null) tvDoctorSpecialty.setText(doctorSpecialty);
        if (doctorHospital != null) tvDoctorHospital.setText(doctorHospital);
        tvDoctorRating.setText(doctorRating + " ★");

        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        rgAppointmentType = findViewById(R.id.rgAppointmentType);
        MaterialButton btnConfirm = findViewById(R.id.btnConfirmBooking);

        etDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        selectedYear = year;
                        selectedMonth = month;
                        selectedDay = dayOfMonth;
                        isDateSelected = true;

                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                        Calendar selectedCal = Calendar.getInstance();
                        selectedCal.set(year, month, dayOfMonth);
                        etDate.setText(sdf.format(selectedCal.getTime()));
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        etTime.setOnClickListener(v -> {
            Calendar rightNow = Calendar.getInstance();
            boolean isToday = (selectedYear == rightNow.get(Calendar.YEAR)) &&
                    (selectedMonth == rightNow.get(Calendar.MONTH)) &&
                    (selectedDay == rightNow.get(Calendar.DAY_OF_MONTH));

            int startHour = isToday ? rightNow.get(Calendar.HOUR_OF_DAY) : 0;
            int startMinute = isToday ? rightNow.get(Calendar.MINUTE) : 0;

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (timeView, hourOfDay, minute) -> {
                        selectedHour = hourOfDay;
                        selectedMinute = minute;
                        isTimeSelected = true;

                        String amPm = hourOfDay >= 12 ? "PM" : "AM";
                        int displayHour = hourOfDay % 12;
                        if (displayHour == 0) displayHour = 12;
                        etTime.setText(String.format("%02d:%02d %s", displayHour, minute, amPm));
                    },
                    startHour,
                    startMinute,
                    false
            );

            if (isToday) {
                timePickerDialog.updateTime(startHour, startMinute);
            }

            timePickerDialog.show();
        });

        btnConfirm.setOnClickListener(v -> {
            int selectedTypeId = rgAppointmentType.getCheckedRadioButtonId();
            if (selectedTypeId == -1) {
                Toast.makeText(this, "Please select an appointment type", Toast.LENGTH_SHORT).show();
                return;
            }
            MaterialRadioButton selectedTypeBtn = findViewById(selectedTypeId);
            String appointmentType = selectedTypeBtn.getText().toString();

            if (!isDateSelected || !isTimeSelected) {
                Toast.makeText(this, "Please select both date and time", Toast.LENGTH_SHORT).show();
                return;
            }

            Calendar selectedDateTime = Calendar.getInstance();
            selectedDateTime.set(Calendar.YEAR, selectedYear);
            selectedDateTime.set(Calendar.MONTH, selectedMonth);
            selectedDateTime.set(Calendar.DAY_OF_MONTH, selectedDay);
            selectedDateTime.set(Calendar.HOUR_OF_DAY, selectedHour);
            selectedDateTime.set(Calendar.MINUTE, selectedMinute);
            selectedDateTime.set(Calendar.SECOND, 0);
            selectedDateTime.set(Calendar.MILLISECOND, 0);

            Calendar rightNow = Calendar.getInstance();
            rightNow.set(Calendar.SECOND, 0);
            rightNow.set(Calendar.MILLISECOND, 0);

            if (selectedDateTime.before(rightNow)) {
                Toast.makeText(this, "Cannot book an appointment in the past!", Toast.LENGTH_LONG).show();
                return;
            }

            String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);

            SharedPreferences prefs = getSharedPreferences("HealthCarePrefs", Context.MODE_PRIVATE);
            String loggedInEmail = prefs.getString("currentUserEmail", "");

            if (loggedInEmail.isEmpty()) {
                Toast.makeText(this, "Error: User not logged in.", Toast.LENGTH_SHORT).show();
                return;
            }

            String scheduleId = "BK" + System.currentTimeMillis();
            Schedule newSchedule = new Schedule(scheduleId, doctorName, doctorSpecialty, doctorHospital, formattedDate, formattedTime, appointmentType, "upcoming");
            ScheduleHelper.saveSchedule(this, loggedInEmail, newSchedule);

            String notificationMessage = "Your " + appointmentType.toLowerCase() + " with " + doctorName + " has been booked.";
            NotificationHelper.saveNotification(this, loggedInEmail, new Notification("NT" + System.currentTimeMillis(), "Appointment Booked", notificationMessage, formattedTime, formattedDate, appointmentType, doctorName));

            String readableDate = ScheduleHelper.formatDisplayDate(formattedDate);
            String readableTime = ScheduleHelper.formatTime(formattedTime);
            showBookingNotification(doctorName, appointmentType, readableDate, readableTime);

            Toast.makeText(this, "Appointment Booked Successfully!", Toast.LENGTH_LONG).show();
            finish();
        });
    }

    private void showBookingNotification(String doctorName, String type, String date, String time) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
                return;
            }
        }

        String CHANNEL_ID = "booking_channel";
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Appointments", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Appointment booking confirmations");
            manager.createNotificationChannel(channel);
        }

        String fullMessage = "Your " + type.toLowerCase() + " with " + doctorName + " has been booked at " + date + ", " + time + ".";

        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_new)
                .setContentTitle("Appointment Booked!")
                .setContentText(fullMessage)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(fullMessage))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat.from(this).notify(1001, builder.build());
    }
}