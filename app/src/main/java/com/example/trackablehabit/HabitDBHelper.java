package com.example.trackablehabit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.trackablehabit.HabitContract.*;

import androidx.annotation.Nullable;

public class HabitDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "habitlist.db";
    private static final int DATABASE_VERSION = 1;

    HabitDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_HABITLIST_TABLE = "CREATE TABLE " +
                HabitEntry.TABLE_NAME + " (" +
                HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HabitEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                HabitEntry.COLUMN_COUNT + " INTEGER, " +
                HabitEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        db.execSQL(SQL_CREATE_HABITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HabitEntry.TABLE_NAME);
        onCreate(db);
    }

    void insertData(String name) {
        SQLiteDatabase habitDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HabitContract.HabitEntry.COLUMN_NAME, name);
        contentValues.put(HabitEntry.COLUMN_COUNT, 0);
        habitDatabase.insert(HabitContract.HabitEntry.TABLE_NAME, null, contentValues);
    }

    Cursor getAllHabits() {
        SQLiteDatabase habitDatabase = this.getWritableDatabase();
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


//    public void incrementData(String id, String name) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        int count = Integer.parseInt(HabitEntry.COLUMN_COUNT);
//        contentValues.put(HabitEntry.COLUMN_NAME, name);
//        contentValues.put(HabitEntry.COLUMN_COUNT, count++);
//        db.update(HabitEntry.TABLE_NAME, contentValues, "id = ?", new String[]{id});
//    }
//
//    public void decrementData(String id, String name) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        int count = Integer.parseInt(HabitEntry.COLUMN_COUNT);
//        contentValues.put(HabitEntry.COLUMN_NAME, name);
//        contentValues.put(HabitEntry.COLUMN_COUNT, count--);
//        db.update(HabitEntry.TABLE_NAME, contentValues, "id = ?", new String[]{id});
//    }

//    public boolean updateData(String id, String name, String count) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(HabitEntry.COLUMN_NAME, name);
//        contentValues.put(HabitEntry.COLUMN_COUNT, count);
//        db.update(HabitEntry.TABLE_NAME, contentValues, "id = ?", new String[]{id});
//        return true;
//    }
}
