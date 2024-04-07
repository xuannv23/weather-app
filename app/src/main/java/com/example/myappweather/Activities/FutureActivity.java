package com.example.myappweather.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myappweather.Adapter.FutureAdapter;
import com.example.myappweather.Adapter.HourlyAdapter;
import com.example.myappweather.Model.Future;
import com.example.myappweather.R;

import java.util.ArrayList;

public class FutureActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterDay;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future);
        
        initRecycleView();
        setVariable();
    }

    private void setVariable() {
        ConstraintLayout backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FutureActivity.this,
                        MainActivity.class));
            }
        });
    }

    private void initRecycleView() {
        ArrayList<Future> items = new ArrayList<>();
        items.add(new Future("Sat", "storm", "Storm", 25,10));
        items.add(new Future("Sun", "sunny", "Sunny", 25,10));
        items.add(new Future("Mon", "rainy", "Rainy", 25,10));
        items.add(new Future("Tue", "snowy", "Snowy", 25,10));
        items.add(new Future("Wen", "windy", "Windy", 25,10));
        items.add(new Future("Thu", "cloudy", "Cloudy", 25,10));
        recyclerView = findViewById(R.id.RVDay);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapterDay = new FutureAdapter(items, this);
        recyclerView.setAdapter(adapterDay);
    }
}