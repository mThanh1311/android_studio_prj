package com.example.fruitlistviewsqlite;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvFruits;
    DatabaseHelper db;
    ArrayList<Fruit> fruitList;
    FruitAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvFruits = findViewById(R.id.lvFruits);
        db = new DatabaseHelper(this);

        fruitList = db.getAllFruits();
        adapter = new FruitAdapter(this, fruitList);
        lvFruits.setAdapter(adapter);

        lvFruits.setOnItemClickListener((parent, view, position, id) -> {
            Fruit fruit = fruitList.get(position);
            Intent intent = new Intent(MainActivity.this, FruitDetailActivity.class);
            intent.putExtra("fruit_id", fruit.getId());
            startActivity(intent);
        });
    }
}