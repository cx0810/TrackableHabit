package com.example.trackablehabit;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.Integer.parseInt;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private Context mContext;
    private ArrayList<String> habit_name;
    private ArrayList<Integer> habit_id, habit_count, habit_target;
    private OnItemClickListener mListener;
    private HabitDBHelper habitDBHelper;

    HabitAdapter(Context context, ArrayList<Integer> habit_id, ArrayList<String> habit_name,
                 ArrayList<Integer> habit_count, ArrayList<Integer> habit_target) {
        mContext = context;
        this.habit_id = habit_id;
        this.habit_name = habit_name;
        this.habit_count = habit_count;
        this.habit_target = habit_target;
    }

    public interface OnItemClickListener {
        void onItemClick();
        void onIncrementClick(int count, TextView countView);
        void onDecrementClick(int count, TextView countView);
    }

    private void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    class HabitViewHolder extends RecyclerView.ViewHolder {
        TextView habitNameBtn;
        TextView habitCount, habitTarget;
        Button incrementCountBtn;
        Button decrementCountBtn;

        HabitViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            habitNameBtn = itemView.findViewById(R.id.habitNameBtn);
            habitCount = itemView.findViewById(R.id.habitCount);
            habitTarget = itemView.findViewById(R.id.habitTarget);
            incrementCountBtn = itemView.findViewById(R.id.incrementCountBtn);
            decrementCountBtn = itemView.findViewById(R.id.decrementCountBtn);

            habitNameBtn.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick();
                }
            });

            incrementCountBtn.setOnClickListener(v -> {
                if (listener != null) {
                    int count = parseInt(habitCount.getText().toString());
                    listener.onIncrementClick(count, habitCount);
                }
            });
            decrementCountBtn.setOnClickListener(v -> {
                if (listener != null) {
                    int count = parseInt(habitCount.getText().toString());
                    listener.onDecrementClick(count, habitCount);
                }
            });
        }

    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.habit_individual, parent, false);
        return new HabitViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, final int position) {

        holder.habitNameBtn.setText(String.valueOf(habit_name.get(position)));
        holder.itemView.setTag(String.valueOf(habit_id.get(position)));
        holder.habitCount.setText(String.valueOf(habit_count.get(position)));
        holder.habitTarget.setText(String.valueOf(habit_target.get(position)));

        setOnItemClickListener(new HabitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                Intent startIntent = new Intent(mContext, EditHabit.class);
                startIntent.putExtra("id", String.valueOf(habit_id.get(position + 1)));
                startIntent.putExtra("name", String.valueOf(habit_name.get(position + 1)));
                startIntent.putExtra("count", String.valueOf(habit_count.get(position + 1)));
                startIntent.putExtra("target", String.valueOf(habit_target.get(position + 1)));
                mContext.startActivity(startIntent);
            }

            @Override
            public void onIncrementClick(int count, TextView habitCount) {
                int newCount = count + 1;
                habitDBHelper = new HabitDBHelper(mContext);
                String idString = String.valueOf(habit_id.get(position + 1));
                String nameString = String.valueOf(habit_name.get(position + 1));
                String countString = String.valueOf(newCount);
                String targetString = String.valueOf(habit_target.get(position + 1));

                habitDBHelper.updateData(idString, nameString, countString, targetString);
                habitDBHelper.updateStats(idString, nameString, countString);
                habitCount.setText(String.valueOf(newCount));
            }

            @Override
            public void onDecrementClick(int count, TextView habitCount) {
                if (count > 0) {
                    int newCount = count - 1;
                    habitDBHelper = new HabitDBHelper(mContext);
                    String idString = String.valueOf(habit_id.get(position + 1));
                    String nameString = String.valueOf(habit_name.get(position + 1));
                    String countString = String.valueOf(newCount);
                    String targetString = String.valueOf(habit_target.get(position + 1));

                    habitDBHelper.updateData(idString, nameString, countString, targetString);
                    habitDBHelper.updateStats(idString, nameString, countString);
                    habitCount.setText(String.valueOf(newCount));
                }
            }
        });

        // to save data at the end of everyday


//        Calendar calendar = Calendar.getInstance();
//        calendar.clear(Calendar.HOUR);
//        calendar.clear(Calendar.HOUR_OF_DAY);
//        calendar.clear(Calendar.MINUTE);
//        calendar.clear(Calendar.SECOND);
//        calendar.clear(Calendar.MILLISECOND);
//        long currentDay = calendar.getTimeInMillis();
//
//        SharedPreferences settings = mContext.getSharedPreferences("PREFS", 0);
//        long lastDay = settings.getLong("day", 0);
//
//        long diffMillis = currentDay - lastDay;
//
//        if (diffMillis >= (3600000  * 24)) {
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putLong("day", currentDay);
//            editor.apply();
//
//            // save stats to database at the end of the day
//            long date = System.currentTimeMillis();
//            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
//            String dateString = sdf.format(date);
//
//            habitDBHelper = new HabitDBHelper(mContext);
//            for (int i = habit_id.size() - 1; i > 0; i++) {
//                int habitID = habit_id.get(i);
//                String habitName = habit_name.get(i);
//                int count = habit_count.get(i);
//                habitDBHelper.insertStats(dateString, habitID, habitName, count);
//            }
//
//            // add code to reset to 0
//        }

    }

    @Override
    public int getItemCount() {
        return habit_id.size();
    }
}
