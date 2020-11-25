package vu.mif.habit_tracker.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import vu.mif.habit_tracker.Models.Habit;

// This is our Habit table data access object
// Room writes code in the background for the written queries
@Dao
public interface habitDAO {

    @Insert
    void insert(Habit habit);
    @Update
    void update(Habit habit);
    @Delete
    void delete(Habit habit);

    @Query("DELETE FROM Habit")
    void deleteAllHabits();

    @Query("SELECT * FROM Habit")
    LiveData<List<Habit>> getAllHabits();
}
