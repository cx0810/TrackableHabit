package com.example.trackablehabit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.RewardsViewHolder> {

    private Context mContext;
    private ArrayList<Integer> habit_id, habit_streak;
    private ArrayList<String> habit_name;
    private HabitDBHelper habitDBHelper;

    RewardsAdapter(Context context, ArrayList<Integer> habit_id,
                   ArrayList<String> habit_name, ArrayList<Integer> habit_streak) {
        mContext = context;
        this.habit_id = habit_id;
        this.habit_name = habit_name;
        this.habit_streak = habit_streak;
    }

    @NonNull
    @Override
    public RewardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.rewards_individual, parent, false);
        return new RewardsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardsViewHolder holder, int position) {
        holder.habitName.setText(habit_name.get(position));
        holder.habitStreak.setText(String.valueOf(habit_streak.get(position)));
    }

    @Override
    public int getItemCount() {
        return habit_id.size();
    }

    class RewardsViewHolder extends RecyclerView.ViewHolder {

        TextView habitName, habitStreak;
        RewardsViewHolder(@NonNull View itemView) {
            super(itemView);
            habitName = itemView.findViewById(R.id.habitName);
            habitStreak = itemView.findViewById(R.id.habitStreak);
        }
    }

}
