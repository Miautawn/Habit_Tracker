package vu.mif.habit_tracker;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import vu.mif.habit_tracker.Models.Habit;
import vu.mif.habit_tracker.Models.Pet;
import vu.mif.habit_tracker.Models.User;

@Database(entities = {User.class, Habit.class, Pet.class}, version = 1, exportSchema = false)
public abstract class roomDB extends RoomDatabase {
    //Declaring roomDB instance singleton
    private static roomDB instance;


}
