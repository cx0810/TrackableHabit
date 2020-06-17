package com.example.trackablehabit;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private OnItemClickListener mListener;

    HabitAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public interface OnItemClickListener {
        void onItemClick();
        void onIncrementClick(int count, TextView countView);
        void onDecrementClick(int count, TextView countView);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
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
                        int count = Integer.parseInt(habitCount.getText().toString());
                        listener.onIncrementClick(count, habitCount);
                    }
                }
            });
            decrementCountBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int count = Integer.parseInt(habitCount.getText().toString());
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
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        String stringHabitName = mCursor.getString(mCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_NAME));
        long id = mCursor.getLong(mCursor.getColumnIndex(HabitContract.HabitEntry._ID));
        int count = mCursor.getInt(mCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_COUNT));

        holder.habitNameBtn.setText(stringHabitName);
        holder.itemView.setTag(id);
        holder.habitCount.setText(String.valueOf(count));
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

}
