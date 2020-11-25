package vu.mif.habit_tracker.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import vu.mif.habit_tracker.Models.Pet;

// This is our Pet table data access object
// Room writes code in the background for the written queries
@Dao
public interface petDAO {

    @Insert
    void insert(Pet pet);
    @Update
    void update(Pet pet);
    @Delete
    void delete(Pet pet);

    @Query("DELETE FROM Habit")
    void deleteAllPets();

    @Query("SELECT * FROM Habit")
    LiveData<List<Pet>> getAllPets();

}
