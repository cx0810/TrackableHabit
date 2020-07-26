package com.example.trackablehabit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditHabit extends AppCompatActivity {
    private HabitDBHelper habitDBHelper;
    private TextView nameOfHabit, resetToZeroEvery, targetView;

    String id, name, count, target, reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);

        resetToZeroEvery = findViewById(R.id.setReset);
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

            Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(startIntent);
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
            reset = getIntent().getStringExtra("reset");

            // Setting Intent data
            nameOfHabit.setText(name);
            targetView.setText(target);
            resetToZeroEvery.setText(reset);
        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }
    public void setName(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter Name of Habit");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                (dialog, whichButton) -> {
                    if (input.getText().toString().length() == 0) {
                        name = Integer.toString(1);
                        nameOfHabit.setText(name);
                    } else {
                        name = input.getText().toString().trim();
                        nameOfHabit.setText(name);
                    }
                });

        alert.setNegativeButton("Cancel", (dialog, which) -> {
            // do nth
        });
        alert.show();

    }

    public void setResetToZero(View view) {

        final String[] items = new String[4];

        items[0] = "Day";
        items[1] = "Week";
        items[2] = "Month";
        items[3] = "Year";

        //  create list dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Type");
        builder.setItems(items, (dialog, item) -> {
            reset = items[item];
            resetToZeroEvery.setText(reset);
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setTarget(View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter Target Count");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                (dialog, whichButton) -> {
                    if (input.getText().toString().length() == 0) {
                        count = Integer.toString(1);
                        targetView.setText(count);
                    } else {
                        count = input.getText().toString().trim();
                        targetView.setText(count);
                    }
                });

        alert.setNegativeButton("Cancel", (dialog, which) -> {
            // do nth
        });
        alert.show();
    }
}
