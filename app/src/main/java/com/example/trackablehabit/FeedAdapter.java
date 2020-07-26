package com.example.trackablehabit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {

    private Context context;

    private HabitDBHelper db;

    private ArrayList<String> username_arr, reward_arr, habit_arr, date_arr;
    private ArrayList<Integer> id_arr, likes_arr, comments_arr;

    private String username, reward, habit;
    private int id;

    FeedAdapter(Context context, ArrayList<Integer> id_arr,
                ArrayList<String> username_arr, ArrayList<String> reward_arr,
                ArrayList<String> habit_arr, ArrayList<String> date_arr,
                ArrayList<Integer> likes_arr, ArrayList<Integer> comments_arr) {

        this.context = context;
        this.id_arr = id_arr;
        this.username_arr = username_arr;
        this.reward_arr = reward_arr;
        this.habit_arr = habit_arr;
        this.date_arr = date_arr;
        this.likes_arr = likes_arr;
        this.comments_arr = comments_arr;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.feed_row, parent, false);

        return new MyViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        id = id_arr.get(position);
        username = username_arr.get(position);
        reward = reward_arr.get(position);
        habit = habit_arr.get(position);

        String message =  username + " received the " + reward + " award for keeping up with the "
                + habit + " habit!";

        holder.tv_message.setText(message);
        holder.tv_time.setText(date_arr.get(position));
        holder.tv_likes.setText(String.valueOf(likes_arr.get(position)));

        holder.like_btn.setOnClickListener(v -> {
            holder.like_btn.setImageResource(R.drawable.ic_like);
            int likes = parseInt(holder.tv_likes.getText().toString());
            int newCount = likes + 1;
            db = new HabitDBHelper(context);
            db.updateLikes(String.valueOf(id), newCount);
            holder.tv_likes.setText(String.valueOf(newCount));
        });

    }

    @Override
    public int getItemCount() {
        return id_arr.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_message, tv_time, tv_likes;
        ImageView like_btn;

        MyViewHolder(View itemView) {
            super(itemView);

            tv_message = itemView.findViewById(R.id.tv_message);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_likes = itemView.findViewById(R.id.tv_like);
            like_btn = itemView.findViewById(R.id.like_btn);
        }
    }


}
