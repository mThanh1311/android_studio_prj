package com.example.hotelmanagementsystem.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelmanagementsystem.R;
import com.example.hotelmanagementsystem.adapters.RoomAdapter;
import com.example.hotelmanagementsystem.database.DatabaseHelper;
import com.example.hotelmanagementsystem.models.Room;

import java.util.ArrayList;

public class RoomListActivity extends AppCompatActivity {

    private EditText edtSearchRoom;
    private TextView tvResultCount;

    private Button btnRoomListSupport;

    private TextView filterAll, filterStandard, filterDeluxe, filterSuite, filterFamily;
    private TextView branchAll, branchCity, branchBeach, branchRiverside;

    private RecyclerView recyclerRooms;
    private DatabaseHelper db;
    private ArrayList<Room> roomList;
    private RoomAdapter adapter;

    private String selectedType = "All";
    private String selectedBranch = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        db = new DatabaseHelper(this);

        initViews();
        setupRecyclerView();
        setupSearch();
        setupTypeFilters();
        setupBranchFilters();
        setupSupportButton();

        loadRooms();
    }

    private void initViews() {
        edtSearchRoom = findViewById(R.id.edtSearchRoom);
        tvResultCount = findViewById(R.id.tvResultCount);

        recyclerRooms = findViewById(R.id.recyclerRooms);

        filterAll = findViewById(R.id.filterAll);
        filterStandard = findViewById(R.id.filterStandard);
        filterDeluxe = findViewById(R.id.filterDeluxe);
        filterSuite = findViewById(R.id.filterSuite);
        filterFamily = findViewById(R.id.filterFamily);

        branchAll = findViewById(R.id.branchAll);
        branchCity = findViewById(R.id.branchCity);
        branchBeach = findViewById(R.id.branchBeach);
        branchRiverside = findViewById(R.id.branchRiverside);
        btnRoomListSupport = findViewById(R.id.btnRoomListSupport);
    }

    private void setupRecyclerView() {
        recyclerRooms.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupSearch() {
        edtSearchRoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadRooms();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupTypeFilters() {
        filterAll.setOnClickListener(v -> {
            selectedType = "All";
            updateTypeChipUI();
            loadRooms();
        });

        filterStandard.setOnClickListener(v -> {
            selectedType = "Standard";
            updateTypeChipUI();
            loadRooms();
        });

        filterDeluxe.setOnClickListener(v -> {
            selectedType = "Deluxe";
            updateTypeChipUI();
            loadRooms();
        });

        filterSuite.setOnClickListener(v -> {
            selectedType = "Suite";
            updateTypeChipUI();
            loadRooms();
        });

        filterFamily.setOnClickListener(v -> {
            selectedType = "Family";
            updateTypeChipUI();
            loadRooms();
        });
    }

    private void setupBranchFilters() {
        branchAll.setOnClickListener(v -> {
            selectedBranch = "All";
            updateBranchChipUI();
            loadRooms();
        });

        branchCity.setOnClickListener(v -> {
            selectedBranch = "Chi nhánh Trung tâm Thành phố";
            updateBranchChipUI();
            loadRooms();
        });

        branchBeach.setOnClickListener(v -> {
            selectedBranch = "Chi nhánh Biển Xanh Resort";
            updateBranchChipUI();
            loadRooms();
        });

        branchRiverside.setOnClickListener(v -> {
            selectedBranch = "Chi nhánh Riverside Premium";
            updateBranchChipUI();
            loadRooms();
        });
    }

    private void loadRooms() {
        String keyword = edtSearchRoom.getText().toString().trim();

        roomList = db.searchRooms(keyword, selectedType, selectedBranch);
        adapter = new RoomAdapter(this, roomList);
        recyclerRooms.setAdapter(adapter);

        tvResultCount.setText(roomList.size() + " phòng phù hợp");
    }

    private void updateTypeChipUI() {
        setChipSelected(filterAll, selectedType.equals("All"));
        setChipSelected(filterStandard, selectedType.equals("Standard"));
        setChipSelected(filterDeluxe, selectedType.equals("Deluxe"));
        setChipSelected(filterSuite, selectedType.equals("Suite"));
        setChipSelected(filterFamily, selectedType.equals("Family"));
    }

    private void updateBranchChipUI() {
        setChipSelected(branchAll, selectedBranch.equals("All"));
        setChipSelected(branchCity, selectedBranch.equals("Chi nhánh Trung tâm Thành phố"));
        setChipSelected(branchBeach, selectedBranch.equals("Chi nhánh Biển Xanh Resort"));
        setChipSelected(branchRiverside, selectedBranch.equals("Chi nhánh Riverside Premium"));
    }

    private void setChipSelected(TextView chip, boolean isSelected) {
        if (isSelected) {
            chip.setBackgroundResource(R.drawable.bg_filter_chip_selected);
            chip.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            chip.setBackgroundResource(R.drawable.bg_filter_chip);
            chip.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    private void setupSupportButton() {
        btnRoomListSupport.setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    "Hotline hỗ trợ đặt phòng: 0123 456 789",
                    Toast.LENGTH_LONG
            ).show();
        });
    }
}