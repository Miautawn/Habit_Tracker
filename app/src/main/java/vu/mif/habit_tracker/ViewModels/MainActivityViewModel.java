package vu.mif.habit_tracker.ViewModels;

import android.app.Application;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
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
    private int currentIndex = 0;
    private MutableLiveData<Habit[]> habitCards;
    private Habit[] _habitCards = new Habit[5];

    private HabitRepository habitRepo;
    private LiveData<List<Habit>> habits;
    private UserRepository userRepo;
    private LiveData<User> user;

    private List<Habit> habitCardList;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        //Getting habits and user with livedata
        roomDB database = roomDB.getInstance(application);
        habitRepo = new HabitRepository(application);
        userRepo = new UserRepository(application);
        habits = habitRepo.getAllHabits();
        user = userRepo.getUser();

        //sito nelieciu
        habitCards = new MutableLiveData<>();
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
        MediatorLiveData<List<Habit>> data = new MediatorLiveData<>();
        data.addSource(habits, habits -> {
            habitCardList = habits;
            data.setValue(habits);
            updateData();
            updateCards();
        });
        return data;
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


    public LiveData<Habit[]> getHabitCards() {
        return habitCards;
    }

    private void updateData() {
        if (habitCardList != null) {
            _habitCards[0] = habitCardList.get(currentIndex % habitCardList.size());
            _habitCards[1] = habitCardList.get((currentIndex + 1) % habitCardList.size());
            _habitCards[2] = habitCardList.get((currentIndex + 2) % habitCardList.size());
            _habitCards[3] = habitCardList.get((currentIndex + 3) % habitCardList.size());
            _habitCards[4] = habitCardList.get((currentIndex + 4) % habitCardList.size());
        }
    }

    public void swipeRight() {
        if (habitCardList != null) {
            currentIndex += 1;
            updateData();
            updateCards();
        }
    }

    public void swipeLeft() {
        if (habitCardList != null) {
            if (currentIndex == 0) {
                currentIndex = habitCardList.size() - 1;
            } else {
                currentIndex -= 1;
            }
            updateData();
            updateCards();
        }
    }

    public void updateCards() {
        habitCards.postValue(_habitCards);
    }
}
