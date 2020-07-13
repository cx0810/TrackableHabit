package com.example.trackablehabit;

import android.provider.BaseColumns;

class HabitContract{

    private HabitContract() {}

    // Create one inner class for each table.
    static final class HabitEntry implements BaseColumns {
        static final String TABLE_NAME = "habitList";
        static final String COLUMN_NAME = "name";
        static final String COLUMN_COUNT = "count";
//        public static final String COLUMN_RESET = "resetEvery";
//        public static final String COLUMN_INCREMENT = "increment";
//        public static final String COLUMN_TARGET = "target";
//        public static final String COLUMN_REMINDERS = "reminders";
//        public static final String COLUMN_NOTES = "notes";
        static final String COLUMN_TIMESTAMP = "timestamp";
    }

    static final class StatsEntry implements BaseColumns {
        static final String TABLE_NAME = "statsList";
        static final String COLUMN_DATE = "date";
        static final String COLUMN_HABIT_ID = "habitID";
        static final String COLUMN_HABIT_NAME = "name";
        static final String COLUMN_COUNT = "count";
    }

    // add one more table for reminders
}
