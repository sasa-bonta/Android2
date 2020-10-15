package com.example.organizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEvent extends AppCompatActivity {

    private static final String TAG = "AddEvent";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        List<Event> eventList = new ArrayList<>();
        AppDatabase database;
        MainAdapter adapter;

        database = AppDatabase.getInstance(this);
        eventList = database.eventDao().getAll();

        addEvent();

        final TextView mDisplayDate = findViewById(R.id.date);
        final TextView mDisplaTime = findViewById(R.id.time);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddEvent.this,
                        android.R.style.Theme_Black,
                        mDateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month +=1;
                Log.d(TAG, "onDateSet : dd/mm/yyyy: "  + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                mDisplayDate.setText("Date: " + date);
            }
        };

        mDisplaTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                final int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(AddEvent.this,
                        android.R.style.Theme_Black,
                        (TimePickerDialog.OnTimeSetListener) mTimeSetListener,
                        hour, minute, true);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.d(TAG, "onTimeSet : hh:mm: " + hour + ":" + minute);

                String time = hour + ":" + minute;
                String timeTest = mDisplaTime.getText().toString().trim();
                mDisplaTime.setText("Time: " + time);
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

                if (!sDescription.equals("") && !sTime.equals("") && !sDate.equals("")) {
                    Event event = new Event();
                    event.setDescription(sDescription);
                    event.setDate(sDate);
                    event.setTime(sTime);

                    database.eventDao().insert(event);

                    editText.setText("");
                    date.setText("");
                    time.setText("");
                }
            }
        });
    }
}