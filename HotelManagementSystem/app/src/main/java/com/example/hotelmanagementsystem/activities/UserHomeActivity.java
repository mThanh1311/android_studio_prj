package com.example.hotelmanagementsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.FrameLayout;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.utils.SessionManager;

import android.view.View;
import com.example.hotelmanagementsystem.utils.NotificationHelper;

public class UserHomeActivity extends AppCompatActivity {

    private ScrollView scrollUserHome;
    private TextView tvHomeGreeting, tvHomeMember;
    private TextView tvSeeAllRooms;
    private LinearLayout cardSearch;
    private LinearLayout cardRooms, cardBranches, cardAmenities, cardSupport;
    private LinearLayout sectionSupport;
    private LinearLayout navHome, navBookings, navNotifications, navProfile, navSettings;
    private TextView tvNotificationBadge;
    private android.widget.FrameLayout navNotificationContainer;
    private SessionManager sessionManager;
    private ImageView imgAvatar;

    private ImageView imgHomeAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        sessionManager = new SessionManager(this);

        initViews();
        setupUserInfo();
        setupEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNotificationBadge();
    }

    private void initViews() {
        scrollUserHome = findViewById(R.id.scrollUserHome);
        tvHomeGreeting = findViewById(R.id.tvHomeGreeting);
        tvHomeMember = findViewById(R.id.tvHomeMember);
        tvSeeAllRooms = findViewById(R.id.tvSeeAllRooms);
        cardSearch = findViewById(R.id.cardSearch);
        cardRooms = findViewById(R.id.cardRooms);
        cardBranches = findViewById(R.id.cardBranches);
        cardAmenities = findViewById(R.id.cardAmenities);
        cardSupport = findViewById(R.id.cardSupport);
        sectionSupport = findViewById(R.id.sectionSupport);

        navHome = findViewById(R.id.navHome);
        navBookings = findViewById(R.id.navBookings);
        navNotifications = findViewById(R.id.navNotifications);
        navProfile = findViewById(R.id.navProfile);
        navSettings = findViewById(R.id.navSettings);

        tvNotificationBadge = findViewById(R.id.tvNotificationBadge);
        navNotificationContainer = findViewById(R.id.navNotificationContainer);

        imgAvatar = findViewById(R.id.imgAvatar);

        imgHomeAvatar = findViewById(R.id.imgHomeAvatar);
        imgHomeAvatar.setImageResource(getDefaultAvatarRes());

    }

    private void setupUserInfo() {
        String name = sessionManager.getUserName();

        if (name == null || name.trim().isEmpty()) {
            name = "User";
        }

        tvHomeGreeting.setText("Hi, " + name);
        tvHomeMember.setText("Gold Member • ABC Hotel");

        String gender = sessionManager.getGender();

        if (gender != null && gender.equalsIgnoreCase("female")) {
            imgHomeAvatar.setImageResource(R.drawable.avatar_female);
        } else {
            imgHomeAvatar.setImageResource(R.drawable.avatar_male);
        }
    }

    private void setupEvents() {
        cardSearch.setOnClickListener(v -> openRoomList());
        cardRooms.setOnClickListener(v -> openRoomList());
        tvSeeAllRooms.setOnClickListener(v -> openRoomList());

        cardBranches.setOnClickListener(v -> {
            Intent intent = new Intent(UserHomeActivity.this, BranchListActivity.class);
            startActivity(intent);
        });

        cardAmenities.setOnClickListener(v -> {
            Intent intent = new Intent(UserHomeActivity.this, AmenityListActivity.class);
            startActivity(intent);
        });

        cardSupport.setOnClickListener(v -> {
            scrollUserHome.post(() -> scrollUserHome.smoothScrollTo(0, sectionSupport.getTop()));
        });

        navNotificationContainer.setOnClickListener(v -> openNotifications());
        navNotifications.setOnClickListener(v -> openNotifications());

        navHome.setOnClickListener(v -> {
            scrollUserHome.smoothScrollTo(0, 0);
        });

        navBookings.setOnClickListener(v -> {
            Intent intent = new Intent(UserHomeActivity.this, MyBookingsActivity.class);
            startActivity(intent);
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(UserHomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        navSettings.setOnClickListener(v -> {
            Intent intent = new Intent(UserHomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        sectionSupport.setOnClickListener(v -> {
            Intent intent = new Intent(UserHomeActivity.this, CustomerSupportActivity.class);
            startActivity(intent);
        });
    }

    private void openRoomList() {
        Intent intent = new Intent(UserHomeActivity.this, RoomListActivity.class);
        startActivity(intent);
    }

    private void openNotifications() {
        Intent intent = new Intent(UserHomeActivity.this, NotificationActivity.class);
        startActivity(intent);
    }

    private int getDefaultAvatarRes() {
        String gender = sessionManager.getGender();

        if (gender != null && gender.equalsIgnoreCase("female")) {
            return R.drawable.avatar_female;
        }
        return R.drawable.avatar_male;
    }

    private void updateNotificationBadge() {
        int unreadCount = NotificationHelper.getUnreadCount(this);

        if (unreadCount > 0) {
            tvNotificationBadge.setVisibility(View.VISIBLE);
            tvNotificationBadge.setText(String.valueOf(unreadCount));
        } else {
            tvNotificationBadge.setVisibility(View.GONE);
        }
    }
}