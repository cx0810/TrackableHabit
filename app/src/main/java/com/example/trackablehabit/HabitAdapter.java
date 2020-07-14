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
    private ArrayList habit_id, habit_name, habit_count;
    private OnItemClickListener mListener;
    private HabitDBHelper habitDBHelper;

    HabitAdapter(Context context, ArrayList habit_id, ArrayList habit_name, ArrayList habit_count) {
        mContext = context;
        this.habit_id = habit_id;
        this.habit_name = habit_name;
        this.habit_count = habit_count;
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
        TextView habitCount;
        Button incrementCountBtn;
        Button decrementCountBtn;

        HabitViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            habitNameBtn = itemView.findViewById(R.id.habitNameBtn);
            habitCount = itemView.findViewById(R.id.habitCount);
            incrementCountBtn = itemView.findViewById(R.id.incrementCountBtn);
            decrementCountBtn = itemView.findViewById(R.id.decrementCountBtn);

            habitNameBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick();
                    }
                }
            });

            incrementCountBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int count = parseInt(habitCount.getText().toString());
                        listener.onIncrementClick(count, habitCount);
                    }
                }
            });
            decrementCountBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int count = parseInt(habitCount.getText().toString());
                        listener.onDecrementClick(count, habitCount);
                    }
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

        setOnItemClickListener(new HabitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                Intent startIntent = new Intent(mContext, EditHabit.class);
                startIntent.putExtra("id", String.valueOf(habit_id.get(position + 1)));
                startIntent.putExtra("name", String.valueOf(habit_name.get(position + 1)));
                startIntent.putExtra("count", String.valueOf(habit_count.get(position + 1)));
                mContext.startActivity(startIntent);
            }

            @Override
            public void onIncrementClick(int count, TextView habitCount) {
                int newCount = count + 1;
                habitDBHelper = new HabitDBHelper(mContext);
                String idString = String.valueOf(habit_id.get(position + 1));
                String nameString = String.valueOf(habit_name.get(position + 1));
                String countString = String.valueOf(newCount);

                habitDBHelper.updateData(idString, nameString, countString);
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

                    habitDBHelper.updateData(idString, nameString, countString);
                    habitDBHelper.updateStats(idString, nameString, countString);
                    habitCount.setText(String.valueOf(newCount));
                }
            }
        });

        // to save data at the end of everyday
//        Calendar calendar = Calendar.getInstance();
//        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
//        SharedPreferences settings = mContext.getSharedPreferences("PREFS", 0);
//        int lastDay = settings.getInt("day", 0);
//
//        if (lastDay != currentDay) {
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putInt("day", currentDay);
//            editor.commit();
//
//            // save stats to database at the end of the day
//            long date = System.currentTimeMillis();
//            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
//            String dateString = sdf.format(date);
//            int habitID = parseInt((String) habit_id.get(position + 1));
//            String habitName = (String) habit_name.get(position + 1);
//            int count = parseInt((String) habit_count.get(position + 1));
//            habitDBHelper.insertStats(dateString, habitID, habitName, count);
//
//            // add code to reset to 0
//        }

    }

    @Override
    public int getItemCount() {
        return habit_id.size();
    }
}
