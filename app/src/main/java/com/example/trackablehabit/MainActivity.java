package com.example.trackablehabit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.drm.DrmStore;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public SQLiteDatabase habitDatabase;
    public HabitDBHelper habitDBHelper;
    private DrawerLayout drawer;

    // arraylists
    private ArrayList<String> habit_name;
    private ArrayList<Integer> habit_id, habit_count, habit_target, habit_streak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        habitDBHelper = new HabitDBHelper(this);
        habitDatabase = habitDBHelper.getReadableDatabase();

        // Initialise arraylists
        habit_id = new ArrayList<>();
        habit_name = new ArrayList<>();
        habit_count = new ArrayList<>();
        habit_target = new ArrayList<>();
        habit_streak = new ArrayList<>();

        storeDataInArrays();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HabitFragment()).commit();


    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    void storeDataInArrays() {
        Cursor cursor = habitDBHelper.getAllHabits();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                habit_id.add(cursor.getInt(0));
                habit_name.add(cursor.getString(1));
                habit_count.add(cursor.getInt(2));
                habit_target.add(cursor.getInt(3));
                habit_streak.add(cursor.getInt(4));
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = new HabitFragment();

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HabitFragment();
                        break;
                    case R.id.nav_statistics:
                        selectedFragment = new StatisticsFragment();
                        break;
                    case R.id.nav_rewards:
                        selectedFragment = new RewardsFragment();
                        break;
                    case R.id.nav_news:
                        selectedFragment = new NewsFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
            };
}
