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

public class Rewards extends AppCompatActivity {

    private SQLiteDatabase habitDatabase;
    private HabitDBHelper habitDBHelper;
    private RewardsAdapter rewardsAdapter;

    ArrayList<Integer> habit_id, habit_streak;
    ArrayList<String> habit_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Rewards");
        }

        habitDBHelper = new HabitDBHelper(this);
        habitDatabase = habitDBHelper.getReadableDatabase();

        habit_id = new ArrayList<>();
        habit_name = new ArrayList<>();
        habit_streak = new ArrayList<>();
        storeDataInArrays();

        RecyclerView recyclerView = findViewById(R.id.rewardsRecyclerView);
        rewardsAdapter = new RewardsAdapter(this, habit_id, habit_name, habit_streak);
        recyclerView.setAdapter(rewardsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    void storeDataInArrays() {
        Cursor cursor = habitDBHelper.getAllHabits();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                habit_id.add(cursor.getInt(0));
                habit_name.add(cursor.getString(1));
                habit_streak.add(cursor.getInt(2)); // need to change
            }
        }
    }
}
