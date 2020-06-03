package com.example.trackablehabit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase habitDatabase;
    private HabitDBHelper habitDBHelper;
    private HabitAdapter habitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        habitDBHelper = new HabitDBHelper(this);
        habitDatabase = habitDBHelper.getReadableDatabase();

        RecyclerView recyclerView = findViewById(R.id.habitRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        habitAdapter = new HabitAdapter(this, habitDBHelper.getAllHabits());
        recyclerView.setAdapter(habitAdapter);

        Button addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habitAdapter.swapCursor(habitDBHelper.getAllHabits());
                Intent startIntent = new Intent(getApplicationContext(), AddHabit.class);
                startActivity(startIntent);
            }
        });

        Button manualBtn = findViewById(R.id.manualBtn);
        manualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), UserManual.class);
                startActivity(startIntent);
            }
        });

        Button statsBtn = findViewById(R.id.statsBtn);
        statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), Statistics.class);
                startActivity(startIntent);
            }
        });
    }
}
