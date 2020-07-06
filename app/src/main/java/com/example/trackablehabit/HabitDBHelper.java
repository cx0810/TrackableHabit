package com.example.trackablehabit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.trackablehabit.HabitContract.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HabitDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "habitlist.db";
    private static final int DATABASE_VERSION = 1;

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
        contentValues.put(HabitEntry.COLUMN_NAME, name);
        contentValues.put(HabitEntry.COLUMN_COUNT, 0);
        habitDatabase.insert(HabitEntry.TABLE_NAME, null, contentValues);
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

    ArrayList<String> queryXData() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> xData = new ArrayList<>();

        String query  = "SELECT " + HabitEntry.COLUMN_TIMESTAMP
                + " FROM " + HabitEntry.TABLE_NAME + " GROUP BY " + HabitEntry.COLUMN_TIMESTAMP;

        Cursor cursor = db.rawQuery(query, null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            xData.add(cursor.getString(0)); // change to different index?
        }
        cursor.close();

        return xData;
    }

    ArrayList<String> queryYData() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> yData = new ArrayList<>();

        String query  = "SELECT " + HabitEntry.COLUMN_COUNT + " FROM "
                + HabitEntry.TABLE_NAME // + " WHERE " + HabitEntry.COLUMN_COUNT + " IS NOT NULL "
                + " GROUP BY " + HabitEntry.COLUMN_TIMESTAMP;
        // is it really the sum???

        Cursor cursor = db.rawQuery(query, null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            yData.add(cursor.getString(0)); // change to different index?
        }
        cursor.close();

        return yData;
    }

    void updateData(String id, String name, String count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HabitEntry.COLUMN_NAME, name);
        contentValues.put(HabitEntry.COLUMN_COUNT, count);

        long result = db.update(HabitEntry.TABLE_NAME, contentValues, "_id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Failed to update.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully updated!", Toast.LENGTH_SHORT).show();
        }
    }
}
