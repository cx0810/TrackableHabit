package com.example.trackablehabit;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NewsFragment extends Fragment {

    private HabitDBHelper db;

    private ArrayList<String> username_arr, reward_arr, habit_arr, date_arr;
    private ArrayList<Integer> id_arr, likes_arr, comments_arr;

    private FeedAdapter adapterFeed;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.newsRecyclerView);

        id_arr = new ArrayList<>();
        username_arr = new ArrayList<>();
        reward_arr = new ArrayList<>();
        habit_arr = new ArrayList<>();
        date_arr = new ArrayList<>();
        likes_arr = new ArrayList<>();
        comments_arr = new ArrayList<>();

        storeNewsInArray();

        adapterFeed = new FeedAdapter(getActivity(), id_arr, username_arr, reward_arr, habit_arr, date_arr, likes_arr, comments_arr);
        recyclerView.setAdapter(adapterFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    private void storeNewsInArray() {
        db = new HabitDBHelper(getActivity());
        Cursor cursor = db.getAllNews();
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                id_arr.add(cursor.getInt(0));
                username_arr.add(cursor.getString(1));
                reward_arr.add(cursor.getString(2));
                habit_arr.add(cursor.getString(3));
                date_arr.add(cursor.getString(4));
                likes_arr.add(cursor.getInt(5));
                comments_arr.add(cursor.getInt(6));
            }
        }
    }

}
