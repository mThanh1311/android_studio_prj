package com.example.hotelmanagementsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(this);

        new Handler().postDelayed(() -> {
            if (sessionManager.isLoggedIn()) {
                if (sessionManager.getRole().equals("admin")) {
                    startActivity(new Intent(this, AdminHomeActivity.class));
                } else {
                    startActivity(new Intent(this, UserHomeActivity.class));
                }
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }

            finish();
        }, 1200);
    }
}