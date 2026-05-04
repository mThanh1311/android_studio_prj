package com.example.hotelmanagementsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.utils.SessionManager;

public class AdminHomeActivity extends AppCompatActivity {

    private Button btnManageRooms, btnManageBookings, btnRevenue, btnLogout;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        sessionManager = new SessionManager(this);

        btnManageRooms = findViewById(R.id.btnManageRooms);
        btnManageBookings = findViewById(R.id.btnManageBookings);
        btnRevenue = findViewById(R.id.btnRevenue);
        btnLogout = findViewById(R.id.btnLogout);

        btnManageRooms.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, AdminRoomActivity.class);
            startActivity(intent);
        });

        btnManageBookings.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, AdminBookingActivity.class);
            startActivity(intent);
        });

        btnRevenue.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, RevenueActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}