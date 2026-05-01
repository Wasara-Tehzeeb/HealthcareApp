package com.example.healthcareapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class NotificationHelper {
    private static final String PREF_NAME = "HealthCarePrefs";
    private static final String NOTIFICATION_PREFIX = "notifications_";

    public static void saveNotification(Context context, String email, Notification notification) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String key = NOTIFICATION_PREFIX + email;
        String existing = prefs.getString(key, "");

        String notificationStr = notification.getId() + "|" +
                notification.getTitle() + "|" +
                notification.getMessage() + "|" +
                notification.getTime() + "|" +
                notification.getDate() + "|" +
                notification.getType() + "|" +
                notification.getDoctorName();

        SharedPreferences.Editor editor = prefs.edit();
        if (existing.isEmpty()) {
            editor.putString(key, notificationStr);
        }
        else {
            editor.putString(key, existing + ";" + notificationStr);
        }
        editor.apply();
    }

    public static List<Notification> getNotifications(Context context, String email) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String key = NOTIFICATION_PREFIX + email;
        String data = prefs.getString(key, "");

        List<Notification> notifications = new ArrayList<>();
        if (data.isEmpty()) return notifications;

        String[] items = data.split(";");
        for (String item : items) {
            String[] parts = item.split("\\|");
            if (parts.length >= 7) {
                Notification notification = new Notification();
                notification.setId(parts[0]);
                notification.setTitle(parts[1]);
                notification.setMessage(parts[2]);
                notification.setTime(parts[3]);
                notification.setDate(parts[4]);
                notification.setType(parts[5]);
                notification.setDoctorName(parts[6]);
                notifications.add(notification);
            }
        }
        return notifications;
    }

    public static void deleteNotification(Context context, String email, String notificationId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String key = NOTIFICATION_PREFIX + email;
        String data = prefs.getString(key, "");

        if (data.isEmpty()) return;

        StringBuilder newData = new StringBuilder();
        String[] items = data.split(";");
        for (String item : items) {
            String[] parts = item.split("\\|");
            if (parts.length >= 7 && !parts[0].equals(notificationId)) {
                if (newData.length() > 0) {
                    newData.append(";");
                }
                newData.append(item);
            }
        }

        prefs.edit().putString(key, newData.toString()).apply();
    }

    public static int getNotificationCount(Context context, String email) {
        return getNotifications(context, email).size();
    }
}