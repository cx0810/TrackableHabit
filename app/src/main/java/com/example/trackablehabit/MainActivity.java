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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase habitDatabase;
    private HabitDBHelper habitDBHelper;
    private HabitAdapter habitAdapter;

    Button addBtn;
    Button manualBtn;
    Button statsBtn;

    Button incrementCountBtn;
    Button decrementCountBtn;

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
                habitAdapter.swapCursor(habitDBHelper.getAllHabits());
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
        habitAdapter.swapCursor(habitDBHelper.getAllHabits());
    }

}
