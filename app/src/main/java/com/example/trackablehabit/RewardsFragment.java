package com.example.trackablehabit;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RewardsFragment extends Fragment {

    private SQLiteDatabase habitDatabase;
    private HabitDBHelper habitDBHelper;
    private RewardsAdapter rewardsAdapter;

    private ArrayList<Integer> habit_id, habit_streak;
    private ArrayList<String> habit_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rewards, container, false);

        habitDBHelper = new HabitDBHelper(getActivity());
        habitDatabase = habitDBHelper.getReadableDatabase();

        habit_id = new ArrayList<>();
        habit_name = new ArrayList<>();
        habit_streak = new ArrayList<>();
        storeDataInArrays();

        RecyclerView recyclerView = rootView.findViewById(R.id.rewardsRecyclerView2);
        rewardsAdapter = new RewardsAdapter(getActivity(), habit_id, habit_name, habit_streak);
        recyclerView.setAdapter(rewardsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                habit_streak.add(cursor.getInt(4));
            }
        }
    }

}
