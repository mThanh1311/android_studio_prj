package com.example.hotelmanagementsystem.models;

public class AppNotification {
    private String key;
    private String title;
    private String detail;
    private int bookingId;

    public AppNotification(String key, String title, String detail, int bookingId) {
        this.key = key;
        this.title = title;
        this.detail = detail;
        this.bookingId = bookingId;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public int getBookingId() {
        return bookingId;
    }
}