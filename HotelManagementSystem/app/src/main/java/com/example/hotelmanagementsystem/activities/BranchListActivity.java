package com.example.hotelmanagementsystem.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.hotelmanagementsystem.utils.BottomNavHelper;

import com.example.hotelmanagementsystem.R;

public class BranchListActivity extends AppCompatActivity {

    private TextView btnBack;

    private ImageView imgBranchCity, imgBranchBeach, imgBranchRiverside;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_list);

        getWindow().setStatusBarColor(android.graphics.Color.parseColor("#21A8F3"));

        initViews();
        setupData();
        setupEvents();
        BottomNavHelper.setup(this, "home");
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);

        imgBranchCity = findViewById(R.id.imgBranchCity);
        imgBranchBeach = findViewById(R.id.imgBranchBeach);
        imgBranchRiverside = findViewById(R.id.imgBranchRiverside);
    }

    private void setupData() {
        imgBranchCity.setImageResource(R.drawable.branch_city);
        imgBranchBeach.setImageResource(R.drawable.branch_beach);
        imgBranchRiverside.setImageResource(R.drawable.branch_riverside);
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());
    }
}