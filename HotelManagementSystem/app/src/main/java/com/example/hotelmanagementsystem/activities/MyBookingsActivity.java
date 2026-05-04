package com.example.hotelmanagementsystem.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.adapters.BookingAdapter;
import com.example.hotelmanagementsystem.database.DatabaseHelper;
import com.example.hotelmanagementsystem.models.Booking;
import com.example.hotelmanagementsystem.utils.SessionManager;

import java.util.ArrayList;

public class MyBookingsActivity extends AppCompatActivity {

    private RecyclerView recyclerBookings;
    private DatabaseHelper db;
    private SessionManager sessionManager;
    private ArrayList<Booking> bookingList;
    private BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        db = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        recyclerBookings = findViewById(R.id.recyclerBookings);
        recyclerBookings.setLayoutManager(new LinearLayoutManager(this));

        bookingList = db.getBookingsByUser(sessionManager.getUserId());
        adapter = new BookingAdapter(this, bookingList, true);

        recyclerBookings.setAdapter(adapter);
    }
}