package com.example.trackablehabit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class Statistics extends AppCompatActivity {

    private SQLiteDatabase habitDatabase;
    private HabitDBHelper habitDBHelper;
    private StatsAdapter statsAdapter;

    ArrayList<Integer> habit_id, habit_count;
    ArrayList<String> date_array, habit_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Statistics");
        }

        habitDBHelper = new HabitDBHelper(this);
        habitDatabase = habitDBHelper.getReadableDatabase();

        date_array = new ArrayList<>();
        habit_id = new ArrayList<>();
        habit_name = new ArrayList<>();
        habit_count = new ArrayList<>();
        storeStatsInArray();

        RecyclerView recyclerView = findViewById(R.id.statsRecyclerView);
        statsAdapter = new StatsAdapter(this, date_array, habit_id, habit_name, habit_count);
        recyclerView.setAdapter(statsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    void storeStatsInArray() {
        Cursor cursor = habitDBHelper.getAllStats();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                date_array.add(cursor.getString(1));
                habit_id.add(cursor.getInt(2));
                habit_name.add(cursor.getString(3));
                habit_count.add(cursor.getInt(4));
            }
        }
    }

}
