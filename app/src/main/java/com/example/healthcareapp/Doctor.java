package com.example.healthcareapp;

public class Doctor {
    private String name;
    private String specialty;
    private int imageResId;
    private String hospital;
    private String phone;
    private String email;
    private double rating;
    private String experience;
    private String bio;

    public Doctor(String name, String specialty, int imageResId, String hospital, String phone, String email, double ratings, String experience, String bio) {
        this.name = name;
        this.specialty = specialty;
        this.imageResId = imageResId;
        this.hospital = hospital;
        this.phone = phone;
        this.email = email;
        this.rating = ratings;
        this.experience = experience;
        this.bio = bio;
    }

    public String getName() { return name; }
    public String getSpecialty() { return specialty; }
    public int getImageResId() { return imageResId; }
    public String getHospital() { return hospital; }
    public String getPhone() { return phone; }

    public String getEmail() {
        return email;
    }
    public double getRating() {
        return rating;
    }

    public String getExperience() {
        return experience;
    }

    public String getBio() { return bio; }
}