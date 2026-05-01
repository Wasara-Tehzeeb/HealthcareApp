package com.example.healthcareapp;

public class Notification {
    private String id;
    private String title;
    private String message;
    private String time;
    private String date;
    private String type;
    private String doctorName;

    public Notification() {
    }

    public Notification(String id, String title, String message, String time, String date, String type, String doctorName) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.time = time;
        this.date = date;
        this.type = type;
        this.doctorName = doctorName;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
}