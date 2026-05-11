package com.example.hotelmanagementsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.adapters.BookingAdapter;
import com.example.hotelmanagementsystem.database.DatabaseHelper;
import com.example.hotelmanagementsystem.models.Booking;
import com.example.hotelmanagementsystem.utils.BottomNavHelper;
import com.example.hotelmanagementsystem.utils.SessionManager;

import java.util.ArrayList;

public class MyBookingsActivity extends AppCompatActivity {

    private RecyclerView recyclerBookings;
    private LinearLayout layoutEmptyBooking;
    private Button btnFindRoomFromEmpty;
    private TextView tvTotalBookings, tvActiveBookings;

    private DatabaseHelper db;
    private SessionManager sessionManager;
    private ArrayList<Booking> bookingList;
    private BookingAdapter bookingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        getWindow().setStatusBarColor(android.graphics.Color.parseColor("#21A8F3"));

        db = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        initViews();
        setupRecycler();
        setupEvents();
        loadBookings();

        BottomNavHelper.setup(this, "booking");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (bookingAdapter != null) {
            loadBookings();
        }

        BottomNavHelper.setup(this, "booking");
    }

    private void initViews() {
        recyclerBookings = findViewById(R.id.recyclerBookings);

        // ID này phải là LinearLayout empty state
        layoutEmptyBooking = findViewById(R.id.layoutEmptyBooking);

        // ID này phải là Button "Tìm phòng ngay"
        btnFindRoomFromEmpty = findViewById(R.id.btnFindRoomFromEmpty);

        tvTotalBookings = findViewById(R.id.tvTotalBookings);
        tvActiveBookings = findViewById(R.id.tvActiveBookings);
    }

    private void setupRecycler() {
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(this, bookingList, true);

        recyclerBookings.setLayoutManager(new LinearLayoutManager(this));
        recyclerBookings.setAdapter(bookingAdapter);
    }

    private void setupEvents() {
        btnFindRoomFromEmpty.setOnClickListener(v -> {
            Intent intent = new Intent(MyBookingsActivity.this, RoomListActivity.class);
            startActivity(intent);
        });
    }

    private void loadBookings() {
        bookingList.clear();

        ArrayList<Booking> data = db.getBookingsByUser(sessionManager.getUserId());
        bookingList.addAll(data);

        bookingAdapter.notifyDataSetChanged();

        updateSummary();
        updateEmptyState();
    }

    private void updateSummary() {
        int total = bookingList.size();
        int active = 0;

        for (Booking booking : bookingList) {
            if ("booked".equalsIgnoreCase(booking.getStatus())) {
                active++;
            }
        }

        tvTotalBookings.setText(String.valueOf(total));
        tvActiveBookings.setText(String.valueOf(active));
    }

    private void updateEmptyState() {
        if (bookingList.isEmpty()) {
            layoutEmptyBooking.setVisibility(View.VISIBLE);
            recyclerBookings.setVisibility(View.GONE);
        } else {
            layoutEmptyBooking.setVisibility(View.GONE);
            recyclerBookings.setVisibility(View.VISIBLE);
        }
    }
}