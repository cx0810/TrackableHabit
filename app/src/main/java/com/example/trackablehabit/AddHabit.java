package com.example.trackablehabit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import static java.lang.Integer.parseInt;

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

        Button plusValueButton = findViewById(R.id.plusValueButton);
        Button minusValueButton = findViewById(R.id.minusValueButton);
        final TextView incrementValueView = findViewById(R.id.incrementValueView);

        plusValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(incrementValueView.getText().toString());
                int newCount = count + 1;
                incrementValueView.setText(String.valueOf(newCount));
            }
        });

        minusValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(incrementValueView.getText().toString());
                if (count > 1) {
                    int newCount = count - 1;
                    incrementValueView.setText(String.valueOf(newCount));
                }
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

                long date = System.currentTimeMillis();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
                String dateString = sdf.format(date);
                String habitIDString = habitDBHelper.queryLatestID();
                habitDBHelper.insertStats(dateString, parseInt(habitIDString), stringNameOfHabit, 0);

                nameOfHabit.getText().clear();

                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startIntent);
            }
        });
    }
}
