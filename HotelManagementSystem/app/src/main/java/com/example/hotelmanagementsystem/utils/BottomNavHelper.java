package com.example.hotelmanagementsystem.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.activities.MyBookingsActivity;
import com.example.hotelmanagementsystem.activities.NotificationActivity;
import com.example.hotelmanagementsystem.activities.ProfileActivity;
import com.example.hotelmanagementsystem.activities.SettingsActivity;
import com.example.hotelmanagementsystem.activities.UserHomeActivity;

public class BottomNavHelper {

    private static final String ACTIVE_COLOR = "#21A8F3";
    private static final String NORMAL_COLOR = "#6B7280";

    public static void setup(Activity activity, String activeTab) {
        View navHome = activity.findViewById(R.id.navHome);
        View navBookings = activity.findViewById(R.id.navBookings);
        View navNotifications = activity.findViewById(R.id.navNotifications);
        View navProfile = activity.findViewById(R.id.navProfile);
        View navSettings = activity.findViewById(R.id.navSettings);

        if (navHome == null) return;

        navHome.setOnClickListener(v -> open(activity, UserHomeActivity.class));
        navBookings.setOnClickListener(v -> open(activity, MyBookingsActivity.class));
        navNotifications.setOnClickListener(v -> open(activity, NotificationActivity.class));
        navProfile.setOnClickListener(v -> open(activity, ProfileActivity.class));
        navSettings.setOnClickListener(v -> open(activity, SettingsActivity.class));

        highlight(activity, activeTab);
    }

    private static void open(Activity activity, Class<?> target) {
        if (activity.getClass().equals(target)) return;

        Intent intent = new Intent(activity, target);
        activity.startActivity(intent);
    }

    private static void highlight(Activity activity, String activeTab) {
        resetAll(activity);

        if ("home".equals(activeTab)) {
            setActive(activity, R.id.iconNavHome, R.id.textNavHome);
        } else if ("booking".equals(activeTab)) {
            setActive(activity, R.id.iconNavBooking, R.id.textNavBooking);
        } else if ("notification".equals(activeTab)) {
            setActive(activity, R.id.iconNavNotification, R.id.textNavNotification);
        } else if ("profile".equals(activeTab)) {
            setActive(activity, R.id.iconNavProfile, R.id.textNavProfile);
        } else if ("setting".equals(activeTab)) {
            setActive(activity, R.id.iconNavSettings, R.id.textNavSettings);
        }
    }

    private static void resetAll(Activity activity) {
        setNormal(activity, R.id.iconNavHome, R.id.textNavHome);
        setNormal(activity, R.id.iconNavBooking, R.id.textNavBooking);
        setNormal(activity, R.id.iconNavNotification, R.id.textNavNotification);
        setNormal(activity, R.id.iconNavProfile, R.id.textNavProfile);
        setNormal(activity, R.id.iconNavSettings, R.id.textNavSettings);
    }

    private static void setActive(Activity activity, int iconId, int textId) {
        setColor(activity, iconId, textId, ACTIVE_COLOR, true);
    }

    private static void setNormal(Activity activity, int iconId, int textId) {
        setColor(activity, iconId, textId, NORMAL_COLOR, false);
    }

    private static void setColor(Activity activity, int iconId, int textId, String color, boolean bold) {
        int parsedColor = android.graphics.Color.parseColor(color);

        ImageView icon = activity.findViewById(iconId);
        TextView text = activity.findViewById(textId);

        if (icon != null) icon.setColorFilter(parsedColor);

        if (text != null) {
            text.setTextColor(parsedColor);
            text.setTypeface(null, bold ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
        }
    }
}