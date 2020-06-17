package com.example.trackablehabit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditHabit extends AppCompatActivity {
    private SQLiteDatabase habitDatabase;
    private HabitDBHelper habitDBHelper;
    private EditText nameOfHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);


        habitDBHelper = new HabitDBHelper(this);
        habitDatabase = habitDBHelper.getWritableDatabase();

        Button cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startIntent);
            }
        });

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
