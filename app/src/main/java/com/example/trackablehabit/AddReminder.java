package com.example.trackablehabit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;


public class AddReminder extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_VEHICLE_LOADER = 0;

    private EditText mTitleText;
    private TextView mDateText, mTimeText, mRepeatText, mRepeatNoText, mRepeatTypeText;
    private Calendar mCalendar;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private long mRepeatTime;
    private Switch mRepeatSwitch;
    private Button mSaveButton;
    private String mTitle, mTime, mDate, mRepeat, mRepeatNo, mRepeatType, mActive;

    private Uri mCurrentReminderUri;
    private boolean mVehicleHasChanged = false;

    private static final String KEY_TITLE = "title_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_DATE = "date_key";
    private static final String KEY_REPEAT = "repeat_key";
    private static final String KEY_REPEAT_NO = "repeat_no_key";
    private static final String KEY_REPEAT_TYPE = "repeat_type_key";
    private static final String KEY_ACTIVE = "active_key";

    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    @SuppressLint("ClickableViewAccessibility")
    private View.OnTouchListener mTouchListener = (v, event) -> {
        mVehicleHasChanged = true;
        return false;
    };


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Reminders");
        }

        Intent intent = getIntent();
        mCurrentReminderUri = intent.getData();

        if (mCurrentReminderUri == null) {
            setTitle("Add Reminder Details");
        } else {
            setTitle("Edit Reminder");
            /* this might be the difference between the edit and
            add page, need to figure out how to differentiate between
            the two
             */

            //getLoaderManager().initLoader(EXISTING_VEHICLE_LOADER, null, this);

        }

        // Initialize Views
        mTitleText = findViewById(R.id.reminder_title);
        mDateText = findViewById(R.id.set_date);
        mTimeText = findViewById(R.id.set_time);
        mRepeatText = findViewById(R.id.set_repeat);
        mRepeatNoText = findViewById(R.id.set_repeat_no);
        mRepeatTypeText = findViewById(R.id.set_repeat_type);
        mRepeatSwitch = findViewById(R.id.repeat_switch);
        mSaveButton = findViewById(R.id.reminder_save);

        // Initialize default values
        mActive = "true";
        mRepeat = "true";
        mRepeatNo = Integer.toString(1);
        mRepeatType = "Hour";

        mCalendar = Calendar.getInstance();

        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);

        mDay = mCalendar.get(Calendar.DATE);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mYear = mCalendar.get(Calendar.YEAR);

        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = mHour + ":" + mMinute;

        mTitleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString().trim();
                mTitleText.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateText.setText(mDate);
        mTimeText.setText(mTime);
        mRepeatNoText.setText(mRepeatNo);
        mRepeatTypeText.setText(mRepeatType);
        String message = getString(R.string.every) + mRepeatNo + getString(R.string.space) + mRepeatType + getString(R.string.bracket_seconds);
        mRepeatText.setText(message);

        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            mTitleText.setText(savedTitle);
            mTitle = savedTitle;

            String savedTime = savedInstanceState.getString(KEY_TIME);
            mTimeText.setText(savedTime);
            mTime = savedTime;

            String savedDate = savedInstanceState.getString(KEY_DATE);
            mDateText.setText(savedDate);
            mDate = savedDate;

            String savedRepeat = savedInstanceState.getString(KEY_REPEAT);
            mRepeatText.setText(savedRepeat);
            mRepeat = savedRepeat;

            String savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO);
            mRepeatNoText.setText(savedRepeatNo);
            mRepeatNo = savedRepeatNo;

            String savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE);
            mRepeatTypeText.setText(savedRepeatType);
            mRepeatType = savedRepeatType;

            mActive = savedInstanceState.getString(KEY_ACTIVE);
        }

        getSupportActionBar().setTitle("Add Reminder");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mSaveButton.setOnClickListener(v -> {
            saveReminder();
            finish();
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_TITLE, mTitleText.getText());
        outState.putCharSequence(KEY_TIME, mTimeText.getText());
        outState.putCharSequence(KEY_DATE, mDateText.getText());
        outState.putCharSequence(KEY_REPEAT, mRepeatText.getText());
        outState.putCharSequence(KEY_REPEAT_NO, mRepeatNoText.getText());
        outState.putCharSequence(KEY_REPEAT_TYPE, mRepeatTypeText.getText());
        outState.putCharSequence(KEY_ACTIVE, mActive);

    }

    // on clicking timepicker
    public void setTime(View view) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.show(getSupportFragmentManager(), "Timepickerdialog");
    }

    // on clicking datepicker
    public void setDate(View view) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getSupportFragmentManager(), "Datepickeredialog");
    }

    // obtain date from datepicker
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        mDay = dayOfMonth;
        mMonth = monthOfYear;
        mYear = year;
        mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        mDateText.setText(mDate);
    }

    // obtain time from timepicker
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        mHour = hourOfDay;
        mMinute = minute;
        if (minute < 10) {
            mTime = hourOfDay + ":" + "0" + minute;
        } else {
            mTime = hourOfDay + ":" + minute;
        }
        mTimeText.setText(mTime);
    }

    // on clicking repeat type button
    public void selectRepeatType(View view) {
        final String[] items = new String[5];

        items[0] = "Minute";
        items[1] = "Hour";
        items[2] = "Day";
        items[3] = "Week";
        items[4] = "Month";

        //  create list dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Type");
        builder.setItems(items, (dialog, item) -> {
            mRepeatType = items[item];
            mRepeatTypeText.setText(mRepeatType);
            String message = getString(R.string.every) + mRepeatNo + getString(R.string.space) + mRepeatType + getString(R.string.bracket_seconds);
            mRepeatText.setText(message);
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    // on clicking repeat interval button
    public void setRepeatNo(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter Number");

        // create edit text box to input repeat no
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                (dialog, whichButton) -> {
                    if (input.getText().toString().length() == 0) {
                        mRepeatNo = Integer.toString(1);
                        mRepeatNoText.setText(mRepeatNo);
                        String message = getString(R.string.every) + mRepeatNo + getString(R.string.space) + mRepeatType + getString(R.string.bracket_seconds);
                        mRepeatText.setText(message);
                    } else {
                        mRepeatNo = input.getText().toString().trim();
                        mRepeatNoText.setText(mRepeatNo);
                        String message = getString(R.string.every) + mRepeatNo + getString(R.string.space) + mRepeatType + getString(R.string.bracket_seconds);
                        mRepeatText.setText(message);
                    }
                });

        alert.setNegativeButton("Cancel", (dialog, which) -> {
            // do nth
        });
        alert.show();
    }

    // on clicking repeat switch
    public void onSwitchRepeat(View view) {
        boolean on = ((Switch) view).isChecked();

        if (on) {
            mRepeat = "true";
            String message = getString(R.string.every) + mRepeatNo + getString(R.string.space) + mRepeatType + getString(R.string.bracket_seconds);
            mRepeatText.setText(message);
        } else {
            mRepeat = "false";
            mRepeatText.setText(R.string.repeat_off);
        }
    }

    public void saveReminder() {
        ContentValues values = new ContentValues();

        values.put(HabitContract.AlarmReminderEntry.KEY_TITLE, mTitle);
        values.put(HabitContract.AlarmReminderEntry.KEY_DATE, mDate);
        values.put(HabitContract.AlarmReminderEntry.KEY_TIME, mTime);
        values.put(HabitContract.AlarmReminderEntry.KEY_REPEAT, mRepeat);
        values.put(HabitContract.AlarmReminderEntry.KEY_REPEAT_NO, mRepeatNo);
        values.put(HabitContract.AlarmReminderEntry.KEY_REPEAT_TYPE, mRepeatType);
        values.put(HabitContract.AlarmReminderEntry.KEY_ACTIVE, mActive);

        // set up calender for creating the notification
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);

        long selectedTimestamp =  mCalendar.getTimeInMillis();

        // check repeat type
        switch (mRepeatType) {
            case "Minute":
                mRepeatTime = Integer.parseInt(mRepeatNo) * milMinute;
                break;
            case "Hour":
                mRepeatTime = Integer.parseInt(mRepeatNo) * milHour;
                break;
            case "Day":
                mRepeatTime = Integer.parseInt(mRepeatNo) * milDay;
                break;
            case "Week":
                mRepeatTime = Integer.parseInt(mRepeatNo) * milWeek;
                break;
            case "Month":
                mRepeatTime = Integer.parseInt(mRepeatNo) * milMonth;
                break;
        }

        if (mCurrentReminderUri == null) {
            // this is a NEW reminder, so insert a new reminder into the provider,
            // returning the content URI for the new reminder.
            Uri newUri = getContentResolver().insert(HabitContract.AlarmReminderEntry.CONTENT_URI, values);

            // show a toast msg depending on whether or not the insertion was successful.
            if (newUri == null) {
                // if the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_reminder_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            int rowsAffected = getContentResolver().update(mCurrentReminderUri, values, null, null);

            // show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // if no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_reminder_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // create new notification
        if (mActive.equals("true")) {
            if (mRepeat.equals("true")) {
                new AlarmScheduler().setRepeatAlarm(getApplicationContext(), selectedTimestamp, mCurrentReminderUri, mRepeatTime);
            } else if (mRepeat.equals("false")) {
                new AlarmScheduler().setAlarm(getApplicationContext(), selectedTimestamp, mCurrentReminderUri);
            }

            Toast.makeText(this, "Alarm time is " + selectedTimestamp,
                    Toast.LENGTH_LONG).show();
        }

        // create toast to confirm new reminder
        Toast.makeText(getApplicationContext(), "Saved",
                Toast.LENGTH_SHORT).show();

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                HabitContract.AlarmReminderEntry._ID,
                HabitContract.AlarmReminderEntry.KEY_TITLE,
                HabitContract.AlarmReminderEntry.KEY_DATE,
                HabitContract.AlarmReminderEntry.KEY_TIME,
                HabitContract.AlarmReminderEntry.KEY_REPEAT,
                HabitContract.AlarmReminderEntry.KEY_REPEAT_NO,
                HabitContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                HabitContract.AlarmReminderEntry.KEY_ACTIVE,
        };

        return new CursorLoader(this,
                mCurrentReminderUri,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // move to the first row of the cursor and read data from it
        if (cursor.moveToFirst()) {
            int titleColumnIndex = cursor.getColumnIndex(HabitContract.AlarmReminderEntry.KEY_TITLE);
            int dateColumnIndex = cursor.getColumnIndex(HabitContract.AlarmReminderEntry.KEY_DATE);
            int timeColumnIndex = cursor.getColumnIndex(HabitContract.AlarmReminderEntry.KEY_TIME);
            int repeatColumnIndex = cursor.getColumnIndex(HabitContract.AlarmReminderEntry.KEY_REPEAT);
            int repeatNoColumnIndex = cursor.getColumnIndex(HabitContract.AlarmReminderEntry.KEY_REPEAT_NO);
            int repeatTypeColumnIndex = cursor.getColumnIndex(HabitContract.AlarmReminderEntry.KEY_REPEAT_TYPE);
            int activeColumnIndex = cursor.getColumnIndex(HabitContract.AlarmReminderEntry.KEY_ACTIVE);

            // extract out the value from the Cursor for the given column index
            String title = cursor.getString(titleColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String time = cursor.getString(timeColumnIndex);
            String repeat = cursor.getString(repeatColumnIndex);
            String repeatNo = cursor.getString(repeatNoColumnIndex);
            String repeatType = cursor.getString(repeatTypeColumnIndex);
            String active = cursor.getString(activeColumnIndex);

            // update views on screen with values from the database
            mTitleText.setText(title);
            mDateText.setText(date);
            mTimeText.setText(time);
            mRepeatNoText.setText(repeatNo);
            mRepeatTypeText.setText(repeatType);
            String message = getString(R.string.every) + mRepeatNo + getString(R.string.space) + mRepeatType + getString(R.string.bracket_seconds);
            mRepeatText.setText(message);
            // setup up active buttons
            // setup repeat switch
            if (repeat.equals("false")) {
                mRepeatSwitch.setChecked(false);
                mRepeatText.setText(R.string.repeat_off);

            } else if (repeat.equals("true")) {
                mRepeatSwitch.setChecked(true);
            }

        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

}
