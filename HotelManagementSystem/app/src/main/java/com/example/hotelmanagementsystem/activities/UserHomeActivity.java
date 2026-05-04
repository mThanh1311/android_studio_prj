package com.example.hotelmanagementsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.utils.SessionManager;

public class UserHomeActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private Button btnRooms, btnMyBookings, btnLogout;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        sessionManager = new SessionManager(this);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnRooms = findViewById(R.id.btnRooms);
        btnMyBookings = findViewById(R.id.btnMyBookings);
        btnLogout = findViewById(R.id.btnLogout);

        tvWelcome.setText("Xin chào, " + sessionManager.getUserName());

        btnRooms.setOnClickListener(v -> {
            Intent intent = new Intent(UserHomeActivity.this, RoomListActivity.class);
            startActivity(intent);
        });

        btnMyBookings.setOnClickListener(v -> {
            Intent intent = new Intent(UserHomeActivity.this, MyBookingsActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(UserHomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}