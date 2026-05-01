package com.example.healthcareapp;

import java.io.Serializable;

public class Schedule implements Serializable {
    private String id;
    private String doctorName;
    private String specialty;
    private String hospital;
    private String date;
    private String time;
    private String type;
    private String status;

    public Schedule(String id, String doctorName, String specialty, String hospital, String date, String time, String type, String status) {
        this.id = id;
        this.doctorName = doctorName;
        this.specialty = specialty;
        this.hospital = hospital;
        this.date = date;
        this.time = time;
        this.type = type;
        this.status = status;
    }

    public String getId() { return id; }
    public String getDoctorName() { return doctorName; }
    public String getSpecialty() { return specialty; }
    public String getHospital() { return hospital; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getType() { return type; }
    public String getStatus() { return status; }
}