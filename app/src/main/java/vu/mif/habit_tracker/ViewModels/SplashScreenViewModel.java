package vu.mif.habit_tracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

import vu.mif.habit_tracker.Models.Habit;
import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.Repositories.HabitRepository;
import vu.mif.habit_tracker.Repositories.UserRepository;

public class SplashScreenViewModel extends AndroidViewModel {
    private UserRepository repo;
    private LiveData<User> user;
    private HabitRepository habitRepo;
    private LiveData<List<Habit>> habits;

    public SplashScreenViewModel(@NonNull Application application) {
        super(application);
        repo = new UserRepository(application);
        user = repo.getUser();
        habitRepo = new HabitRepository(application);
        habits = habitRepo.getAllHabits();
    }

    //methods for habits
    public void insertHabit(Habit Habit) { habitRepo.insertHabit(Habit); }
    public void updateHabit(Habit Habit)
    {
        habitRepo.updateHabit(Habit);
    }
    public void deleteHabit(Habit Habit)
    {
        habitRepo.deleteHabit(Habit);
    }
    public void deleteAllHabits()
    {
        habitRepo.deleteAllHabits();
    }
    public LiveData<List<Habit>> getAllHabits()
    {
        return habits;
    }

    public LiveData<User> getUser()
    {
        return user;
    }
    public boolean isLogedIn() { return repo.isLogedIn();}
}
