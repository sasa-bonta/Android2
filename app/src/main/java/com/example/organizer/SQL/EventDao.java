package com.example.organizer.SQL;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface EventDao {

    @Insert(onConflict = REPLACE)
    void insert(Event event);

    @Query("SELECT * FROM myTable")
    List<Event> getAll();

    @Delete
    void delete(Event event);

    @Query("UPDATE myTable SET description = :sDescription WHERE id = :sId")
    void update(int sId, String sDescription);
}
