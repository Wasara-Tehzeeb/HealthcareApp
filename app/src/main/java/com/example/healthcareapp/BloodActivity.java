package com.example.healthcareapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class BloodActivity extends AppCompatActivity {

    private static final String HEALTH_PREFS = "HealthAppPrefs";

    private TextView tvQtyAP, tvQtyAN, tvQtyBP, tvQtyBN, tvQtyABP, tvQtyABN, tvQtyOP, tvQtyON;
    private Spinner spinnerBloodType;
    private EditText etQuantity;
    private MaterialButton btnRequestBlood;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood);

        tvQtyAP = findViewById(R.id.tvQtyAP);
        tvQtyAN = findViewById(R.id.tvQtyAN);
        tvQtyBP = findViewById(R.id.tvQtyBP);
        tvQtyBN = findViewById(R.id.tvQtyBN);
        tvQtyABP = findViewById(R.id.tvQtyABP);
        tvQtyABN = findViewById(R.id.tvQtyABN);
        tvQtyOP = findViewById(R.id.tvQtyOP);
        tvQtyON = findViewById(R.id.tvQtyON);
        ivBack = findViewById(R.id.ivBack);

        spinnerBloodType = findViewById(R.id.spinnerBloodType);
        etQuantity = findViewById(R.id.etQuantity);
        btnRequestBlood = findViewById(R.id.btnRequestBlood);

        ivBack.setOnClickListener(v -> finish());

        String[] bloodTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bloodTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodType.setAdapter(adapter);

        loadBloodInventory();

        btnRequestBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestBlood();
            }
        });
    }

    private void loadBloodInventory() {
        SharedPreferences sharedPreferences = getSharedPreferences(HEALTH_PREFS, Context.MODE_PRIVATE);

        tvQtyAP.setText(String.valueOf(sharedPreferences.getInt("blood_A_plus", 0)));
        tvQtyAN.setText(String.valueOf(sharedPreferences.getInt("blood_A_minus", 0)));
        tvQtyBP.setText(String.valueOf(sharedPreferences.getInt("blood_B_plus", 0)));
        tvQtyBN.setText(String.valueOf(sharedPreferences.getInt("blood_B_minus", 0)));
        tvQtyABP.setText(String.valueOf(sharedPreferences.getInt("blood_AB_plus", 0)));
        tvQtyABN.setText(String.valueOf(sharedPreferences.getInt("blood_AB_minus", 0)));
        tvQtyOP.setText(String.valueOf(sharedPreferences.getInt("blood_O_plus", 0)));
        tvQtyON.setText(String.valueOf(sharedPreferences.getInt("blood_O_minus", 0)));
    }

    private String getKeyForBloodType(String bloodType) {
        switch (bloodType) {
            case "A+": return "blood_A_plus";
            case "A-": return "blood_A_minus";
            case "B+": return "blood_B_plus";
            case "B-": return "blood_B_minus";
            case "AB+": return "blood_AB_plus";
            case "AB-": return "blood_AB_minus";
            case "O+": return "blood_O_plus";
            case "O-": return "blood_O_minus";
            default: return "";
        }
    }

    private void requestBlood() {
        String selectedBloodType = spinnerBloodType.getSelectedItem().toString();
        String quantityStr = etQuantity.getText().toString().trim();

        if (quantityStr.isEmpty()) {
            Toast.makeText(this, "Please enter quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        int requestedQty = Integer.parseInt(quantityStr);

        if (requestedQty <= 0) {
            Toast.makeText(this, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences(HEALTH_PREFS, Context.MODE_PRIVATE);

        String key = getKeyForBloodType(selectedBloodType);

        int currentQty = sharedPreferences.getInt(key, 0);

        if (currentQty >= requestedQty) {
            int newQty = currentQty - requestedQty;

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, newQty);
            editor.apply();

            loadBloodInventory();
            etQuantity.setText("");

            Toast.makeText(this, "Successfully requested " + requestedQty + " units of " + selectedBloodType, Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Insufficient stock! Only " + currentQty + " units available.", Toast.LENGTH_LONG).show();
        }
    }
}