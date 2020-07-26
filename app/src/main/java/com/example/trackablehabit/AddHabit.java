package com.example.trackablehabit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import java.text.SimpleDateFormat;

import static java.lang.Integer.parseInt;

public class AddHabit extends AppCompatActivity {
    private SQLiteDatabase habitDatabase;
    private HabitDBHelper habitDBHelper;
    private TextView nameOfHabit, resetToZeroEvery, target;
    private String name, reset, count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        nameOfHabit = findViewById(R.id.nameInputView);
        resetToZeroEvery = findViewById(R.id.setReset);
        target = findViewById(R.id.targetValueView);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Add a Habit");
        }

        habitDBHelper = new HabitDBHelper(this);
        habitDatabase = habitDBHelper.getWritableDatabase();

        Button addReminderButton = findViewById(R.id.addReminderButton);
        addReminderButton.setOnClickListener(v -> {
            Intent startIntent = new Intent(getApplicationContext(), AddReminder.class);
            startActivity(startIntent);
        });

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            String stringNameOfHabit = nameOfHabit.getText().toString();
            String stringTarget = target.getText().toString();
            String resetEvery = resetToZeroEvery.getText().toString();

            habitDBHelper.insertData(stringNameOfHabit, parseInt(stringTarget), resetEvery);

            long date = System.currentTimeMillis();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
            String dateString = sdf.format(date);
            String habitIDString = habitDBHelper.queryLatestID();
            habitDBHelper.insertStats(dateString, parseInt(habitIDString), stringNameOfHabit, 0);

//            nameOfHabit.getText().clear();

            Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(startIntent);
        });
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
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                (dialog, whichButton) -> {
                    if (input.getText().toString().length() == 0) {
                        count = Integer.toString(1);
                        target.setText(count);
                    } else {
                        count = input.getText().toString().trim();
                        target.setText(count);
                    }
                });

        alert.setNegativeButton("Cancel", (dialog, which) -> {
            // do nth
        });
        alert.show();
    }

}
