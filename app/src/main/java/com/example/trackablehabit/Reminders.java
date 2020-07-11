package com.example.trackablehabit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Reminders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Reminders");
        }
    }

    public void setRepeatNo(View view) {
    }

    public void onSwitchRepeat(View view) {
    }

    public void setTime(View view) {
    }

    public void setDate(View view) {
    }

    public void selectRepeatType(View view) {
    }
}
