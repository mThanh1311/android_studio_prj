package com.example.hotelmanagementsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.utils.SessionManager;
import com.example.hotelmanagementsystem.utils.BottomNavHelper;

public class SettingsActivity extends AppCompatActivity {

    private TextView tvUserName, tvUserEmail, tvMembership;
    private LinearLayout rowAccount, rowSecurity, rowNotification, rowSupport, rowTerms, rowAbout;
    private LinearLayout rowLogout;
    private ImageView imgSettingAvatar;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sessionManager = new SessionManager(this);

        initViews();
        loadUserInfo();
        setupEvents();
        BottomNavHelper.setup(this, "setting");
    }

    private void initViews() {
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvMembership = findViewById(R.id.tvMembership);

        rowAccount = findViewById(R.id.rowAccount);
        rowSecurity = findViewById(R.id.rowSecurity);
        rowNotification = findViewById(R.id.rowNotification);
        rowSupport = findViewById(R.id.rowSupport);
        rowTerms = findViewById(R.id.rowTerms);
        rowAbout = findViewById(R.id.rowAbout);
        imgSettingAvatar = findViewById(R.id.imgSettingAvatar);
        imgSettingAvatar.setImageResource(getDefaultAvatarRes());
        rowLogout = findViewById(R.id.rowLogout);
    }

    private void loadUserInfo() {
        String name = sessionManager.getUserName();
        String email = sessionManager.getUserEmail();
        String gender = sessionManager.getGender();

        if (name == null || name.trim().isEmpty()) {
            name = "ABC Member";
        }

        if (email == null || email.trim().isEmpty()) {
            email = "member@abchotel.vn";
        }

        tvUserName.setText(name);
        tvUserEmail.setText(email);
        tvMembership.setText("Gold Member • 1.250 điểm");

        if ("female".equals(gender)) {
            imgSettingAvatar.setImageResource(R.drawable.avatar_female);
        } else {
            imgSettingAvatar.setImageResource(R.drawable.avatar_male);
        }
    }

    private void setupEvents() {
        rowAccount.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, AccountInfoActivity.class);
            startActivity(intent);
        });

        rowSecurity.setOnClickListener(v ->
                Toast.makeText(this, "Face Check-in và bảo mật tài khoản sẽ được cập nhật sau", Toast.LENGTH_SHORT).show()
        );

        rowNotification.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, NotificationActivity.class);
            startActivity(intent);
        });

        rowSupport.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, CustomerSupportActivity.class);
            startActivity(intent);
        });

        rowTerms.setOnClickListener(v ->
                Toast.makeText(this, "Điều khoản dịch vụ ABC Hotel", Toast.LENGTH_SHORT).show()
        );

        rowAbout.setOnClickListener(v ->
                Toast.makeText(this, "ABC Hotel Booking App - Version 1.0", Toast.LENGTH_SHORT).show()
        );

        rowLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Đăng xuất tài khoản");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất khỏi ABC Hotel không?");

        builder.setPositiveButton("Đăng xuất", (dialog, which) -> {
            sessionManager.logout();

            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        builder.setNegativeButton("Huỷ", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private int getDefaultAvatarRes() {
        String gender = sessionManager.getGender();

        if (gender != null && gender.equalsIgnoreCase("female")) {
            return R.drawable.avatar_female;
        }
        return R.drawable.avatar_male;
    }
}