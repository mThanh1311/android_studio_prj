package com.example.fruitlistviewsqlite;

public class Fruit {
    private int id;
    private String name;
    private String imageName;
    private String summary;
    private String detail;

    public Fruit(int id, String name, String imageName, String summary, String detail) {
        this.id = id;
        this.name = name;
        this.imageName = imageName;
        this.summary = summary;
        this.detail = detail;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getImageName() { return imageName; }
    public String getSummary() { return summary; }
    public String getDetail() { return detail; }
}