package com.example.hotelmanagementsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hotelmanagementsystem.models.Booking;
import com.example.hotelmanagementsystem.models.Room;
import com.example.hotelmanagementsystem.models.User;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "hotel_management.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "email TEXT UNIQUE," +
                "password TEXT," +
                "role TEXT DEFAULT 'user')";

        String createRooms = "CREATE TABLE rooms (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "room_name TEXT," +
                "room_type TEXT," +
                "price INTEGER," +
                "capacity INTEGER," +
                "description TEXT," +
                "status TEXT DEFAULT 'available')";

        String createBookings = "CREATE TABLE bookings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "room_id INTEGER," +
                "customer_name TEXT," +
                "phone TEXT," +
                "check_in TEXT," +
                "check_out TEXT," +
                "total_days INTEGER," +
                "total_price INTEGER," +
                "status TEXT DEFAULT 'booked')";

        db.execSQL(createUsers);
        db.execSQL(createRooms);
        db.execSQL(createBookings);

        seedData(db);
    }

    private void seedData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO users(name, email, password, role) VALUES " +
                "('Administrator', 'admin@gmail.com', 'admin123', 'admin')");

        db.execSQL("INSERT INTO rooms(room_name, room_type, price, capacity, description, status) VALUES " +
                "('Deluxe Ocean Room', 'Deluxe', 850000, 2, 'Phòng deluxe hướng biển, nội thất hiện đại, phù hợp cho cặp đôi.', 'available')," +
                "('Royal Suite', 'Suite', 1800000, 4, 'Phòng suite cao cấp có phòng khách riêng, view thành phố.', 'available')," +
                "('Family Comfort Room', 'Family', 1200000, 5, 'Phòng gia đình rộng rãi, đầy đủ tiện nghi.', 'available')," +
                "('Standard City Room', 'Standard', 550000, 2, 'Phòng tiêu chuẩn, sạch đẹp, giá hợp lý.', 'available')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS bookings");
        db.execSQL("DROP TABLE IF EXISTS rooms");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public boolean registerUser(String name, String email, String password) {
        if (isEmailExists(email)) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        values.put("role", "user");

        long result = db.insert("users", null, values);
        return result != -1;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM users WHERE email = ?",
                new String[]{email}
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();

        return exists;
    }

    public User loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE email = ? AND password = ?",
                new String[]{email, password}
        );

        if (cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    cursor.getString(cursor.getColumnIndexOrThrow("role"))
            );

            cursor.close();
            return user;
        }

        cursor.close();
        return null;
    }

    public ArrayList<Room> getAllRooms() {
        ArrayList<Room> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM rooms ORDER BY id DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Room room = new Room(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("room_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("room_type")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("capacity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status"))
                );

                list.add(room);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public Room getRoomById(int roomId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM rooms WHERE id = ?",
                new String[]{String.valueOf(roomId)}
        );

        if (cursor.moveToFirst()) {
            Room room = new Room(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("room_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("room_type")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("capacity")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getString(cursor.getColumnIndexOrThrow("status"))
            );

            cursor.close();
            return room;
        }

        cursor.close();
        return null;
    }

    public boolean addRoom(String name, String type, int price, int capacity, String description, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("room_name", name);
        values.put("room_type", type);
        values.put("price", price);
        values.put("capacity", capacity);
        values.put("description", description);
        values.put("status", status);

        long result = db.insert("rooms", null, values);
        return result != -1;
    }

    public boolean updateRoom(int id, String name, String type, int price, int capacity, String description, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("room_name", name);
        values.put("room_type", type);
        values.put("price", price);
        values.put("capacity", capacity);
        values.put("description", description);
        values.put("status", status);

        int result = db.update(
                "rooms",
                values,
                "id = ?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }

    public boolean deleteRoom(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                "rooms",
                "id = ?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }

    public boolean createBooking(int userId, int roomId, String customerName, String phone,
                                 String checkIn, String checkOut, int totalDays, int totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("room_id", roomId);
        values.put("customer_name", customerName);
        values.put("phone", phone);
        values.put("check_in", checkIn);
        values.put("check_out", checkOut);
        values.put("total_days", totalDays);
        values.put("total_price", totalPrice);
        values.put("status", "booked");

        long result = db.insert("bookings", null, values);
        return result != -1;
    }

    public ArrayList<Booking> getBookingsByUser(int userId) {
        ArrayList<Booking> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT b.*, r.room_name FROM bookings b " +
                "JOIN rooms r ON b.room_id = r.id " +
                "WHERE b.user_id = ? ORDER BY b.id DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                list.add(cursorToBooking(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public ArrayList<Booking> getAllBookings() {
        ArrayList<Booking> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT b.*, r.room_name FROM bookings b " +
                "JOIN rooms r ON b.room_id = r.id " +
                "ORDER BY b.id DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(cursorToBooking(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    private Booking cursorToBooking(Cursor cursor) {
        return new Booking(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                cursor.getInt(cursor.getColumnIndexOrThrow("room_id")),
                cursor.getString(cursor.getColumnIndexOrThrow("room_name")),
                cursor.getString(cursor.getColumnIndexOrThrow("customer_name")),
                cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                cursor.getString(cursor.getColumnIndexOrThrow("check_in")),
                cursor.getString(cursor.getColumnIndexOrThrow("check_out")),
                cursor.getInt(cursor.getColumnIndexOrThrow("total_days")),
                cursor.getInt(cursor.getColumnIndexOrThrow("total_price")),
                cursor.getString(cursor.getColumnIndexOrThrow("status"))
        );
    }

    public boolean cancelBooking(int bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("status", "cancelled");

        int result = db.update(
                "bookings",
                values,
                "id = ?",
                new String[]{String.valueOf(bookingId)}
        );

        return result > 0;
    }

    public int getTotalRevenue() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT SUM(total_price) AS revenue FROM bookings WHERE status = 'booked'",
                null
        );

        int revenue = 0;

        if (cursor.moveToFirst()) {
            revenue = cursor.getInt(cursor.getColumnIndexOrThrow("revenue"));
        }

        cursor.close();
        return revenue;
    }

    public int getBookingCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) AS count FROM bookings",
                null
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
        }

        cursor.close();
        return count;
    }

    public int getRoomCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) AS count FROM rooms",
                null
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
        }

        cursor.close();
        return count;
    }
}