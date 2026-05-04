package com.example.hotelmanagementsystem.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.adapters.RoomAdapter;
import com.example.hotelmanagementsystem.database.DatabaseHelper;
import com.example.hotelmanagementsystem.models.Room;

import java.util.ArrayList;

public class RoomListActivity extends AppCompatActivity {

    private RecyclerView recyclerRooms;
    private DatabaseHelper db;
    private ArrayList<Room> roomList;
    private RoomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        recyclerRooms = findViewById(R.id.recyclerRooms);
        recyclerRooms.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHelper(this);
        roomList = db.getAllRooms();

        adapter = new RoomAdapter(this, roomList);
        recyclerRooms.setAdapter(adapter);
    }
}