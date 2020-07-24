package com.example.trackablehabit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
    private ArrayList<Integer> habit_id, habit_count, habit_target, habit_streak;
    private OnItemClickListener mListener;
    private HabitDBHelper habitDBHelper;

    HabitAdapter(Context context, ArrayList<Integer> habit_id, ArrayList<String> habit_name,
                 ArrayList<Integer> habit_count, ArrayList<Integer> habit_target,
                 ArrayList<Integer> habit_streak) {
        mContext = context;
        this.habit_id = habit_id;
        this.habit_name = habit_name;
        this.habit_count = habit_count;
        this.habit_target = habit_target;
        this.habit_streak = habit_streak;
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
                int streak = habit_streak.get(position + 1);

                habitDBHelper.updateData(idString, nameString, countString, targetString);
                habitDBHelper.updateStats(idString, nameString, countString);

                if (newCount == habit_target.get(position + 1)) {
                    int newStreak = streak + 1;
                    habitDBHelper.updateStreak(idString, newStreak);
                    targetReachedAlert(nameString, newStreak);
                }

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
                    int streak = habit_streak.get(position + 1);

                    habitDBHelper.updateData(idString, nameString, countString, targetString);
                    habitDBHelper.updateStats(idString, nameString, countString);

                    if (count == habit_target.get(position + 1)) {
                        habitDBHelper.updateStreak(idString, streak - 1);
                    }

                    habitCount.setText(String.valueOf(newCount));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return habit_id.size();
    }

    private void targetReachedAlert(String habitName, int streak) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("Great job!");
        builder.setMessage("You've reached your target for " + habitName + " today.");

        builder.setPositiveButton("Ok", (dialog, which) -> {

        });

        if ((((streak == 1 || streak == 3) || streak == 7) || streak == 14) || streak == 30) {
            giveRewards(habitName, streak);
        }

        builder.show();
    }

    private void giveRewards(String habitName, int streak) {
        String rewardName = (streak == 1
                ? "1 Day"
                : streak == 3
                    ? "3 Days"
                    : streak == 7
                        ? "1 Week"
                        : streak == 14
                            ? "2 Weeks"
                            : "1 Month");

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        // Give the alert dialogue
        builder.setTitle("Congratulations!");
        builder.setMessage("You received the " + rewardName + " reward for your " + habitName + " habit.");

        // save data into news database table
        long timeMillis = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, h:mm a");
        String dateAndTimeString = sdf.format(timeMillis);

        String username = habitDBHelper.getCurrentUsername();

        habitDBHelper.insertNews(username, rewardName, habitName, dateAndTimeString);

        builder.setPositiveButton("Ok", (dialog, which) -> {
//                Intent startIntent = new Intent(mContext, Rewards.class);
//                mContext.startActivity(startIntent);
        });

        builder.show();
    }
}
