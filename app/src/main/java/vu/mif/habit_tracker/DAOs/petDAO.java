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

    // We would only need to delete one pet, so specifying which one is unnecessary
    //@Delete
    //void delete(Pet pet);

    @Query("DELETE FROM Pet")
    void deletePet();

    // "LIMIT 1" should be just a precaution as there should be only one pet
    @Query("SELECT * FROM Pet LIMIT 1")
    LiveData<Pet> getPet();

}
