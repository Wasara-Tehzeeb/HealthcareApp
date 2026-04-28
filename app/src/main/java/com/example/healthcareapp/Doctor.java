package com.example.healthcareapp;

public class Doctor {
    private String name;
    private String specialty;
    private int imageResId;
    private String hospital;
    private String phone;
    private String bio;

    public Doctor(String name, String specialty, int imageResId, String hospital, String phone, String bio) {
        this.name = name;
        this.specialty = specialty;
        this.imageResId = imageResId;
        this.hospital = hospital;
        this.phone = phone;
        this.bio = bio;
    }

    public String getName() { return name; }
    public String getSpecialty() { return specialty; }
    public int getImageResId() { return imageResId; }
    public String getHospital() { return hospital; }
    public String getPhone() { return phone; }
    public String getBio() { return bio; }
}