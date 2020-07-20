package com.example.trackablehabit;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.trackablehabit.HabitContract.*;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HabitDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "habitlist.db";
    private static final int DATABASE_VERSION = 7;

    private Context context;

    HabitDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_HABITLIST_TABLE = "CREATE TABLE " +
                HabitEntry.TABLE_NAME + " (" +
                HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HabitEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                HabitEntry.COLUMN_COUNT + " INTEGER, " +
                HabitEntry.COLUMN_TARGET + " INTEGER NOT NULL, " +
                HabitEntry.COLUMN_STREAK + " INTEGER, " +
                HabitEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        final String SQL_CREATE_ALARMLIST_TABLE =  "CREATE TABLE " +
                HabitContract.AlarmReminderEntry.TABLE_NAME + " (" +
                HabitContract.AlarmReminderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HabitContract.AlarmReminderEntry.KEY_TITLE + " TEXT NOT NULL, " +
                HabitContract.AlarmReminderEntry.KEY_DATE + " TEXT NOT NULL, " +
                HabitContract.AlarmReminderEntry.KEY_TIME + " TEXT NOT NULL, " +
                HabitContract.AlarmReminderEntry.KEY_REPEAT + " TEXT NOT NULL, " +
                HabitContract.AlarmReminderEntry.KEY_REPEAT_NO + " TEXT NOT NULL, " +
                HabitContract.AlarmReminderEntry.KEY_REPEAT_TYPE + " TEXT NOT NULL, " +
                HabitContract.AlarmReminderEntry.KEY_ACTIVE + " TEXT NOT NULL " + " );";

      
        final String SQL_CREATE_STATSLIST_TABLE = "CREATE TABLE " +
                StatsEntry.TABLE_NAME + " (" +
                StatsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StatsEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                StatsEntry.COLUMN_HABIT_ID + " INTEGER NOT NULL, " +
                StatsEntry.COLUMN_HABIT_NAME + " TEXT NOT NULL, " +
                StatsEntry.COLUMN_COUNT + " INTEGER" + ");";

        db.execSQL(SQL_CREATE_ALARMLIST_TABLE);
        db.execSQL(SQL_CREATE_HABITLIST_TABLE);
        db.execSQL(SQL_CREATE_STATSLIST_TABLE);
      
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HabitEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AlarmReminderEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StatsEntry.TABLE_NAME);
        onCreate(db);
    }

    void insertData(String name, int target) {
        SQLiteDatabase habitDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HabitEntry.COLUMN_NAME, name);
        contentValues.put(HabitEntry.COLUMN_COUNT, 0);
        contentValues.put(HabitEntry.COLUMN_TARGET, target);
        contentValues.put(HabitEntry.COLUMN_STREAK, 0);
        habitDatabase.insert(HabitEntry.TABLE_NAME, null, contentValues);
    }

    void insertStats(String date, int habitID, String habitName, int count) {
        SQLiteDatabase habitDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(StatsEntry.COLUMN_DATE, date);
        contentValues.put(StatsEntry.COLUMN_HABIT_ID, habitID);
        contentValues.put(StatsEntry.COLUMN_HABIT_NAME, habitName);
        contentValues.put(StatsEntry.COLUMN_COUNT, count);
        habitDatabase.insert(StatsEntry.TABLE_NAME, null, contentValues);
    }

    Cursor getAllHabits() {
        SQLiteDatabase habitDatabase = this.getReadableDatabase();
        return habitDatabase.query(
                HabitContract.HabitEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                HabitContract.HabitEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    Cursor getAllStats() {
        SQLiteDatabase habitDatabase = this.getReadableDatabase();
        return habitDatabase.query(
                HabitContract.StatsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                HabitContract.StatsEntry.COLUMN_DATE + " DESC"
        );
    }

    String queryLatestID() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + HabitEntry.TABLE_NAME
                + " WHERE " + HabitEntry._ID + " = "
                + "(SELECT MAX("+ HabitEntry._ID + ") FROM " + HabitEntry.TABLE_NAME + ")";

        Cursor cursor = db.rawQuery(query, null);

        String habitID;
        if (cursor != null && cursor.moveToFirst() ) {
            habitID = cursor.getString(cursor.getColumnIndex("_id"));
        } else {
            assert cursor != null;
            habitID = cursor.getString(0);
        }
        cursor.close();

        return habitID;
    }

    void updateData(String id, String name, String count, String target) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HabitEntry.COLUMN_NAME, name);
        contentValues.put(HabitEntry.COLUMN_COUNT, count);
        contentValues.put(HabitEntry.COLUMN_TARGET, target);

        long result = db.update(HabitEntry.TABLE_NAME, contentValues, "_id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Failed to update.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully updated!", Toast.LENGTH_SHORT).show();
        }
    }

    void updateStats(String habitID, String habitName, String count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(StatsEntry.COLUMN_HABIT_ID, habitID);
        contentValues.put(StatsEntry.COLUMN_HABIT_NAME, habitName);
        contentValues.put(StatsEntry.COLUMN_COUNT, count);

        long date = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
        String dateString = sdf.format(date);

        long result = db.update(StatsEntry.TABLE_NAME, contentValues, "date = ? AND habitID = ?" , new String[]{dateString, habitID});
        if (result == -1) {
            Toast.makeText(context, "Failed to update.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully updated!", Toast.LENGTH_SHORT).show();
        }
    }
}
