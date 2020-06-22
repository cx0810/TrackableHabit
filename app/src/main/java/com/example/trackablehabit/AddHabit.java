package com.example.trackablehabit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddHabit extends AppCompatActivity {
    private SQLiteDatabase habitDatabase;
    private HabitDBHelper habitDBHelper;
    private EditText nameOfHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Add a Habit");
        }

        habitDBHelper = new HabitDBHelper(this);
        habitDatabase = habitDBHelper.getWritableDatabase();

        Button addReminderButton = findViewById(R.id.addReminderButton);
        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), Reminders.class);
                startActivity(startIntent);
            }
        });

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameOfHabit = findViewById(R.id.nameInputView);
                String stringNameOfHabit = nameOfHabit.getText().toString();

                habitDBHelper.insertData(stringNameOfHabit);
                nameOfHabit.getText().clear();

                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startIntent);
            }

        });
    }

}
