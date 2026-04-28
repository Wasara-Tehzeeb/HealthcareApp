package com.example.healthcareapp;

public class Medicine {
    public String name;
    public String dosage;
    public String frequency;

    public Medicine(String name, String dosage, String frequency) {
        this.name = name;
        this.dosage = dosage;
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public String getDosage() {
        return dosage;
    }

    public String getFrequency() {
        return frequency;
    }
}