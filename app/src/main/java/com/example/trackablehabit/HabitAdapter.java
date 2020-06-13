package com.example.trackablehabit;

import android.content.Context;
import android.database.Cursor;
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


    HabitAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    class HabitViewHolder extends RecyclerView.ViewHolder {

        TextView habitNameBtn;
        Button incrementButton;
        Button decrementButton;

        HabitViewHolder(@NonNull View itemView) {
            super(itemView);

            habitNameBtn = itemView.findViewById(R.id.habitNameBtn);
            incrementButton = itemView.findViewById(R.id.incrementCountBtn);
            decrementButton = itemView.findViewById(R.id.decrementCountBtn);
        }

    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.habit_individual, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        String stringHabitName = mCursor.getString(mCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_NAME));
        long id = mCursor.getLong(mCursor.getColumnIndex(HabitContract.HabitEntry._ID));

        holder.habitNameBtn.setText(stringHabitName);
        holder.itemView.setTag(id);
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
