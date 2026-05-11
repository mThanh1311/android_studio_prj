package com.example.hotelmanagementsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.database.DatabaseHelper;
import com.example.hotelmanagementsystem.models.Booking;
import com.example.hotelmanagementsystem.utils.SessionManager;
import com.example.hotelmanagementsystem.utils.BottomNavHelper;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.example.hotelmanagementsystem.models.AppNotification;
import com.example.hotelmanagementsystem.utils.NotificationHelper;

public class NotificationActivity extends AppCompatActivity {

    private LinearLayout layoutEmptyNotification;
    private LinearLayout layoutNotificationContainer;

    private SessionManager sessionManager;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        sessionManager = new SessionManager(this);
        db = new DatabaseHelper(this);

        initViews();
        loadNotifications();
    }

    private void initViews() {
        layoutEmptyNotification = findViewById(R.id.layoutEmptyNotification);
        layoutNotificationContainer = findViewById(R.id.layoutNotificationContainer);
    }

    private void loadNotifications() {
        layoutNotificationContainer.removeAllViews();

        ArrayList<AppNotification> notifications = NotificationHelper.getCurrentNotifications(this);

        for (AppNotification notification : notifications) {
            addNotificationCard(
                    notification.getTitle(),
                    notification.getDetail(),
                    notification.getBookingId()
            );
        }

        if (notifications.isEmpty()) {
            layoutEmptyNotification.setVisibility(View.VISIBLE);
            layoutNotificationContainer.setVisibility(View.GONE);
        } else {
            layoutEmptyNotification.setVisibility(View.GONE);
            layoutNotificationContainer.setVisibility(View.VISIBLE);
        }

        NotificationHelper.markAllCurrentNotificationsAsSeen(this);
    }

    private long daysBetween(Calendar today, Calendar target) {
        Calendar todayOnly = Calendar.getInstance();
        todayOnly.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        todayOnly.set(Calendar.MILLISECOND, 0);

        Calendar targetOnly = Calendar.getInstance();
        targetOnly.set(target.get(Calendar.YEAR), target.get(Calendar.MONTH), target.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        targetOnly.set(Calendar.MILLISECOND, 0);

        long diff = targetOnly.getTimeInMillis() - todayOnly.getTimeInMillis();
        return diff / (24 * 60 * 60 * 1000);
    }

    private void addNotificationCard(String title, String detail, int bookingId) {
        View card = getLayoutInflater().inflate(
                R.layout.item_notification_dynamic,
                layoutNotificationContainer,
                false
        );

        TextView tvTitle = card.findViewById(R.id.tvNotiTitle);
        TextView tvDetail = card.findViewById(R.id.tvNotiDetail);

        tvTitle.setText(title);
        tvDetail.setText(detail);

        card.setOnClickListener(v -> {
            if (bookingId != -1) {
                Intent intent = new Intent(NotificationActivity.this, BookingDetailActivity.class);
                intent.putExtra("booking_id", bookingId);
                startActivity(intent);
            } else {
                Toast.makeText(NotificationActivity.this, "Ưu đãi đang được áp dụng cho tài khoản của bạn", Toast.LENGTH_SHORT).show();
            }
        });

        layoutNotificationContainer.addView(card);
    }
}