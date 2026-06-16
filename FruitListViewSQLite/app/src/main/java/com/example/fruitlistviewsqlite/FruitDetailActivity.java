package com.example.fruitlistviewsqlite;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FruitDetailActivity extends AppCompatActivity {
    ImageView imgDetail;
    TextView txtName, txtSummary, txtBenefit;
    Button btnBack;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_detail);

        imgDetail = findViewById(R.id.imgDetail);
        txtName = findViewById(R.id.txtName);
        txtSummary = findViewById(R.id.txtSummary);
        txtBenefit = findViewById(R.id.txtBenefit);
        btnBack = findViewById(R.id.btnBack);

        db = new DatabaseHelper(this);

        int fruitId = getIntent().getIntExtra("fruit_id", -1);
        Fruit fruit = db.getFruitById(fruitId);

        if (fruit != null) {
            txtName.setText(fruit.getName());
            txtSummary.setText(fruit.getSummary());
            txtBenefit.setText(fruit.getDetail());

            int imageId = getResources().getIdentifier(
                    fruit.getImageName(), "drawable", getPackageName()
            );
            imgDetail.setImageResource(imageId);
        }

        btnBack.setOnClickListener(v -> finish());
    }
}