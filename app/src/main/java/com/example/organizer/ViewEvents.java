package com.example.organizer;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizer.SQL.AppDatabase;
import com.example.organizer.SQL.Event;
import com.example.organizer.SQL.MainAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewEvents extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Event> eventsList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    AppDatabase database;
    MainAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity);

        recyclerView = findViewById(R.id.recycler_view);

        database = AppDatabase.getInstance(this);
        eventsList = database.eventDao().getAll();

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MainAdapter(ViewEvents.this, eventsList);
        recyclerView.setAdapter(adapter);

    }
}
