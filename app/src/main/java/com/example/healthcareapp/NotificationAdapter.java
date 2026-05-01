package com.example.healthcareapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final Context context;
    private final List<Notification> notificationList;
    private final OnNotificationClickListener listener;

    public interface OnNotificationClickListener {
        void onDeleteClick(Notification notification, int position);
        void onNotificationClick(Notification notification);
    }

    public NotificationAdapter(Context context, List<Notification> notificationList, OnNotificationClickListener listener) {
        this.context = context;
        this.notificationList = notificationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        holder.tvTitle.setText(notification.getTitle());
        holder.tvMessage.setText(notification.getMessage());
        holder.tvDoctorName.setText(notification.getDoctorName());
        holder.tvType.setText(notification.getType());

        holder.tvDateTime.setText(notification.getDate() + ", " + notification.getTime());

        holder.btnAction.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(notification, position);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNotificationClick(notification);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvMessage;
        TextView tvDoctorName;
        TextView tvDateTime;
        TextView tvType;
        Button btnAction;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvType = itemView.findViewById(R.id.tvType);
            btnAction = itemView.findViewById(R.id.btnAction);
        }
    }
}