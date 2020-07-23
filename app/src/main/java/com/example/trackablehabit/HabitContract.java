package com.example.trackablehabit;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

class HabitContract{

    private HabitContract() {}

    // Create one inner class for each table.
    static final class HabitEntry implements BaseColumns {
        static final String TABLE_NAME = "habitList";
        static final String COLUMN_NAME = "name";
        static final String COLUMN_COUNT = "count";
        static final String COLUMN_TARGET = "target";
        static final String COLUMN_STREAK = "streak";
        static final String COLUMN_TIMESTAMP = "timestamp";
    }

    static final class StatsEntry implements BaseColumns {
        static final String TABLE_NAME = "statsList";
        static final String COLUMN_DATE = "date";
        static final String COLUMN_HABIT_ID = "habitID";
        static final String COLUMN_HABIT_NAME = "name";
        static final String COLUMN_COUNT = "count";
    }

    static final String CONTENT_AUTHORITY = "com.delaroystudios.alarmreminder";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final String PATH_VEHICLE = "reminder-path";

    static final class AlarmReminderEntry implements BaseColumns {

        static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_VEHICLE);

        static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE;

        final static String TABLE_NAME = "remindersList";

        final static String _ID = BaseColumns._ID;

        static final String KEY_TITLE = "title";
        static final String KEY_DATE = "date";
        static final String KEY_TIME = "time";
        static final String KEY_REPEAT = "repeat";
        static final String KEY_REPEAT_NO = "repeat_no";
        static final String KEY_REPEAT_TYPE = "repeat_type";
        static final String KEY_ACTIVE = "active";
    }

    static final class UserEntry implements BaseColumns {
        static final String TABLE_NAME ="userList";
        static final String COLUMN_ID ="ID";
        static final String COLUMN_USERNAME ="username";
        static final String COLUMN_PASSWORD ="password";
        static final String COLUMN_LOGGEDIN = "loggedin";
    }

    static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
}
