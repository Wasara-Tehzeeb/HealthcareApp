package com.example.healthcareapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionActivity extends AppCompatActivity {

    private static final String AUTH_PREFS = "HealthCarePrefs";
    private static final String HEALTH_PREFS = "HealthAppPrefs";

    private RecyclerView rvMedicines, rvDietPlan;
    private TextView tvEmptyMedicines, tvEmptyDiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        rvMedicines = findViewById(R.id.rvMedicines);
        rvDietPlan = findViewById(R.id.rvDietPlan);
        tvEmptyMedicines = findViewById(R.id.tvEmptyMedicines);
        tvEmptyDiet = findViewById(R.id.tvEmptyDiet);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        SharedPreferences authPrefs = getSharedPreferences(AUTH_PREFS, MODE_PRIVATE);
        String email = authPrefs.getString("currentUserEmail", "");

        SharedPreferences healthPrefs = getSharedPreferences(HEALTH_PREFS, MODE_PRIVATE);

        String medKey = "prescription_" + email;
        String medData = healthPrefs.getString(medKey, "");
        List<Medicine> medicineList = parseMedicines(medData);

        if (medicineList.isEmpty()) {
            tvEmptyMedicines.setVisibility(View.VISIBLE);
            rvMedicines.setVisibility(View.GONE);
        }
        else {
            rvMedicines.setVisibility(View.VISIBLE);
            rvMedicines.setHasFixedSize(true);
            MedicineAdapter medAdapter = new MedicineAdapter(medicineList);
            rvMedicines.setLayoutManager(new LinearLayoutManager(this));
            rvMedicines.setAdapter(medAdapter);
        }

        String dietKey = "diet_plan_" + email;
        String dietData = healthPrefs.getString(dietKey, "");
        List<DietItem> dietList = parseDiet(dietData);

        if (dietList.isEmpty()) {
            tvEmptyDiet.setVisibility(View.VISIBLE);
            rvDietPlan.setVisibility(View.GONE);
        }
        else {
            rvDietPlan.setVisibility(View.VISIBLE);
            rvDietPlan.setHasFixedSize(true);
            DietAdapter dietAdapter = new DietAdapter(dietList);
            rvDietPlan.setLayoutManager(new LinearLayoutManager(this));
            rvDietPlan.setAdapter(dietAdapter);
        }
    }

    private List<Medicine> parseMedicines(String data) {
        List<Medicine> list = new ArrayList<>();
        if (data == null || data.isEmpty()) return list;
        String[] medicines = data.split(";");
        for (String med : medicines) {
            String[] details = med.split("\\|");
            if (details.length == 3) {
                list.add(new Medicine(details[0], details[1], details[2]));
            }
        }
        return list;
    }

    private List<DietItem> parseDiet(String data) {
        List<DietItem> list = new ArrayList<>();
        if (data == null || data.isEmpty()) return list;
        String[] items = data.split("\\|");
        for (String item : items) {
            if (!item.trim().isEmpty()) {
                list.add(new DietItem(item.trim()));
            }
        }
        return list;
    }
}