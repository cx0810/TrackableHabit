package com.example.trackablehabit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public SQLiteDatabase habitDatabase;
    public HabitDBHelper habitDBHelper;
    private HabitAdapter habitAdapter;

    Button addBtn;
    Button manualBtn;
    Button statsBtn;
    Button rewardsBtn;

    // arraylists
    private ArrayList<String> habit_name;
    private ArrayList<Integer> habit_id, habit_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        habitDBHelper = new HabitDBHelper(this);
        habitDatabase = habitDBHelper.getReadableDatabase();

        // Initialise arraylists
        habit_id = new ArrayList<>();
        habit_name = new ArrayList<>();
        habit_count = new ArrayList<>();

        storeDataInArrays();

        RecyclerView recyclerView = findViewById(R.id.habitRecyclerView);
        habitAdapter = new HabitAdapter(this, habit_id, habit_name, habit_count);
        recyclerView.setAdapter(habitAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((String) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);

        addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(v -> {
            Intent startIntent = new Intent(getApplicationContext(), AddHabit.class);
            startActivity(startIntent);
        });

        manualBtn = findViewById(R.id.manualBtn);
        manualBtn.setOnClickListener(v -> {
            Intent startIntent = new Intent(getApplicationContext(), UserManual.class);
            startActivity(startIntent);
        });

        statsBtn = findViewById(R.id.statsBtn);
        statsBtn.setOnClickListener(v -> {
            Intent startIntent = new Intent(getApplicationContext(), Statistics.class);
            startActivity(startIntent);
        });

        rewardsBtn = findViewById(R.id.rewardsBtn);
        rewardsBtn.setOnClickListener(v -> {
            Intent startIntent = new Intent(getApplicationContext(), Rewards.class);
            startActivity(startIntent);
        });

        saveDailyStats();
    }

    void storeDataInArrays() {
        Cursor cursor = habitDBHelper.getAllHabits();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                habit_id.add(cursor.getInt(0));
                habit_name.add(cursor.getString(1));
                habit_count.add(cursor.getInt(2));
            }
        }
    }

    private void removeItem(String id) {
        long result = habitDatabase.delete(HabitContract.HabitEntry.TABLE_NAME,
                "_id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(this, "Failed to delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Habit deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    void saveDailyStats() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        long currentDay = calendar.getTimeInMillis();

        SharedPreferences settings = MainActivity.this.getSharedPreferences("PREFS", 0);
        long lastDay = settings.getLong("day", 0);

        long diffMillis = currentDay - lastDay;

        if (diffMillis >= (3600000  * 24)) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putLong("day", currentDay);
            editor.apply();

            // save stats to database at the end of the day
            long date = System.currentTimeMillis();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
            String dateString = sdf.format(date);

            habitDBHelper = new HabitDBHelper(MainActivity.this);
//            Toast.makeText(MainActivity.this, "habit_id size = " + habit_id.size(), Toast.LENGTH_SHORT).show();
            for (int i = 0; i < habit_id.size(); i++) {
                int habitID = habit_id.get(i);
                String habitName = habit_name.get(i);
                int count = habit_count.get(i);
                habitDBHelper.insertStats(dateString, habitID, habitName, count);
            }

            // add code to reset to 0
        }
    }
}
