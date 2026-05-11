package com.example.hotelmanagementsystem.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

public class SessionManager {

    private static final String PREF_NAME = "HotelSession";

    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_ROLE = "role";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_LAST_BOOKING_ID = "last_booking_id";
    private static final String KEY_SEEN_NOTIFICATIONS = "seen_notifications";
    private static final String KEY_EMAIL = "email";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveLogin(int userId, String name, String email, String role, String gender) {
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_GENDER, gender);
        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_LOGGED_IN, false);
    }

    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }

    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "");
    }

    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, "");
    }

    public String getRole() {
        return pref.getString(KEY_ROLE, "user");
    }

    public String getGender() {
        return pref.getString(KEY_GENDER, "male");
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

    public void saveLastBookingId(int bookingId) {
        editor.putInt(KEY_LAST_BOOKING_ID, bookingId);
        editor.apply();
    }

    public int getLastBookingId() {
        return pref.getInt(KEY_LAST_BOOKING_ID, -1);
    }

    public Set<String> getSeenNotificationKeys() {
        return new HashSet<>(pref.getStringSet(KEY_SEEN_NOTIFICATIONS, new HashSet<>()));
    }

    public boolean isNotificationSeen(String key) {
        Set<String> seenKeys = getSeenNotificationKeys();
        return seenKeys.contains(key);
    }

    public void markNotificationAsSeen(String key) {
        Set<String> seenKeys = getSeenNotificationKeys();
        seenKeys.add(key);

        editor.putStringSet(KEY_SEEN_NOTIFICATIONS, seenKeys);
        editor.apply();
    }

    public void markNotificationsAsSeen(Set<String> keys) {
        Set<String> seenKeys = getSeenNotificationKeys();
        seenKeys.addAll(keys);

        editor.putStringSet(KEY_SEEN_NOTIFICATIONS, seenKeys);
        editor.apply();
    }
}