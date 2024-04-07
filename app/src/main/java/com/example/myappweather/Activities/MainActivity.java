package com.example.myappweather.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myappweather.Adapter.HourlyAdapter;
import com.example.myappweather.Model.Hourly;
import com.example.myappweather.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterHourly;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecycleView();
        setVariable();
    }

    private void setVariable() {
        TextView nextBtn = findViewById(R.id.next7day);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FutureActivity.class));
            }
        });
    }

    private void initRecycleView() {
        ArrayList<Hourly> items = new ArrayList<>();
        items.add(new Hourly("10 pm",20, R.drawable.cloudy));
        items.add(new Hourly("11 pm",28, R.drawable.sunny));
        items.add(new Hourly("12 pm",21, R.drawable.windy));
        items.add(new Hourly("1 am",22, R.drawable.rainy));
        items.add(new Hourly("2 pm",23, R.drawable.snowy));
        items.add(new Hourly("3 pm",24, R.drawable.storm));
        items.add(new Hourly("4 pm",25, R.drawable.sunny));
        items.add(new Hourly("5 pm",26, R.drawable.humidity));
        recyclerView = findViewById(R.id.RvHour);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        adapterHourly = new HourlyAdapter(items, this);
        recyclerView.setAdapter(adapterHourly);
    }
}