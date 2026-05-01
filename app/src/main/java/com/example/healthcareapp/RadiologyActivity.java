package com.example.healthcareapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RadiologyActivity extends AppCompatActivity implements CartAdapter.OnRemoveClickListener {

    private static final String HEALTH_PREFS = "HealthAppPrefs";
    private static final String ITEM_DELIMITER = "~~~";
    private static final String FIELD_DELIMITER = "|||";

    private String USER_CART_KEY;
    private ArrayList<CheckupTest> currentCartList; // Reusing the exact same data model!

    private RecyclerView rvCart;
    private TextView tvEmptyCart, tvCartCount;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radiology);

        String email = getIntent().getStringExtra("USER_EMAIL");
        if (email == null) email = "guest";

        USER_CART_KEY = "radiology_cart_" + email;

        rvCart = findViewById(R.id.rvCartList);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        tvCartCount = findViewById(R.id.tvCartCount);
        ivBack = findViewById(R.id.ivBack);

        rvCart.setHasFixedSize(true);
        rvCart.setLayoutManager(new LinearLayoutManager(this));

        ivBack.setOnClickListener(v -> finish());

        findViewById(R.id.btnAddRad1).setOnClickListener(v -> addToCart(
                new CheckupTest("Chest X-Ray", "Images of heart, lungs, and airways", "Remove jewelry & wear gown", "$50.00", R.drawable.radiology))
        );

        findViewById(R.id.btnAddRad2).setOnClickListener(v -> addToCart(
                new CheckupTest("MRI (Brain)", "Detailed images of brain tissues", "No metal objects allowed", "$250.00", R.drawable.radiology))
        );

        findViewById(R.id.btnAddRad3).setOnClickListener(v -> addToCart(
                new CheckupTest("CT Scan (Abdomen)", "Cross-sectional images of belly", "Fasting 4 hours required", "$180.00", R.drawable.radiology))
        );

        findViewById(R.id.btnAddRad4).setOnClickListener(v -> addToCart(
                new CheckupTest("Pelvic Ultrasound", "Uses sound waves to capture images", "Drink 4 glasses of water", "$120.00", R.drawable.radiology))
        );

        findViewById(R.id.btnAddRad5).setOnClickListener(v -> addToCart(
                new CheckupTest("MRI (Spine)", "Evaluates spinal cord and nerves", "Remove all metal objects", "$300.00", R.drawable.radiology))
        );

        findViewById(R.id.btnAddRad6).setOnClickListener(v -> addToCart(
                new CheckupTest("CT Scan (Head)", "Detailed cross-sections of the brain", "No jewelry or metal allowed", "$220.00", R.drawable.radiology))
        );

        findViewById(R.id.btnAddRad7).setOnClickListener(v -> addToCart(
                new CheckupTest("Abdominal Ultrasound", "Examines abdominal organs visually", "Fasting 6 hours required", "$150.00", R.drawable.radiology))
        );

        findViewById(R.id.btnAddRad8).setOnClickListener(v -> addToCart(
                new CheckupTest("Mammogram", "Screening for breast tissue abnormalities", "Do not apply deodorant on the day", "$200.00", R.drawable.radiology))
        );

        loadUserCart();
    }

    private void addToCart(CheckupTest test) {
        if (currentCartList == null) currentCartList = new ArrayList<>();

        for (CheckupTest t : currentCartList) {
            if (t.getTestName().equals(test.getTestName())) {
                Toast.makeText(this, "Already in cart", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        currentCartList.add(test);
        saveUserCart();
        Toast.makeText(this, test.getTestName() + " added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemoveClicked(CheckupTest test) {
        currentCartList.remove(test);
        saveUserCart();
        Toast.makeText(this, test.getTestName() + " removed", Toast.LENGTH_SHORT).show();
    }

    private void saveUserCart() {
        SharedPreferences prefs = getSharedPreferences(HEALTH_PREFS, Context.MODE_PRIVATE);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < currentCartList.size(); i++) {
            CheckupTest t = currentCartList.get(i);
            sb.append(t.getTestName()).append(FIELD_DELIMITER)
                    .append(t.getTestDesc()).append(FIELD_DELIMITER)
                    .append(t.getTestPrep()).append(FIELD_DELIMITER)
                    .append(t.getTestPrice()).append(FIELD_DELIMITER)
                    .append(t.getIcon());

            if (i < currentCartList.size() - 1) {
                sb.append(ITEM_DELIMITER);
            }
        }

        prefs.edit().putString(USER_CART_KEY, sb.toString()).apply();
        updateCartUI();
    }

    private void loadUserCart() {
        SharedPreferences prefs = getSharedPreferences(HEALTH_PREFS, Context.MODE_PRIVATE);
        currentCartList = new ArrayList<>();
        String savedString = prefs.getString(USER_CART_KEY, "");

        if (!savedString.isEmpty()) {
            String[] items = savedString.split(ITEM_DELIMITER);
            for (String item : items) {
                String[] fields = item.split("\\|\\|\\|");
                if (fields.length == 5) {
                    currentCartList.add(new CheckupTest(
                            fields[0], fields[1], fields[2], fields[3], Integer.parseInt(fields[4])
                    ));
                }
            }
        }
        updateCartUI();
    }

    private void updateCartUI() {
        tvCartCount.setText("Cart: " + currentCartList.size());

        if (currentCartList.isEmpty()) {
            tvEmptyCart.setVisibility(View.VISIBLE);
            rvCart.setVisibility(View.GONE);
        } else {
            tvEmptyCart.setVisibility(View.GONE);
            rvCart.setVisibility(View.VISIBLE);
            CheckupTest[] cartArray = currentCartList.toArray(new CheckupTest[0]);
            CartAdapter cartAdapter = new CartAdapter(cartArray, this);
            rvCart.setAdapter(cartAdapter);
        }
    }
}