package com.example.hotelmanagementsystem.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.database.DatabaseHelper;
import com.example.hotelmanagementsystem.models.Booking;
import com.example.hotelmanagementsystem.utils.BottomNavHelper;
import com.example.hotelmanagementsystem.utils.SessionManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private ImageView imgProfileAvatar;

    private TextView tvProfileName;
    private TextView tvProfileEmail;
    private TextView tvProfileGreeting;

    private HorizontalScrollView hsvMemberTiers;
    private LinearLayout cardTierSilver;
    private LinearLayout cardTierGold;
    private LinearLayout cardTierPlatinum;
    private LinearLayout cardTierDiamond;

    private TextView tvMembershipLevel;
    private TextView tvMembershipPoints;
    private TextView tvMembershipDescription;
    private TextView tvCurrentTierLabel;
    private TextView tvNextTierLabel;
    private TextView tvNextTierHint;
    private ProgressBar progressMembership;

    private TextView tvBookingCount;
    private TextView tvActiveBookingCount;
    private TextView tvRecentBooking;

    private Button btnMyBookings;
    private Button btnSettings;
    private Button btnEnableFaceCheckin;

    private SessionManager sessionManager;
    private DatabaseHelper db;

    private boolean faceCheckinEnabled = false;

    private static final int MOCK_MEMBER_POINTS = 1250;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getWindow().setStatusBarColor(Color.parseColor("#21A8F3"));

        sessionManager = new SessionManager(this);
        db = new DatabaseHelper(this);

        initViews();
        loadProfileInfo();
        setupEvents();

        BottomNavHelper.setup(this, "profile");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfileInfo();
        BottomNavHelper.setup(this, "profile");
    }

    private void initViews() {
        imgProfileAvatar = findViewById(R.id.imgProfileAvatar);

        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        tvProfileGreeting = findViewById(R.id.tvProfileGreeting);

        hsvMemberTiers = findViewById(R.id.hsvMemberTiers);
        cardTierSilver = findViewById(R.id.cardTierSilver);
        cardTierGold = findViewById(R.id.cardTierGold);
        cardTierPlatinum = findViewById(R.id.cardTierPlatinum);
        cardTierDiamond = findViewById(R.id.cardTierDiamond);

        tvMembershipLevel = findViewById(R.id.tvMembershipLevel);
        tvMembershipPoints = findViewById(R.id.tvMembershipPoints);
        tvMembershipDescription = findViewById(R.id.tvMembershipDescription);
        tvCurrentTierLabel = findViewById(R.id.tvCurrentTierLabel);
        tvNextTierLabel = findViewById(R.id.tvNextTierLabel);
        tvNextTierHint = findViewById(R.id.tvNextTierHint);
        progressMembership = findViewById(R.id.progressMembership);

        tvBookingCount = findViewById(R.id.tvProfileTotalBooking);
        tvActiveBookingCount = findViewById(R.id.tvProfileActiveBooking);
        tvRecentBooking = findViewById(R.id.tvRecentBookingInfo);

        btnMyBookings = findViewById(R.id.btnMyBookings);
        btnSettings = findViewById(R.id.btnSettings);
        btnEnableFaceCheckin = findViewById(R.id.btnEnableFaceCheckin);
    }

    private void loadProfileInfo() {
        String name = sessionManager.getUserName();
        String email = sessionManager.getUserEmail();
        String gender = sessionManager.getGender();

        if (name == null || name.trim().isEmpty()) {
            name = "ABC Member";
        }

        if (email == null || email.trim().isEmpty()) {
            email = "member@abchotel.vn";
        }

        tvProfileName.setText(name);
        tvProfileEmail.setText(email);
        tvProfileGreeting.setText("Chúc bạn có một kỳ nghỉ thật tuyệt vời tại ABC Hotel !!");

        imgProfileAvatar.setImageResource(getDefaultAvatarRes(gender));

        setupMembershipUI(MOCK_MEMBER_POINTS);
        loadBookingStats();
        updateFaceCheckinButton();
    }

    private void setupMembershipUI(int points) {
        String currentTier;
        String nextTier;
        int minPoint;
        int maxPoint;
        int tierColor;

        if (points < 1000) {
            currentTier = "Silver";
            nextTier = "Gold";
            minPoint = 0;
            maxPoint = 1000;
            tierColor = Color.parseColor("#6B7280");

            tvMembershipDescription.setText("Khởi đầu hành trình thành viên với các ưu đãi cơ bản.");
        } else if (points < 3000) {
            currentTier = "Gold";
            nextTier = "Platinum";
            minPoint = 1000;
            maxPoint = 3000;
            tierColor = Color.parseColor("#9A673A");

            tvMembershipDescription.setText("Ưu đãi tốt hơn, tích điểm nhanh hơn và hỗ trợ ưu tiên.");
        } else if (points < 6000) {
            currentTier = "Platinum";
            nextTier = "Diamond";
            minPoint = 3000;
            maxPoint = 6000;
            tierColor = Color.parseColor("#475569");

            tvMembershipDescription.setText("Đặc quyền nổi bật hơn, check-in nhanh và nhiều quyền lợi hơn.");
        } else {
            currentTier = "Diamond";
            nextTier = "MAX";
            minPoint = 6000;
            maxPoint = 6000;
            tierColor = Color.parseColor("#2563EB");

            tvMembershipDescription.setText("Hạng cao nhất với đầy đủ đặc quyền cao cấp tại ABC Hotel.");
        }

        tvMembershipLevel.setText(currentTier);
        tvMembershipLevel.setTextColor(tierColor);
        tvMembershipPoints.setText(formatPoints(points) + " points");

        if (points >= 6000) {
            progressMembership.setMax(100);
            progressMembership.setProgress(100);

            tvCurrentTierLabel.setText("Diamond");
            tvNextTierLabel.setText("MAX");
            tvNextTierHint.setText("Bạn đang ở hạng cao nhất 🎉");
        } else {
            progressMembership.setMax(maxPoint - minPoint);
            progressMembership.setProgress(points - minPoint);

            tvCurrentTierLabel.setText(currentTier);
            tvNextTierLabel.setText(nextTier);
            tvNextTierHint.setText("Còn " + formatPoints(maxPoint - points) + " điểm để lên " + nextTier);
        }

        highlightCurrentTier(currentTier);
        scrollToCurrentTier(currentTier);
    }

    private void loadBookingStats() {
        ArrayList<Booking> bookings = db.getBookingsByUser(sessionManager.getUserId());

        int total = bookings.size();
        int active = 0;

        for (Booking booking : bookings) {
            if ("booked".equalsIgnoreCase(booking.getStatus())) {
                active++;
            }
        }

        tvBookingCount.setText(String.valueOf(total));
        tvActiveBookingCount.setText(String.valueOf(active));

        if (!bookings.isEmpty()) {
            Booking latest = bookings.get(0);
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

            tvRecentBooking.setText(
                    "Phòng: " + latest.getRoomName() + "\n" +
                            "Mã booking: #" + latest.getId() + "\n" +
                            "Check-in: " + latest.getCheckIn() + " • Check-out: " + latest.getCheckOut() + "\n" +
                            "Tổng tiền: " + formatter.format(latest.getTotalPrice()) + " VNĐ"
            );
        } else {
            tvRecentBooking.setText("Bạn chưa có booking nào. Hãy đặt phòng để bắt đầu tích điểm thành viên.");
        }
    }

    private void setupEvents() {
        btnMyBookings.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MyBookingsActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        btnEnableFaceCheckin.setOnClickListener(v -> {
            faceCheckinEnabled = !faceCheckinEnabled;
            updateFaceCheckinButton();

            if (faceCheckinEnabled) {
                Toast.makeText(this, "Face Check-in đã được kích hoạt mô phỏng", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Face Check-in đã được tắt", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFaceCheckinButton() {
        if (faceCheckinEnabled) {
            btnEnableFaceCheckin.setText("Tắt Face Check-in");
        } else {
            btnEnableFaceCheckin.setText("Kích hoạt Face Check-in");
        }
    }

    private int getDefaultAvatarRes(String gender) {
        if (gender != null && gender.equalsIgnoreCase("female")) {
            return R.drawable.avatar_female;
        }

        return R.drawable.avatar_male;
    }

    private String formatPoints(int points) {
        return String.format(Locale.US, "%,d", points).replace(',', '.');
    }

    private void highlightCurrentTier(String currentTier) {
        cardTierSilver.setBackgroundResource(R.drawable.bg_member_card_inactive);
        cardTierGold.setBackgroundResource(R.drawable.bg_member_card_inactive);
        cardTierPlatinum.setBackgroundResource(R.drawable.bg_member_card_inactive);
        cardTierDiamond.setBackgroundResource(R.drawable.bg_member_card_inactive);

        if ("Silver".equalsIgnoreCase(currentTier)) {
            cardTierSilver.setBackgroundResource(R.drawable.bg_member_card_active);
        } else if ("Gold".equalsIgnoreCase(currentTier)) {
            cardTierGold.setBackgroundResource(R.drawable.bg_member_card_active);
        } else if ("Platinum".equalsIgnoreCase(currentTier)) {
            cardTierPlatinum.setBackgroundResource(R.drawable.bg_member_card_active);
        } else if ("Diamond".equalsIgnoreCase(currentTier)) {
            cardTierDiamond.setBackgroundResource(R.drawable.bg_member_card_active);
        }
    }

    private void scrollToCurrentTier(String currentTier) {
        View targetView;

        if ("Silver".equalsIgnoreCase(currentTier)) {
            targetView = cardTierSilver;
        } else if ("Gold".equalsIgnoreCase(currentTier)) {
            targetView = cardTierGold;
        } else if ("Platinum".equalsIgnoreCase(currentTier)) {
            targetView = cardTierPlatinum;
        } else {
            targetView = cardTierDiamond;
        }

        hsvMemberTiers.post(() -> hsvMemberTiers.smoothScrollTo(targetView.getLeft(), 0));
    }
}