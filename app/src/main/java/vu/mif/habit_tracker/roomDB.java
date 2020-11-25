package vu.mif.habit_tracker;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import vu.mif.habit_tracker.Models.Habit;
import vu.mif.habit_tracker.Models.Pet;
import vu.mif.habit_tracker.Models.User;

import vu.mif.habit_tracker.DAOs.*;

@Database(entities = {User.class, Habit.class, Pet.class}, version = 1, exportSchema = false)
public abstract class roomDB extends RoomDatabase {
    //Declaring roomDB instance singleton
    private static roomDB instance;

    //all the Data access object initializers
    public abstract userDAO getUserDAO();
    public abstract habitDAO getHabitDAO();
    public abstract petDAO getPetDAO();

    public static synchronized roomDB getInstance(Context context)
    {
        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(), roomDB.class, "vu.mif.habit_tracker.localDB").fallbackToDestructiveMigration().build();
        }
        return instance;
    }

}
