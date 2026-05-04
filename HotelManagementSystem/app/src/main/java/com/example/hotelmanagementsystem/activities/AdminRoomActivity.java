package com.example.hotelmanagementsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.adapters.AdminRoomAdapter;
import com.example.hotelmanagementsystem.database.DatabaseHelper;
import com.example.hotelmanagementsystem.models.Room;

import java.util.ArrayList;

public class AdminRoomActivity extends AppCompatActivity {

    private RecyclerView recyclerRooms;
    private Button btnAddRoom;

    private DatabaseHelper db;
    private ArrayList<Room> roomList;
    private AdminRoomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_room);

        recyclerRooms = findViewById(R.id.recyclerRooms);
        btnAddRoom = findViewById(R.id.btnAddRoom);

        recyclerRooms.setLayoutManager(new LinearLayoutManager(this));

        btnAddRoom.setOnClickListener(v -> {
            Intent intent = new Intent(AdminRoomActivity.this, AddEditRoomActivity.class);
            intent.putExtra("mode", "add");
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRooms();
    }

    private void loadRooms() {
        db = new DatabaseHelper(this);
        roomList = db.getAllRooms();
        adapter = new AdminRoomAdapter(this, roomList);
        recyclerRooms.setAdapter(adapter);
    }
}