package com.example.hotelmanagementsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.database.DatabaseHelper;
import com.example.hotelmanagementsystem.models.Room;

import java.text.NumberFormat;
import java.util.Locale;

public class RoomDetailActivity extends AppCompatActivity {

    private TextView tvRoomName, tvRoomType, tvPrice, tvCapacity, tvDescription, tvStatus;
    private Button btnBook;

    private DatabaseHelper db;
    private int roomId;
    private Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        db = new DatabaseHelper(this);
        roomId = getIntent().getIntExtra("room_id", -1);

        tvRoomName = findViewById(R.id.tvRoomName);
        tvRoomType = findViewById(R.id.tvRoomType);
        tvPrice = findViewById(R.id.tvPrice);
        tvCapacity = findViewById(R.id.tvCapacity);
        tvDescription = findViewById(R.id.tvDescription);
        tvStatus = findViewById(R.id.tvStatus);
        btnBook = findViewById(R.id.btnBook);

        loadRoomDetail();

        btnBook.setOnClickListener(v -> {
            Intent intent = new Intent(RoomDetailActivity.this, BookingActivity.class);
            intent.putExtra("room_id", roomId);
            startActivity(intent);
        });
    }

    private void loadRoomDetail() {
        room = db.getRoomById(roomId);

        if (room == null) {
            finish();
            return;
        }

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        tvRoomName.setText(room.getName());
        tvRoomType.setText("Loại phòng: " + room.getType());
        tvPrice.setText(formatter.format(room.getPrice()) + " VNĐ / đêm");
        tvCapacity.setText("Sức chứa: " + room.getCapacity() + " người");
        tvDescription.setText(room.getDescription());
        tvStatus.setText("Trạng thái: " + room.getStatus());
    }
}