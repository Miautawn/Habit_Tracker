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

    private Habit habitCards[] = new Habit[5];

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
        //updateData();
        //updateCards();
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


//    private List<Integer> data = new ArrayList<>(Arrays.asList(
//            Color.parseColor("#ffebee"),
//            Color.parseColor("#ffcdd2"),
//            Color.parseColor("#ef9a9a"),
//            Color.parseColor("#e57373"),
//            Color.parseColor("#ef5350"),
//            Color.parseColor("#f44336"),
//            Color.parseColor("#e53935"),
//            Color.parseColor("#d32f2f"),
//            Color.parseColor("#c62828"),
//            Color.parseColor("#b71c1c"),
//            Color.parseColor("#880e4f"),
//            Color.parseColor("#ad1457"),
//            Color.parseColor("#c2185b"),
//            Color.parseColor("#d81b60"),
//            Color.parseColor("#e91e63"),
//            Color.parseColor("#ec407a"),
//            Color.parseColor("#f06292"),
//            Color.parseColor("#f48fb1"),
//            Color.parseColor("#f8bbd0"),
//            Color.parseColor("#fce4ec"),
//
//            Color.parseColor("#fff8e1"),
//            Color.parseColor("#ffecb3"),
//            Color.parseColor("#ffe082"),
//            Color.parseColor("#ffd54f"),
//            Color.parseColor("#ffca28"),
//            Color.parseColor("#ffc107"),
//            Color.parseColor("#ffb300"),
//            Color.parseColor("#ffa000"),
//            Color.parseColor("#ff8f00"),
//            Color.parseColor("#ff6f00"),
//
//            Color.parseColor("#bf360c"),
//            Color.parseColor("#d84315"),
//            Color.parseColor("#e64a19"),
//            Color.parseColor("#f4511e"),
//            Color.parseColor("#ff5722"),
//            Color.parseColor("#ff7043"),
//            Color.parseColor("#ff8a65"),
//            Color.parseColor("#ffab91"),
//            Color.parseColor("#ffccbc"),
//            Color.parseColor("#fbe9e7")
//    ));

//    private int currentIndex = 0;
//
//    private void updateData() {
//        colors[0] = data.get(currentIndex % data.size());
//        colors[1] = data.get((currentIndex + 1) % data.size());
//        colors[2] = data.get((currentIndex + 2) % data.size());
//        colors[3] = data.get((currentIndex + 3) % data.size());
//        colors[4] = data.get((currentIndex + 4) % data.size());
//    }
//
//    public void swipeRight() {
//        currentIndex += 1;
//        updateData();
//        updateCards();
//    }
//
//    public void swipeLeft() {
//        if (currentIndex == 0) {
//            currentIndex = data.size() - 1;
//        } else {
//            currentIndex -= 1;
//        }
//        updateData();
//        updateCards();
//    }
//
    public void updateCards(Habit[] cardHabits) {
        stream.postValue(cardHabits);
    }
}
