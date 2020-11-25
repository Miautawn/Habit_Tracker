package vu.mif.habit_tracker.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import vu.mif.habit_tracker.Models.User;

// This is our User table data access object
// Room writes code in the background for the written queries
@Dao
public interface userDAO {

    @Insert
    void insert(User user);
    @Update
    void update(User user);
    @Delete
    void delete(User user);

    @Query("DELETE FROM User")
    void deleteAllUsers();

    @Query("SELECT * FROM User")
    LiveData<List<User>> getAllUsers();

}
