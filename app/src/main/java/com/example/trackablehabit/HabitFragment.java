package com.example.trackablehabit;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class HabitFragment extends Fragment {

    private SQLiteDatabase habitDatabase;
    private HabitDBHelper habitDBHelper;
    private HabitAdapter habitAdapter;

    private ArrayList<String> habit_name;
    private ArrayList<Integer> habit_id, habit_count, habit_target, habit_streak;
    private FloatingActionButton addHabitFab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_habit, container, false);
        addHabitFab = rootView.findViewById(R.id.addHabitFab);

        habitDBHelper = new HabitDBHelper(getActivity());
        habitDatabase = habitDBHelper.getReadableDatabase();

        // Initialise arraylists
        habit_id = new ArrayList<>();
        habit_name = new ArrayList<>();
        habit_count = new ArrayList<>();
        habit_target = new ArrayList<>();
        habit_streak = new ArrayList<>();

        storeDataInArrays();
        saveAndResetDailyStats();

        RecyclerView recyclerView = rootView.findViewById(R.id.habitRecyclerView);
        habitAdapter = new HabitAdapter(getActivity(), habit_id, habit_name, habit_count, habit_target, habit_streak);
        recyclerView.setAdapter(habitAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((String) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);

        addHabitFab.setOnClickListener(v -> {
            Intent startIntent = new Intent(getActivity(), AddHabit.class);
            startActivity(startIntent);
        });

        return rootView;
    }

    private void storeDataInArrays() {
        Cursor cursor = habitDBHelper.getAllHabits();
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No data.", Toast.LENGTH_SHORT).show();
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

    private void removeItem(String id) {
        long result = habitDatabase.delete(HabitContract.HabitEntry.TABLE_NAME,
                "_id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(getActivity(), "Failed to delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Habit deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAndResetDailyStats() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        long currentDay = calendar.getTimeInMillis();

        SharedPreferences settings = requireActivity().getSharedPreferences("PREFS", 0);
        long lastDay = settings.getLong("day", 0);

        long diffMillis = currentDay - lastDay;

        if (diffMillis >= (3600000  * 24)) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putLong("day", currentDay);
            editor.apply();

            // save stats to database at the end of the day
            long date = System.currentTimeMillis();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
            String dateString = sdf.format(date);

            habitDBHelper = new HabitDBHelper(getActivity());
            for (int i = 0; i < habit_id.size(); i++) {
                int habitID = habit_id.get(i);
                String habitName = habit_name.get(i);
                int count = habit_count.get(i);
                int target = habit_target.get(i);
                habitDBHelper.insertStats(dateString, habitID, habitName, count);

                // Reset streak if needed
                if (count < target) {
                    habitDBHelper.updateStreak(String.valueOf(habitID), 0);
                }

                // Reset count to 0
                habitDBHelper.updateData(String.valueOf(habitID), habitName, String.valueOf(0), String.valueOf(target));
            }
        }
    }
}
