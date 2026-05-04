package com.example.hotelmanagementsystem.models;

public class Booking {
    private int id;
    private int userId;
    private int roomId;
    private String roomName;
    private String customerName;
    private String phone;
    private String checkIn;
    private String checkOut;
    private int totalDays;
    private int totalPrice;
    private String status;

    public Booking(int id, int userId, int roomId, String roomName,
                   String customerName, String phone, String checkIn,
                   String checkOut, int totalDays, int totalPrice, String status) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.roomName = roomName;
        this.customerName = customerName;
        this.phone = phone;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalDays = totalDays;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPhone() {
        return phone;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }
}