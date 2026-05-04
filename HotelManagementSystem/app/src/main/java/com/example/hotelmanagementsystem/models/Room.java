package com.example.hotelmanagementsystem.models;

public class Room {
    private int id;
    private String name;
    private String type;
    private int price;
    private int capacity;
    private String description;
    private String status;

    public Room(int id, String name, String type, int price, int capacity, String description, String status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.capacity = capacity;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }
}