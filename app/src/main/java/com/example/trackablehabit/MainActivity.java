package com.example.trackablehabit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    public SQLiteDatabase habitDatabase;
    public HabitDBHelper habitDBHelper;
    private HabitAdapter habitAdapter;

    Button addBtn;
    Button manualBtn;
    Button statsBtn;

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

        habitAdapter.setOnItemClickListener(new HabitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                Intent startIntent = new Intent(getApplicationContext(), EditHabit.class);
                startActivity(startIntent);
            }

            @Override
            public void onIncrementClick(int count, TextView habitCount) {
                int newCount = count + 1;
                habitCount.setText(String.valueOf(newCount));
                habitDBHelper.incrementData(HabitContract.HabitEntry._ID, HabitContract.HabitEntry.COLUMN_NAME, newCount);
            }

            @Override
            public void onDecrementClick(int count, TextView habitCount) {
                if (count > 0) {
                    int newCount = count - 1;
                    habitCount.setText(String.valueOf(newCount));
                    habitDBHelper.decrementData(HabitContract.HabitEntry._ID, HabitContract.HabitEntry.COLUMN_NAME, newCount);
                }
            }
        });


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
