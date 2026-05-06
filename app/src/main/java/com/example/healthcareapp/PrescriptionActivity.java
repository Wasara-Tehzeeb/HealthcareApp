package com.example.healthcareapp;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionActivity extends AppCompatActivity {

    private static final String AUTH_PREFS = "HealthCarePrefs";
    private static final String HEALTH_PREFS = "HealthAppPrefs";

    private RecyclerView rvMedicines, rvDietPlan;
    private TextView tvEmptyMedicines, tvEmptyDiet;

    private List<Medicine> medicineList;
    private MedicineAdapter medAdapter;
    private String medKey;

    private List<DietItem> dietList;
    private DietAdapter dietAdapter;
    private String dietKey;

    private SharedPreferences healthPrefs;

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

        healthPrefs = getSharedPreferences(HEALTH_PREFS, MODE_PRIVATE);
        medKey = "prescription_" + email;
        dietKey = "diet_plan_" + email;

        String medData = healthPrefs.getString(medKey, "");
        medicineList = parseMedicines(medData);

        rvMedicines.setHasFixedSize(true);
        rvMedicines.setLayoutManager(new LinearLayoutManager(this));

        medAdapter = new MedicineAdapter(medicineList, position -> {
            medicineList.remove(position);
            saveMedicinesToPrefs();
            medAdapter.notifyDataSetChanged();
            updateMedicineUI();
            Toast.makeText(this, "Medicine removed", Toast.LENGTH_SHORT).show();
        });
        rvMedicines.setAdapter(medAdapter);
        updateMedicineUI();

        FloatingActionButton fabAddMedicine = findViewById(R.id.fabAddMedicine);
        fabAddMedicine.setOnClickListener(v -> showAddMedicineDialog());

        String dietData = healthPrefs.getString(dietKey, "");
        dietList = parseDiet(dietData);

        rvDietPlan.setHasFixedSize(true);
        rvDietPlan.setLayoutManager(new LinearLayoutManager(this));

        dietAdapter = new DietAdapter(dietList, position -> {
            dietList.remove(position);
            saveDietToPrefs();
            dietAdapter.notifyDataSetChanged();
            updateDietUI();
            Toast.makeText(this, "Risk factor removed", Toast.LENGTH_SHORT).show();
        });
        rvDietPlan.setAdapter(dietAdapter);
        updateDietUI();

        FloatingActionButton fabAddDiet = findViewById(R.id.fabAddDiet);
        fabAddDiet.setOnClickListener(v -> showAddDietDialog());
    }

    private void showAddMedicineDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Medicine");
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_medicine, null);
        TextInputEditText etName = dialogView.findViewById(R.id.etMedName);
        TextInputEditText etDosage = dialogView.findViewById(R.id.etMedDosage);
        TextInputEditText etFreq = dialogView.findViewById(R.id.etMedFreq);
        builder.setView(dialogView);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String dosage = etDosage.getText().toString().trim();
            String frequency = etFreq.getText().toString().trim();
            if (name.isEmpty() || dosage.isEmpty() || frequency.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            String existingData = healthPrefs.getString(medKey, "");
            String newMedicine = name + "|" + dosage + "|" + frequency;
            String updatedData = existingData.isEmpty() ? newMedicine : existingData + ";" + newMedicine;
            healthPrefs.edit().putString(medKey, updatedData).apply();

            medicineList.clear();
            medicineList.addAll(parseMedicines(updatedData));
            medAdapter.notifyDataSetChanged();
            updateMedicineUI();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void saveMedicinesToPrefs() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < medicineList.size(); i++) {
            Medicine m = medicineList.get(i);
            sb.append(m.getName()).append("|").append(m.getDosage()).append("|").append(m.getFrequency());
            if (i < medicineList.size() - 1) {
                sb.append(";");
            }
        }
        healthPrefs.edit().putString(medKey, sb.toString()).apply();
    }

    private void updateMedicineUI() {
        if (medicineList.isEmpty()) {
            tvEmptyMedicines.setVisibility(View.VISIBLE);
            rvMedicines.setVisibility(View.GONE);
        } else {
            tvEmptyMedicines.setVisibility(View.GONE);
            rvMedicines.setVisibility(View.VISIBLE);
        }
    }

    private void showAddDietDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Risk Factor");
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_diet, null);
        TextInputEditText etDietName = dialogView.findViewById(R.id.etDietName);
        builder.setView(dialogView);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String itemName = etDietName.getText().toString().trim();
            if (itemName.isEmpty()) {
                Toast.makeText(this, "Please enter a risk factor", Toast.LENGTH_SHORT).show();
                return;
            }
            String existingData = healthPrefs.getString(dietKey, "");
            String updatedData = existingData.isEmpty() ? itemName : existingData + "|" + itemName;
            healthPrefs.edit().putString(dietKey, updatedData).apply();

            dietList.clear();
            dietList.addAll(parseDiet(updatedData));
            dietAdapter.notifyDataSetChanged();
            updateDietUI();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void saveDietToPrefs() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dietList.size(); i++) {
            sb.append(dietList.get(i).getItemName());
            if (i < dietList.size() - 1) {
                sb.append("|");
            }
        }
        healthPrefs.edit().putString(dietKey, sb.toString()).apply();
    }

    private void updateDietUI() {
        if (dietList.isEmpty()) {
            tvEmptyDiet.setVisibility(View.VISIBLE);
            rvDietPlan.setVisibility(View.GONE);
        } else {
            tvEmptyDiet.setVisibility(View.GONE);
            rvDietPlan.setVisibility(View.VISIBLE);
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