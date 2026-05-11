package com.example.hotelmanagementsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private LinearLayout layoutSplashContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(this);
        layoutSplashContent = findViewById(R.id.layoutSplashContent);

        startFadeAnimation();

//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            if (sessionManager.isLoggedIn()) {
//                if (sessionManager.getRole().equals("admin")) {
//                    startActivity(new Intent(SplashActivity.this, AdminHomeActivity.class));
//                } else {
//                    startActivity(new Intent(SplashActivity.this, UserHomeActivity.class));
//                }
//            } else {
//                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
//            }
//
//            finish();
//        }, 2000);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            finish();
        }, 2000);
    }

    private void startFadeAnimation() {
        Animation fadeIn = new AlphaAnimation(0.3f, 1.0f);
        fadeIn.setDuration(1200);
        layoutSplashContent.startAnimation(fadeIn);
    }
}