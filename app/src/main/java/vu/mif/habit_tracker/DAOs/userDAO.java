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

    // We would only need to delete one user, so specifying which one is unnecessary
    //@Delete
    //void delete(User user);

    @Query("DELETE FROM User")
    void deleteUser();

    // "LIMIT 1" should be just a precaution as there should be only one user
    @Query("SELECT *  FROM User LIMIT 1")
    LiveData<User> getUser();

}
