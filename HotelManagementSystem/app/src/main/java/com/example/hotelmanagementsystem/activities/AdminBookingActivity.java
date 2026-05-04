package com.example.hotelmanagementsystem.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.adapters.BookingAdapter;
import com.example.hotelmanagementsystem.database.DatabaseHelper;
import com.example.hotelmanagementsystem.models.Booking;

import java.util.ArrayList;

public class AdminBookingActivity extends AppCompatActivity {

    private RecyclerView recyclerBookings;
    private DatabaseHelper db;
    private ArrayList<Booking> bookingList;
    private BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_booking);

        db = new DatabaseHelper(this);

        recyclerBookings = findViewById(R.id.recyclerBookings);
        recyclerBookings.setLayoutManager(new LinearLayoutManager(this));

        bookingList = db.getAllBookings();
        adapter = new BookingAdapter(this, bookingList, false);

        recyclerBookings.setAdapter(adapter);
    }
}