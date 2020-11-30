package vu.mif.habit_tracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import vu.mif.habit_tracker.Models.Habit;
import vu.mif.habit_tracker.Repositories.HabitRepository;
import vu.mif.habit_tracker.roomDB;

public class CustomHabitViewModel extends AndroidViewModel {
    private HabitRepository repo;
    private LiveData<List<Habit>> habits;

    public CustomHabitViewModel(@NonNull Application application) {
        super(application);
        roomDB database = roomDB.getInstance(application);
        repo = new HabitRepository(application);
        habits = repo.getAllHabits();
    }

    public void insertHabit(Habit Habit)
    {
        repo.insertHabit(Habit);
    }
    public void updateHabit(Habit Habit)
    {
        repo.updateHabit(Habit);
    }
    public void deleteHabit(Habit Habit)
    {
        repo.deleteHabit(Habit);
    }
    public void deleteAllHabits()
    {
        repo.deleteAllHabits();
    }
    public LiveData<List<Habit>> getAllHabits()
    {
        return habits;
    }
}
