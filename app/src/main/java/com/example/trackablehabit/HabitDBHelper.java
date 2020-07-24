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
    private static final int DATABASE_VERSION = 3;

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

        final String SQL_CREATE_USERLIST_TABLE ="CREATE TABLE " +
                UserEntry.TABLE_NAME + " (" +
                UserEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserEntry.COLUMN_USERNAME + " TEXT NOT NULL, " +
                UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL, " +
                UserEntry.COLUMN_LOGGEDIN + " INTEGER NOT NULL" + ");";

        final String SQL_CREATE_NEWSLIST_TABLE = "CREATE TABLE " +
                NewsEntry.TABLE_NAME + " (" +
                NewsEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NewsEntry.COLUMN_USERNAME + " TEXT NOT NULL, " +
                NewsEntry.COLUMN_REWARD_NAME + " TEXT NOT NULL, " +
                NewsEntry.COLUMN_HABIT_NAME + " TEXT NOT NULL, " +
                NewsEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                NewsEntry.COLUMN_LIKES + " INTEGER NOT NULL, " +
                NewsEntry.COLUMN_COMMENTS + " INTEGER NOT NULL" + ");";

        db.execSQL(SQL_CREATE_ALARMLIST_TABLE);
        db.execSQL(SQL_CREATE_HABITLIST_TABLE);
        db.execSQL(SQL_CREATE_STATSLIST_TABLE);
        db.execSQL(SQL_CREATE_USERLIST_TABLE);
        db.execSQL(SQL_CREATE_NEWSLIST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HabitEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AlarmReminderEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StatsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NewsEntry.TABLE_NAME);
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

    long addUser(String user, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserEntry.COLUMN_USERNAME, user);
        contentValues.put(UserEntry.COLUMN_PASSWORD, password);
        contentValues.put(UserEntry.COLUMN_LOGGEDIN, 0);
        return db.insert(UserEntry.TABLE_NAME, null, contentValues);
    }

    int checkUser(String username, String password) {
        int userID = -1;
        String[] columns = { UserEntry.COLUMN_ID };
        SQLiteDatabase db = getReadableDatabase();
        String selection = UserEntry.COLUMN_USERNAME + "=?" + " and " + UserEntry.COLUMN_PASSWORD + "=?";
        String[] selectionArgs = { username, password };
        Cursor cursor = db.query(UserEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            userID = cursor.getInt(cursor.getColumnIndex("ID"));
        }
        assert cursor != null;
        cursor.close();
        db.close();

        return userID;
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

    void insertNews(String username, String rewardName, String habitName, String date) {
        SQLiteDatabase habitDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NewsEntry.COLUMN_USERNAME, username);
        contentValues.put(NewsEntry.COLUMN_REWARD_NAME, rewardName);
        contentValues.put(NewsEntry.COLUMN_HABIT_NAME, habitName);
        contentValues.put(NewsEntry.COLUMN_DATE, date);
        contentValues.put(NewsEntry.COLUMN_LIKES, 0);
        contentValues.put(NewsEntry.COLUMN_COMMENTS, 0);
        habitDatabase.insert(NewsEntry.TABLE_NAME, null, contentValues);
    }

    String getCurrentUsername() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + UserEntry.TABLE_NAME
                + " WHERE " + UserEntry.COLUMN_LOGGEDIN + " = 1";

        Cursor cursor = db.rawQuery(query, null);

        String currentUsername;
        if (cursor != null && cursor.moveToFirst() ) {
            currentUsername = cursor.getString(cursor.getColumnIndex("username"));
        } else {
            assert cursor != null;
            currentUsername = cursor.getString(1);
        }
        cursor.close();

        return currentUsername;
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

    Cursor getAllNews() {
        SQLiteDatabase habitDatabase = this.getReadableDatabase();
        return habitDatabase.query(
                HabitContract.NewsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                NewsEntry._ID + " DESC"
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

    void updateUser(String id, String username, String password, int loggedIn) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserEntry.COLUMN_USERNAME, username);
        contentValues.put(UserEntry.COLUMN_PASSWORD, password);
        contentValues.put(UserEntry.COLUMN_LOGGEDIN, loggedIn);

        long result = db.update(UserEntry.TABLE_NAME, contentValues, "ID=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Failed to update.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully updated!", Toast.LENGTH_SHORT).show();
        }
    }

    void updateUserLogOut() {
        SQLiteDatabase db = this.getWritableDatabase();
        String instruction = "UPDATE " + UserEntry.TABLE_NAME
                + " SET " + UserEntry.COLUMN_LOGGEDIN + " = " + 0
                + " WHERE " + UserEntry.COLUMN_ID + " = " + queryCurrentUserID();
        db.execSQL(instruction);

    }

    private int queryCurrentUserID() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + UserEntry.TABLE_NAME
                + " WHERE " + UserEntry.COLUMN_LOGGEDIN + " = 1";

        Cursor cursor = db.rawQuery(query, null);

        int userID;
        if (cursor != null && cursor.moveToFirst() ) {
            userID = cursor.getInt(cursor.getColumnIndex("ID"));
        } else {
            assert cursor != null;
            userID = cursor.getInt(1);
        }
        cursor.close();

        return userID;

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

    void updateStreak(String habitID, int streak) {
        SQLiteDatabase db = this.getWritableDatabase();
        String instruction = "UPDATE " + HabitEntry.TABLE_NAME
                + " SET " + HabitEntry.COLUMN_STREAK + " = " + streak
                + " WHERE " + HabitEntry._ID + " = " + habitID;
        db.execSQL(instruction);
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

    void updateLikes(String id, int likes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NewsEntry.COLUMN_LIKES, likes);

        long result = db.update(NewsEntry.TABLE_NAME, contentValues, "_id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Failed to update.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully updated!", Toast.LENGTH_SHORT).show();
        }
    }
}
