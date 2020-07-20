package com.example.trackablehabit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditHabit extends AppCompatActivity {
    private HabitDBHelper habitDBHelper;
    private EditText nameOfHabit, targetView;

    String id, name, count, target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);

        nameOfHabit = findViewById(R.id.nameInputView);
        targetView = findViewById(R.id.targetValueView);
        getAndSetIntentData();

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(name);
        }

        habitDBHelper = new HabitDBHelper(this);

        Button addReminderButton = findViewById(R.id.addReminderButton);
        addReminderButton.setOnClickListener(v -> {
            Intent startIntent = new Intent(getApplicationContext(), AddReminder.class);
            startActivity(startIntent);
        });

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            String stringNameOfHabit = nameOfHabit.getText().toString();
            String stringTarget = targetView.getText().toString();

            habitDBHelper.updateData(id, stringNameOfHabit, count, stringTarget);
            nameOfHabit.getText().clear();

            Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(startIntent);
        });

        Button plusValueButton = findViewById(R.id.plusValueButton);
        Button minusValueButton = findViewById(R.id.minusValueButton);
        final TextView incrementValueView = findViewById(R.id.incrementValueView);

        plusValueButton.setOnClickListener(v -> {
            int count = Integer.parseInt(incrementValueView.getText().toString());
            int newCount = count + 1;
            incrementValueView.setText(String.valueOf(newCount));
        });

        minusValueButton.setOnClickListener(v -> {
            int count = Integer.parseInt(incrementValueView.getText().toString());
            if (count > 1) {
                int newCount = count - 1;
                incrementValueView.setText(String.valueOf(newCount));
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
            target = getIntent().getStringExtra("target");

            // Setting Intent data
            nameOfHabit.setText(name);
            targetView.setText(target);
        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }
}
