package com.example.foodorder.ui.location;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.databinding.ActivityMapBinding;

public class MapActivity extends AppCompatActivity {
    private ActivityMapBinding binding;
    private static final String STORE_ADDRESS = "123 Food Street, City, State 12345";
    private static final String STORE_PHONE = "+1 234-567-8900";
    private static final String STORE_HOURS = "9:00 AM - 10:00 PM (Daily)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        displayStoreInfo();
        setupButtons();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Store Location");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void displayStoreInfo() {
        binding.tvStoreAddress.setText(STORE_ADDRESS);
        binding.tvStorePhone.setText(STORE_PHONE);
        binding.tvStoreHours.setText(STORE_HOURS);
    }

    private void setupButtons() {
        binding.btnGetDirections.setOnClickListener(v -> openMapsForDirections());
    }

    private void openMapsForDirections() {
        // Open Google Maps for directions
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(STORE_ADDRESS));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Google Maps not installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
