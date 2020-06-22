package com.example.trackablehabit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditHabit extends AppCompatActivity {
    private SQLiteDatabase habitDatabase;
    private HabitDBHelper habitDBHelper;
    private EditText nameOfHabit;

    String id, name, count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);

        nameOfHabit = findViewById(R.id.nameInputView);
        getAndSetIntentData();

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(name);
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
                String stringNameOfHabit = nameOfHabit.getText().toString();

                habitDBHelper.updateData(id, stringNameOfHabit, count);
                nameOfHabit.getText().clear();

                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startIntent);
            }

        });
    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("name")
                && getIntent().hasExtra("count")) {
            // Getting data from Intent
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            count = getIntent().getStringExtra("count");

            // Setting Intent data
            nameOfHabit.setText(name);
        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }
}
