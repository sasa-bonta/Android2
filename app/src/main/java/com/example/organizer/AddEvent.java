package com.example.organizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.organizer.SQL.AppDatabase;
import com.example.organizer.SQL.Event;
import com.example.organizer.SQL.MainAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEvent extends AppCompatActivity {

    private static final String TAG = "AddEvent";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    final Calendar cal = Calendar.getInstance();
    final Calendar current = Calendar.getInstance();

    int[] year = new int[1];
    int[] month = new int[1];
    int[] day = new int[1];
    int[] hour = new int[1];
    int[] minute = new int[1];

    final static int RQS_1 = 1;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        List<Event> eventList = new ArrayList<>();
        AppDatabase database;
        MainAdapter adapter;

        database = AppDatabase.getInstance(this);
        eventList = database.eventDao().getAll();
        context = getApplicationContext();

        addEvent();

        final TextView mDisplayDate = findViewById(R.id.date);
        final TextView mDisplayTime = findViewById(R.id.time);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    Calendar cal = Calendar.getInstance();
                year[0] = cal.get(Calendar.YEAR);
                month[0] = cal.get(Calendar.MONTH);
                day[0] = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddEvent.this,
                        android.R.style.Theme_Black,
                        mDateSetListener,
                        year[0], month[0], day[0]);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                cal.set(Calendar.DAY_OF_MONTH, day);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.YEAR, year);

                month+=1;
                Log.d(TAG, "onDateSet : dd/mm/yyyy: "  + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);


            }
        };

        mDisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    Calendar cal = Calendar.getInstance();
                hour[0] = cal.get(Calendar.HOUR_OF_DAY);
                minute[0] = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(AddEvent.this,
                        android.R.style.Theme_Black,
                        (TimePickerDialog.OnTimeSetListener) mTimeSetListener,
                        hour[0], minute[0], true);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.d(TAG, "onTimeSet : hh:mm: " + hour + ":" + minute);

                String time = hour + ":" + minute;
                String timeTest = mDisplayTime.getText().toString().trim();
                mDisplayTime.setText(time);

                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, minute);

                Log.d(TAG, "cal.set(time): " +cal.getTime());
                Log.e(TAG, "cal.set(time): " +cal.getTimeInMillis());
            }
        };
    }



    public void addEvent() {

        final EditText editText = findViewById(R.id.edit_text);
        final TextView date = findViewById(R.id.date);
        final TextView time = findViewById(R.id.time);
        final Button button = findViewById(R.id.addBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sDescription = editText.getText().toString().trim();
                String sDate = date.getText().toString().trim();
                String sTime = time.getText().toString().trim();

                List<Event> eventList = new ArrayList<>();
                AppDatabase database;
                MainAdapter adapter;

                database = AppDatabase.getInstance(AddEvent.this);
                eventList = database.eventDao().getAll();

                Log.d(TAG, "current.set(time): " +current.getTime());
                Log.e(TAG, "current.set(time): " +current.getTimeInMillis());

                if (!sDescription.equals("") && !sTime.equals("") && !sDate.equals("")) {
                    Event event = new Event();
                    event.setDescription(sDescription);
                    event.setDate(sDate);
                    event.setTime(sTime);

                    database.eventDao().insert(event);

                    editText.setText("");
                    date.setText("");
                    time.setText("");

                    if (cal.getTimeInMillis() > current.getTimeInMillis()) {
                        Log.e(TAG, "cal > current");
                        Log.e(TAG, "time" + sDate + sTime);
                        setAlarm(sDescription, sDate, sTime);
                    }

                }
            }
        });
    }

    private void setAlarm(String text, String date, String time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);
        intent.putExtra("event", text);
        intent.putExtra("time", date);
        intent.putExtra("date", time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + time;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        finish();

    }

}