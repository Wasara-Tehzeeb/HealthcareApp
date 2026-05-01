package com.example.healthcareapp;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class MessageFragment extends Fragment implements NotificationAdapter.OnNotificationClickListener {

    private RecyclerView rvNotifications;
    private View layoutNoNotifications;
    private TextView tvNoNotificationsTitle;
    private TextView tvNoNotificationsSubtitle;
    private TextView tvNotificationCount;
    private String loggedInUserEmail;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        tvNotificationCount = view.findViewById(R.id.tvNotificationCount);
        rvNotifications = view.findViewById(R.id.rvNotifications);
        layoutNoNotifications = view.findViewById(R.id.layoutNoNotifications);
        tvNoNotificationsTitle = view.findViewById(R.id.tvNoNotificationsTitle);
        tvNoNotificationsSubtitle = view.findViewById(R.id.tvNoNotificationsSubtitle);

        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNotifications.setHasFixedSize(true);

        SharedPreferences prefs = requireActivity().getSharedPreferences("HealthCarePrefs", Context.MODE_PRIVATE);
        loggedInUserEmail = prefs.getString("currentUserEmail", "");

        loadNotifications();

        return view;
    }

    private void loadNotifications() {
        notificationList = NotificationHelper.getNotifications(getContext(), loggedInUserEmail);
        Collections.reverse(notificationList);

        if (tvNotificationCount != null) {
            int count = notificationList.size();
            tvNotificationCount.setText(count + " notification" + (count != 1 ? "s" : ""));
        }

        if (notificationList.isEmpty()) {
            rvNotifications.setVisibility(View.GONE);
            layoutNoNotifications.setVisibility(View.VISIBLE);
        }
        else {
            rvNotifications.setVisibility(View.VISIBLE);
            layoutNoNotifications.setVisibility(View.GONE);
            adapter = new NotificationAdapter(getContext(), notificationList, this);
            rvNotifications.setAdapter(adapter);
        }
    }

    @Override
    public void onDeleteClick(Notification notification, int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Notification")
                .setMessage("Are you sure you want to delete this notification?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    NotificationHelper.deleteNotification(getContext(), loggedInUserEmail, notification.getId());
                    notificationList.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, notificationList.size());

                    int count = notificationList.size();
                    tvNotificationCount.setText(count + " notification" + (count != 1 ? "s" : ""));

                    if (notificationList.isEmpty()) {
                        rvNotifications.setVisibility(View.GONE);
                        layoutNoNotifications.setVisibility(View.VISIBLE);
                    }

                    Toast.makeText(getContext(), "Notification removed", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onNotificationClick(Notification notification) {
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNotifications();
    }
}