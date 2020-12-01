package vu.mif.habit_tracker.ViewModels;

import android.app.Application;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vu.mif.habit_tracker.Models.Habit;
import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.Repositories.HabitRepository;
import vu.mif.habit_tracker.Repositories.UserRepository;
import vu.mif.habit_tracker.roomDB;

public class MainActivityViewModel extends AndroidViewModel {
    private MutableLiveData<Habit[]> stream;

    private HabitRepository habitRepo;
    private LiveData<List<Habit>> habits;
    private UserRepository userRepo;
    private LiveData<User> user;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        //Getting habits and user with livedata
        roomDB database = roomDB.getInstance(application);
        habitRepo = new HabitRepository(application);
        userRepo = new UserRepository(application);
        habits = habitRepo.getAllHabits();
        user = userRepo.getUser();

        //sito nelieciu
        stream = new MutableLiveData<>();
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

    //methods for user
    public void insertUser(User user) { if(this.user.getValue() == null) userRepo.insertUser(user); }
    public void updateUser(User user)
    {
        userRepo.updateUser(user);
    }
    public void deleteUser()
    {
        userRepo.deleteUser();
    }
    public LiveData<User> getUser()
    {
        return user;
    }


    public LiveData<Habit[]> getStream() {
        return stream;
    }

    public void updateCards(Habit[] cardHabits) {
        stream.postValue(cardHabits);
    }
}
