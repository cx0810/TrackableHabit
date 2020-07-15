package com.example.trackablehabit;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ReminderAlarmService extends IntentService {
    private static final String TAG = ReminderAlarmService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;

    Cursor cursor;

    public static PendingIntent getReminderPendingIntent(Context context, Uri uri) {
        Intent action = new Intent(context, ReminderAlarmService.class);
        action.setData(uri);
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public ReminderAlarmService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        assert intent != null;
        Uri uri = intent.getData();

        //display notification to view the task details
        Intent action = new Intent(this, Reminders.class);
        action.setData(uri);
        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //grab task description
        if (uri != null) {
            cursor = getContentResolver().query(uri, null, null, null, null);
        }

        String description = "";

        try {
            if (cursor != null && cursor.moveToFirst()) {
                description = HabitContract.getColumnString(cursor, HabitContract.AlarmReminderEntry.KEY_TITLE);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Notification note = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setContentTitle(getString(R.string.reminder_title))
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_alert)
                .setContentIntent(operation)
                .setAutoCancel(true)
                .build();

        assert manager != null;
        manager.notify(NOTIFICATION_ID, note);

    }
}


















