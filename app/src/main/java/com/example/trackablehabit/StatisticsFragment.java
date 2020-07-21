package com.example.trackablehabit;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StatisticsFragment extends Fragment {

    private SQLiteDatabase habitDatabase;
    private HabitDBHelper habitDBHelper;
    private StatsAdapter statsAdapter;

    private ArrayList<Integer> habit_id, habit_count, list_habit_id;
    private ArrayList<String> date_array, habit_name, list_habit_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);

        habitDBHelper = new HabitDBHelper(getActivity());
        habitDatabase = habitDBHelper.getReadableDatabase();

        date_array = new ArrayList<>();
        habit_id = new ArrayList<>();
        habit_name = new ArrayList<>();
        habit_count = new ArrayList<>();
        storeStatsInArray();

        list_habit_id = new ArrayList<>();
        list_habit_name = new ArrayList<>();
        storeHabitList();

        RecyclerView recyclerView = rootView.findViewById(R.id.statsRecyclerView2);
        statsAdapter = new StatsAdapter(getActivity(), date_array, habit_id, habit_name,
                habit_count, list_habit_id, list_habit_name);
        recyclerView.setAdapter(statsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    private void storeStatsInArray() {
        Cursor cursor = habitDBHelper.getAllStats();
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                date_array.add(cursor.getString(1));
                habit_id.add(cursor.getInt(2));
                habit_name.add(cursor.getString(3));
                habit_count.add(cursor.getInt(4));
            }
        }
    }

    private void storeHabitList() {
        Cursor cursor = habitDBHelper.getAllHabits();
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                list_habit_id.add(cursor.getInt(0));
                list_habit_name.add(cursor.getString(1));
            }
        }
    }

}
