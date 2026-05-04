package com.example.hotelmanagementsystem.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.database.DatabaseHelper;
import com.example.hotelmanagementsystem.models.Room;

public class AddEditRoomActivity extends AppCompatActivity {

    private EditText edtRoomName, edtRoomType, edtPrice, edtCapacity, edtDescription, edtStatus;
    private Button btnSaveRoom;

    private DatabaseHelper db;
    private String mode;
    private int roomId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_room);

        db = new DatabaseHelper(this);

        edtRoomName = findViewById(R.id.edtRoomName);
        edtRoomType = findViewById(R.id.edtRoomType);
        edtPrice = findViewById(R.id.edtPrice);
        edtCapacity = findViewById(R.id.edtCapacity);
        edtDescription = findViewById(R.id.edtDescription);
        edtStatus = findViewById(R.id.edtStatus);
        btnSaveRoom = findViewById(R.id.btnSaveRoom);

        mode = getIntent().getStringExtra("mode");

        if ("edit".equals(mode)) {
            roomId = getIntent().getIntExtra("room_id", -1);
            loadRoomData();
        } else {
            edtStatus.setText("available");
        }

        btnSaveRoom.setOnClickListener(v -> saveRoom());
    }

    private void loadRoomData() {
        Room room = db.getRoomById(roomId);

        if (room == null) {
            finish();
            return;
        }

        edtRoomName.setText(room.getName());
        edtRoomType.setText(room.getType());
        edtPrice.setText(String.valueOf(room.getPrice()));
        edtCapacity.setText(String.valueOf(room.getCapacity()));
        edtDescription.setText(room.getDescription());
        edtStatus.setText(room.getStatus());
    }

    private void saveRoom() {
        String name = edtRoomName.getText().toString().trim();
        String type = edtRoomType.getText().toString().trim();
        String priceText = edtPrice.getText().toString().trim();
        String capacityText = edtCapacity.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String status = edtStatus.getText().toString().trim();

        if (name.isEmpty() || type.isEmpty() || priceText.isEmpty()
                || capacityText.isEmpty() || description.isEmpty() || status.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int price;
        int capacity;

        try {
            price = Integer.parseInt(priceText);
            capacity = Integer.parseInt(capacityText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá phòng hoặc sức chứa không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success;

        if ("edit".equals(mode)) {
            success = db.updateRoom(roomId, name, type, price, capacity, description, status);
        } else {
            success = db.addRoom(name, type, price, capacity, description, status);
        }

        if (success) {
            Toast.makeText(this, "Lưu phòng thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lưu phòng thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}