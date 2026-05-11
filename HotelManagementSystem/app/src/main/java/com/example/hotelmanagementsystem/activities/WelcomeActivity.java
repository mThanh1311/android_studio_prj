package com.example.hotelmanagementsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.adapters.WelcomeSliderAdapter;
import com.example.hotelmanagementsystem.models.WelcomeSlide;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager2 viewPagerWelcome;
    private LinearLayout layoutDots;
    private Button btnGetStarted;
    private TextView tvGoRegister;

    private ArrayList<WelcomeSlide> slideList;
    private Handler sliderHandler;
    private Runnable sliderRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        sliderHandler = new Handler(Looper.getMainLooper());

        initViews();

        if (!checkViewsReady()) {
            Toast.makeText(this, "Lỗi layout Welcome: thiếu id trong activity_welcome.xml", Toast.LENGTH_LONG).show();
            return;
        }

        setupSlides();
        setupEvents();
        setupAutoSlide();
    }

    private void initViews() {
        viewPagerWelcome = findViewById(R.id.viewPagerWelcome);
        layoutDots = findViewById(R.id.layoutDots);
        btnGetStarted = findViewById(R.id.btnGetStarted);
        tvGoRegister = findViewById(R.id.tvGoRegister);
    }

    private boolean checkViewsReady() {
        return viewPagerWelcome != null
                && layoutDots != null
                && btnGetStarted != null
                && tvGoRegister != null;
    }

    private void setupSlides() {
        slideList = new ArrayList<>();

        slideList.add(new WelcomeSlide(
                R.drawable.welcome_slide_1,
                "Welcome to ABC Hotel",
                "Trải nghiệm đặt phòng khách sạn nhanh chóng, tiện lợi và hiện đại."
        ));

        slideList.add(new WelcomeSlide(
                R.drawable.welcome_slide_2,
                "Luxury Rooms",
                "Khám phá nhiều hạng phòng sang trọng, sạch sẽ và đầy đủ tiện nghi."
        ));

        slideList.add(new WelcomeSlide(
                R.drawable.welcome_slide_3,
                "Premium Services",
                "Tận hưởng hồ bơi, nhà hàng, spa và dịch vụ hỗ trợ khách hàng 24/7."
        ));

        slideList.add(new WelcomeSlide(
                R.drawable.welcome_slide_4,
                "Easy Booking",
                "Đăng nhập tài khoản, chọn phòng yêu thích và quản lý booking dễ dàng."
        ));

        WelcomeSliderAdapter adapter = new WelcomeSliderAdapter(slideList);
        viewPagerWelcome.setAdapter(adapter);

        setupDots(0);

        viewPagerWelcome.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setupDots(position);
            }
        });
    }

    private void setupDots(int selectedPosition) {
        layoutDots.removeAllViews();

        for (int i = 0; i < slideList.size(); i++) {
            android.view.View dot = new android.view.View(this);

            LinearLayout.LayoutParams params;
            if (i == selectedPosition) {
                dot.setBackgroundResource(R.drawable.bg_indicator_active);
                params = new LinearLayout.LayoutParams(22, 8);
            } else {
                dot.setBackgroundResource(R.drawable.bg_indicator_inactive);
                params = new LinearLayout.LayoutParams(8, 8);
            }

            params.setMargins(6, 0, 6, 0);
            dot.setLayoutParams(params);

            layoutDots.addView(dot);
        }
    }

    private void setupEvents() {
        btnGetStarted.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        tvGoRegister.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void setupAutoSlide() {
        sliderRunnable = () -> {
            if (viewPagerWelcome == null || slideList == null || slideList.isEmpty()) {
                return;
            }

            int currentItem = viewPagerWelcome.getCurrentItem();
            int nextItem = currentItem + 1;

            if (nextItem >= slideList.size()) {
                nextItem = 0;
            }

            viewPagerWelcome.setCurrentItem(nextItem, true);
            sliderHandler.postDelayed(sliderRunnable, 3000);
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sliderHandler != null && sliderRunnable != null) {
            sliderHandler.postDelayed(sliderRunnable, 3000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (sliderHandler != null && sliderRunnable != null) {
            sliderHandler.removeCallbacks(sliderRunnable);
        }
    }
}