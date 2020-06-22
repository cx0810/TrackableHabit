package com.example.trackablehabit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public SQLiteDatabase habitDatabase;
    public HabitDBHelper habitDBHelper;
    private HabitAdapter habitAdapter;

    Button addBtn;
    Button manualBtn;
    Button statsBtn;

    // arraylists
    ArrayList<String> habit_id, habit_name, habit_count;

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
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);

        addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), AddHabit.class);
                startActivity(startIntent);
            }
        });

        manualBtn = findViewById(R.id.manualBtn);
        manualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), UserManual.class);
                startActivity(startIntent);
            }
        });

        statsBtn = findViewById(R.id.statsBtn);
        statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), Statistics.class);
                startActivity(startIntent);
            }
        });

    }

    private void removeItem(long id) {
        habitDatabase.delete(HabitContract.HabitEntry.TABLE_NAME,
                HabitContract.HabitEntry._ID + "=" + id, null);
//        habitAdapter.swapCursor(habitDBHelper.getAllHabits());
    }

    // added
    void storeDataInArrays() {
        Cursor cursor = habitDBHelper.getAllHabits();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                habit_id.add(cursor.getString(0));
                habit_name.add(cursor.getString(1));
                habit_count.add(cursor.getString(2));
            }
        }
    }
}
