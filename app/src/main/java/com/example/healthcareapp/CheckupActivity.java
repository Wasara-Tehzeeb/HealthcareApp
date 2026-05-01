package com.example.healthcareapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CheckupActivity extends AppCompatActivity implements CartAdapter.OnRemoveClickListener {

    private static final String HEALTH_PREFS = "HealthAppPrefs";
    private static final String ITEM_DELIMITER = "~~~";
    private static final String FIELD_DELIMITER = "|||";

    private String USER_CART_KEY;
    private ArrayList<CheckupTest> currentCartList;

    private RecyclerView rvCart;
    private TextView tvEmptyCart, tvCartCount;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkup);

        String email = getIntent().getStringExtra("USER_EMAIL");
        if (email == null) email = "guest";
        USER_CART_KEY = "cart_" + email;

        rvCart = findViewById(R.id.rvCartList);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        tvCartCount = findViewById(R.id.tvCartCount);
        ivBack = findViewById(R.id.ivBack);

        rvCart.setHasFixedSize(true);
        rvCart.setLayoutManager(new LinearLayoutManager(this));

        ivBack.setOnClickListener(v -> finish());

        findViewById(R.id.btnAdd1).setOnClickListener(v -> addToCart(
                new CheckupTest("Complete Blood Count", "Measures blood components", "Fasting 10hrs required", "$25.00", R.drawable.blood))
        );

        findViewById(R.id.btnAdd2).setOnClickListener(v -> addToCart(
                new CheckupTest("Basic Metabolic Panel", "Checks blood sugar & calcium", "Fasting 8hrs required", "$35.00", R.drawable.checkup))
        );

        findViewById(R.id.btnAdd3).setOnClickListener(v -> addToCart(
                new CheckupTest("Urinalysis", "Detects kidney disorders", "Mid-stream urine sample", "$15.00", R.drawable.prescription))
        );

        findViewById(R.id.btnAdd4).setOnClickListener(v -> addToCart(
                new CheckupTest("Thyroid Function Test", "Checks thyroid levels", "No preparation needed", "$45.00", R.drawable.hospital))
        );

        findViewById(R.id.btnAdd5).setOnClickListener(v -> addToCart(
                new CheckupTest("Lipid Panel (Cholesterol)", "Measures cholesterol and triglycerides", "Fasting 12hrs required", "$30.00", R.drawable.blood))
        );

        findViewById(R.id.btnAdd6).setOnClickListener(v -> addToCart(
                new CheckupTest("HbA1c (Diabetes Test)", "Measures average blood sugar levels", "No preparation needed", "$20.00", R.drawable.blood))
        );

        findViewById(R.id.btnAdd7).setOnClickListener(v -> addToCart(
                new CheckupTest("Liver Function Test (LFT)", "Checks liver health and enzymes", "Fasting 10hrs required", "$40.00", R.drawable.blood))
        );

        findViewById(R.id.btnAdd8).setOnClickListener(v -> addToCart(
                new CheckupTest("Vitamin D Test", "Checks vitamin D levels in blood", "No preparation needed", "$35.00", R.drawable.blood))
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
        }
        else {
            tvEmptyCart.setVisibility(View.GONE);
            rvCart.setVisibility(View.VISIBLE);
            CheckupTest[] cartArray = currentCartList.toArray(new CheckupTest[0]);
            CartAdapter cartAdapter = new CartAdapter(cartArray, this);
            rvCart.setAdapter(cartAdapter);
        }
    }
}