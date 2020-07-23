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
    private ArrayList<FeedModel> feedModelArrayList;

    FeedAdapter(Context context, ArrayList<FeedModel> feedModelArrayList) {

        this.context = context;
        this.feedModelArrayList = feedModelArrayList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_row, parent, false);

        return new MyViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final FeedModel feedModel = feedModelArrayList.get(position);

        holder.tv_message.setText(feedModel.getMessage());
        holder.tv_time.setText(feedModel.getTime());
        holder.tv_likes.setText(String.valueOf(feedModel.getLikes()));
        String message = feedModel.getComments() + context.getString(R.string.comments);
        holder.tv_comments.setText(message);

        holder.like_btn.setOnClickListener(v -> {
            int likesCount = parseInt(holder.tv_likes.getText().toString());
            int newCount = likesCount + 1;
            holder.tv_likes.setText(String.valueOf(newCount));
//                feedModelArrayList.get(position).setLikes(newCount);
        });

    }

    @Override
    public int getItemCount() {
        return feedModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_message, tv_time, tv_likes, tv_comments;
        TextView like_btn;

        MyViewHolder(View itemView) {
            super(itemView);

            tv_message = itemView.findViewById(R.id.tv_message);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_likes = itemView.findViewById(R.id.tv_like);
            tv_comments = itemView.findViewById(R.id.tv_comment);
            like_btn = itemView.findViewById(R.id.like_btn);
        }
    }


}
