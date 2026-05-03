package com.example.healthcareapp;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.healthcareapp.Schedule;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleHelper {
    private static final String PREF_NAME = "HealthCarePrefs";
    private static final String KEY_PREFIX = "schedules_";

    public static void autoExpireUpcomingSchedules(Context context, String email) {
        List<Schedule> list = getSchedules(context, email);
        Calendar currentTime = Calendar.getInstance();
        boolean changed = false;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        sdf.setLenient(false); // Strict parsing

        Calendar now = Calendar.getInstance();
        String currentTimeStr = String.format(Locale.getDefault(), "%02d:%02d", now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
        String todayDate = getTodayDate();

        for (int i = 0; i < list.size(); i++) {
            Schedule s = list.get(i);

            if (s.getStatus().equals("upcoming")) {
                try {
                    Date scheduleDate = sdf.parse(s.getDate() + " " + s.getTime());

                    if (scheduleDate != null && scheduleDate.before(currentTime.getTime())) {
                        Schedule expiredSchedule = new Schedule(
                                s.getId(), s.getDoctorName(), s.getSpecialty(),
                                s.getHospital(), s.getDate(), s.getTime(),
                                s.getType(), "cancelled"
                        );
                        list.set(i, expiredSchedule);
                        changed = true;

                        String missedMessage = "You missed your " + s.getType().toLowerCase()
                                + " appointment with " + s.getDoctorName()
                                + " scheduled on " + formatDisplayDate(s.getDate())
                                + " at " + formatTime(s.getTime()) + ".";

                        NotificationHelper.saveNotification(context, email,
                                new Notification(
                                        "NM" + System.currentTimeMillis(),
                                        "Missed Appointment",
                                        missedMessage,
                                        currentTimeStr,
                                        todayDate,
                                        s.getType(),
                                        s.getDoctorName()
                                ));
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (changed) {
            saveList(context, email, list);
        }
    }

    public static void saveSchedule(Context context, String email, Schedule schedule) {
        List<Schedule> list = getSchedules(context, email);
        list.add(schedule);
        saveList(context, email, list);
    }

    public static void updateScheduleStatus(Context context, String email, String scheduleId, String newStatus) {
        List<Schedule> list = getSchedules(context, email);
        for (Schedule s : list) {
            if (s.getId().equals(scheduleId)) {
                list.remove(s);
                Schedule updated = new Schedule(s.getId(), s.getDoctorName(), s.getSpecialty(), s.getHospital(), s.getDate(), s.getTime(), s.getType(), newStatus);
                list.add(updated);
                break;
            }
        }
        saveList(context, email, list);
    }

    public static List<Schedule> getSchedules(Context context, String email) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String data = prefs.getString(KEY_PREFIX + email, "");
        List<Schedule> list = new ArrayList<>();
        if (!data.isEmpty()) {
            for (String item : data.split(";")) {
                String[] parts = item.split("\\|");
                if (parts.length == 8) {
                    list.add(new Schedule(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]));
                }
            }
        }
        return list;
    }

    private static void saveList(Context context, String email, List<Schedule> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(";");
            Schedule s = list.get(i);
            sb.append(s.getId()).append("|").append(s.getDoctorName()).append("|")
                    .append(s.getSpecialty()).append("|").append(s.getHospital()).append("|")
                    .append(s.getDate()).append("|").append(s.getTime()).append("|")
                    .append(s.getType()).append("|").append(s.getStatus());
        }
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                .putString(KEY_PREFIX + email, sb.toString()).apply();
    }

    public static List<Schedule> getUpcomingSchedules(Context context, String email) {
        List<Schedule> all = getSchedules(context, email);
        List<Schedule> upcoming = new ArrayList<>();

        for (Schedule s : all) {
            if (s.getStatus().equals("upcoming")) {
                upcoming.add(s);
            }
        }
        return sortList(upcoming);
    }

    public static List<Schedule> getCompletedSchedules(Context context, String email) {
        List<Schedule> all = getSchedules(context, email);
        List<Schedule> completed = new ArrayList<>();
        for (Schedule s : all) { if (s.getStatus().equals("completed")) completed.add(s); }
        return sortListDesc(completed);
    }

    public static List<Schedule> getCancelledSchedules(Context context, String email) {
        List<Schedule> all = getSchedules(context, email);
        List<Schedule> cancelled = new ArrayList<>();
        for (Schedule s : all) { if (s.getStatus().equals("cancelled")) cancelled.add(s); }
        return sortListDesc(cancelled);
    }

    private static List<Schedule> sortList(List<Schedule> list) {
        Collections.sort(list, (s1, s2) -> {
            int dateComp = s1.getDate().compareTo(s2.getDate());
            return dateComp != 0 ? dateComp : s1.getTime().compareTo(s2.getTime());
        });
        return list;
    }

    private static List<Schedule> sortListDesc(List<Schedule> list) {
        Collections.sort(list, (s1, s2) -> {
            int dateComp = s2.getDate().compareTo(s1.getDate());
            return dateComp != 0 ? dateComp : s2.getTime().compareTo(s1.getTime());
        });
        return list;
    }

    public static String formatTime(String time) {
        try {
            String[] parts = time.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            String ampm = hour >= 12 ? "PM" : "AM";
            hour = hour % 12;
            if (hour == 0) hour = 12;
            return String.format("%d:%02d %s", hour, minute, ampm);
        } catch (Exception e) { return time; }
    }

    public static String formatDisplayDate(String date) {
        try {
            SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            return output.format(input.parse(date));
        } catch (Exception e) { return date; }
    }

    public static void rescheduleAppointment(Context context, String email, String scheduleId, String newDate, String newTime) {
        List<Schedule> list = getSchedules(context, email);
        for (Schedule s : list) {
            if (s.getId().equals(scheduleId)) {
                list.remove(s);
                Schedule updated = new Schedule(s.getId(), s.getDoctorName(), s.getSpecialty(), s.getHospital(), newDate, newTime, s.getType(), "upcoming");
                list.add(updated);
                break;
            }
        }
        saveList(context, email, list);
    }

    public static String getTodayDate() {
        return new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(new java.util.Date());
    }
}