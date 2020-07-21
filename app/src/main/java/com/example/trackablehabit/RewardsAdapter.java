package com.example.trackablehabit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.RewardsViewHolder> {

    private Context mContext;
    private ArrayList<Integer> habit_id, habit_streak;
    private ArrayList<String> habit_name;
    private HabitDBHelper habitDBHelper;
    private ImageView star1, star2, star3, star4, star5;
    private ProgressBar progressBar1, progressBar2, progressBar3, progressBar4, progressBar5;

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

        int streak = habit_streak.get(position);
        holder.habitStreak.setText(String.valueOf(streak));

        // decide which rewards to appear
        String message = null;
        if (streak >= 1) {
            star1.setImageResource(R.drawable.full_reward1);
            if (streak >= 3) {
                star2.setImageResource(R.drawable.full_reward2);
                if (streak >= 7) {
                    star3.setImageResource(R.drawable.full_reward3);
                    if (streak >= 14) {
                        star4.setImageResource(R.drawable.full_reward4);
                        if (streak >= 30) {
                            star5.setImageResource(R.drawable.full_reward5);
                        } else {
                            message = "You need " + (30 - streak) + " more to reach the 1 month reward!";
                        }
                    } else {
                        message = "You need " + (14 - streak) + " more to reach the 2 Weeks reward!";
                    }
                } else {
                    message = "You need " + (7 - streak) + " more to reach the 1 Week reward!";
                }
            } else {
                message = "You need " + (3 - streak) + " more to reach the 3 Days reward!";
            }
        } else {
            message = "Start on your habit to get the 1 Day reward!";
        }
        holder.motivationMessage.setText(message);

        progressBar1.setProgress(streak * 100);
        progressBar2.setProgress((streak * 100) / 3);
        progressBar3.setProgress((streak * 100) / 7);
        progressBar4.setProgress((streak * 100) / 14);
        progressBar5.setProgress((streak * 100) / 30);
    }

    @Override
    public int getItemCount() {
        return habit_id.size();
    }

    class RewardsViewHolder extends RecyclerView.ViewHolder {

        TextView habitName, habitStreak, motivationMessage;
        RewardsViewHolder(@NonNull View itemView) {
            super(itemView);
            habitName = itemView.findViewById(R.id.habitName);
            habitStreak = itemView.findViewById(R.id.habitStreak);
            motivationMessage = itemView.findViewById(R.id.motivationMessage);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            star4 = itemView.findViewById(R.id.star4);
            star5 = itemView.findViewById(R.id.star5);
            progressBar1 = itemView.findViewById(R.id.progressBar1);
            progressBar2 = itemView.findViewById(R.id.progressBar2);
            progressBar3 = itemView.findViewById(R.id.progressBar3);
            progressBar4 = itemView.findViewById(R.id.progressBar4);
            progressBar5 = itemView.findViewById(R.id.progressBar5);
        }
    }

}
