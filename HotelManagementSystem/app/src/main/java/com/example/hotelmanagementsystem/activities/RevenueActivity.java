package com.example.hotelmanagementsystem.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.database.DatabaseHelper;

import java.text.NumberFormat;
import java.util.Locale;

public class RevenueActivity extends AppCompatActivity {

    private TextView tvRevenue, tvBookingCount, tvRoomCount;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);

        db = new DatabaseHelper(this);

        tvRevenue = findViewById(R.id.tvRevenue);
        tvBookingCount = findViewById(R.id.tvBookingCount);
        tvRoomCount = findViewById(R.id.tvRoomCount);

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        tvRevenue.setText(formatter.format(db.getTotalRevenue()) + " VNĐ");
        tvBookingCount.setText(String.valueOf(db.getBookingCount()));
        tvRoomCount.setText(String.valueOf(db.getRoomCount()));
    }
}