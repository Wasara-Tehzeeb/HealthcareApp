package com.example.healthcareapp;

public class CheckupTest {
    private String testName;
    private String testDesc;
    private String testPrep;
    private String testPrice;
    private int icon;

    public CheckupTest(String testName, String testDesc, String testPrep, String testPrice, int icon) {
        this.testName = testName;
        this.testDesc = testDesc;
        this.testPrep = testPrep;
        this.testPrice = testPrice;
        this.icon = icon;
    }

    public String getTestName() { return testName; }
    public String getTestDesc() { return testDesc; }
    public String getTestPrep() { return testPrep; }
    public String getTestPrice() { return testPrice; }
    public int getIcon() { return icon; }
}